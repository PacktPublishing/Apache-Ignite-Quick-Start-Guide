package com.computegrid.mapreduce.session;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.SoccerClub;
import com.computegrid.query.dto.SoccerPlayer;
import com.computegrid.query.dto.SoccerPlayerKey;
import static com.computegrid.mapreduce.IgniteMapReduceTest.*;

import java.util.Map;

public class JobSessionTest {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
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

			populatePlayersAndClubs(clubNames, playerCache, clubCache);

			IgniteCompute compute = ignite.compute();
			Map<String, Object> res = compute.execute(SessionTaskSplitAdapter.class, clubNames);
			System.out.println(res.get("top"));
			System.out.println(res.get("sum"));
		}
	}
}
