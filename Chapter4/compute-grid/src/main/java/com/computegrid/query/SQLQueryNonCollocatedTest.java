package com.computegrid.query;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.annotations.QuerySqlFunction;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.SoccerClub;
import com.computegrid.query.dto.SoccerPlayer;

public class SQLQueryNonCollocatedTest {

	private static final String CLUB_SQL_CACHE = "club_sql_cache";
	private static final String PLAYER_SQL_CACHE = "player_sql_cache";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<Long, SoccerPlayer> playerCacheConfig = new CacheConfiguration<>();
		playerCacheConfig.setName(PLAYER_SQL_CACHE);
		playerCacheConfig.setIndexedTypes(Long.class, SoccerPlayer.class);
		playerCacheConfig.setCacheMode(CacheMode.PARTITIONED);

		CacheConfiguration<Long, SoccerClub> clubCacheConfig = new CacheConfiguration<>();
		clubCacheConfig.setName(CLUB_SQL_CACHE);
		clubCacheConfig.setIndexedTypes(Long.class, SoccerClub.class);
		clubCacheConfig.setCacheMode(CacheMode.REPLICATED);

		cfg.setCacheConfiguration(playerCacheConfig, clubCacheConfig);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Long, SoccerPlayer> playerCache = Ignition.ignite().getOrCreateCache(PLAYER_SQL_CACHE);
			IgniteCache<Long, SoccerClub> clubCache = Ignition.ignite().getOrCreateCache(CLUB_SQL_CACHE);

			SoccerClub barcelona = new SoccerClub(1l, "Barcelona");
			SoccerClub chelsea = new SoccerClub(2l, "Chelsea");

			clubCache.put(barcelona.getId(), barcelona);
			clubCache.put(chelsea.getId(), chelsea);

			long id = 1;
			SoccerPlayer suarez = new SoccerPlayer(id++, "Luis Suárez", 578699.00d, barcelona.getId());
			SoccerPlayer messi = new SoccerPlayer(id++, "Leo Messi", 200000.00d, barcelona.getId());
			SoccerPlayer hazard = new SoccerPlayer(id++, "Eden Hazard", 178999.00d, chelsea.getId());

			playerCache.put(suarez.getId(), suarez);
			playerCache.put(messi.getId(), messi);
			playerCache.put(hazard.getId(), hazard);

			// Search 'Barcelona' players
			System.out.println("Find Barcelona players");
			SqlQuery<Long, SoccerPlayer> sqlQuery = new SqlQuery<>(SoccerPlayer.class,
					"from SoccerPlayer, \"" + CLUB_SQL_CACHE + "\".SoccerClub "
							+ "where SoccerPlayer.clubId = SoccerClub.id and SoccerClub.name = ?");
			QueryCursor<Entry<Long, SoccerPlayer>> resultCursor = playerCache.query(sqlQuery.setArgs("Barcelona"));
			resultCursor.forEach(e -> {
				System.out.println(e.getValue());
			});

		}
	}

	@QuerySqlFunction
	public static String myUpperCase(String name) {
		return name.toUpperCase();
	}
}
