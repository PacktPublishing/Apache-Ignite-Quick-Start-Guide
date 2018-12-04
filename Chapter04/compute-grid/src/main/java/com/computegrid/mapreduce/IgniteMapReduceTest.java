package com.computegrid.mapreduce;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.cluster.ClusterTopologyException;
import org.apache.ignite.compute.ComputeExecutionRejectedException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobFailoverException;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeJobResultPolicy;
import org.apache.ignite.compute.ComputeLoadBalancer;
import org.apache.ignite.compute.ComputeTask;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.resources.LoadBalancerResource;

import com.computegrid.query.dto.SoccerClub;
import com.computegrid.query.dto.SoccerPlayer;
import com.computegrid.query.dto.SoccerPlayerKey;

public class IgniteMapReduceTest {

	public static final String CLUB_JOB_CACHE = "club_job_sql_cache";
	public static final String PLAYER_JOB_CACHE = "player_job_sql_cache";

	public static String[] clubNames = new String[] { "FC Barcelona", "Real Madrid", "Manchester United", "Manchester City",
			"Chelsea", "Juventus", "PSG", "FC Bayern Munich" };
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		CacheConfiguration<SoccerPlayerKey, SoccerPlayer> playerCacheConfig = new CacheConfiguration<>();
		playerCacheConfig.setName(PLAYER_JOB_CACHE);
		playerCacheConfig.setIndexedTypes(SoccerPlayerKey.class, SoccerPlayer.class);
		playerCacheConfig.setCacheMode(CacheMode.PARTITIONED);

		CacheConfiguration<Long, SoccerClub> clubCacheConfig = new CacheConfiguration<>();
		clubCacheConfig.setName(CLUB_JOB_CACHE);
		clubCacheConfig.setIndexedTypes(Long.class, SoccerClub.class);
		clubCacheConfig.setCacheMode(CacheMode.REPLICATED);

		cfg.setCacheConfiguration(playerCacheConfig, clubCacheConfig);


		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<SoccerPlayerKey, SoccerPlayer> playerCache = Ignition.ignite()
					.getOrCreateCache(PLAYER_JOB_CACHE);
			IgniteCache<Long, SoccerClub> clubCache = Ignition.ignite().getOrCreateCache(CLUB_JOB_CACHE);

			double expectedExpense =populatePlayersAndClubs(clubNames, playerCache, clubCache);

			IgniteCompute compute = ignite.compute();
			double totalCalculatedExpense = compute.execute(new ComputeTask<String[], Double>() {
				private static final long serialVersionUID = 1L;
				@LoadBalancerResource
				private ComputeLoadBalancer balancer;

				@Override
				public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, String[] names)
						throws IgniteException {
					Map<ComputeJob, ClusterNode> result = new HashMap<>();
					for (String clubName : names) {
						ComputeJobAdapter job = new ClubExpenseCalculatorJob(clubName);
						ClusterNode balancedNode = balancer.getBalancedNode(job, null);
						result.put(job, balancedNode);
					}
					return result;
				}

				@Override
				public ComputeJobResultPolicy result(ComputeJobResult res, List<ComputeJobResult> rcvd)
						throws IgniteException {
					IgniteException exception = res.getException();
					if (exception != null) {
						if (exception instanceof IgniteException) {
							return ComputeJobResultPolicy.FAILOVER;
						} else {
							throw exception;
						}
					} else {
						return ComputeJobResultPolicy.WAIT;
					}
				}

				@Override
				public Double reduce(List<ComputeJobResult> results) throws IgniteException {
					double sum = 0;

				    for (ComputeJobResult res : results)
				      sum += res.<Double>getData();

				    return sum;
				}

			}, clubNames);
			
			System.out.println(String.format("Total Expense  calculated=%s, expected=%s",totalCalculatedExpense, expectedExpense));
		}
	}

	public static double populatePlayersAndClubs(String[] clubNames,
			IgniteCache<SoccerPlayerKey, SoccerPlayer> playerCache, IgniteCache<Long, SoccerClub> clubCache) {
		Long clubId = 1l;
		long playerId = 1;
		double sum =0.0d;
		Random random = new Random();
		for (String clubName : clubNames) {
			SoccerClub club = new SoccerClub(clubId++, clubName);
			clubCache.put(club.getId(), club);
			int numberOfPlayersToCreate = random.nextInt(20);

			for (int i = 0; i <= numberOfPlayersToCreate+1; i++) {
				double salary = random.nextDouble() * 100000.00;
				SoccerPlayer player = new SoccerPlayer(playerId++, String.format("%s-player%s", clubName, i),
						salary, club.getId());
				playerCache.put(new SoccerPlayerKey(player.getId(), club.getId()), player);
				sum+=salary;
			}
		}
		
		return sum;
	}
}
