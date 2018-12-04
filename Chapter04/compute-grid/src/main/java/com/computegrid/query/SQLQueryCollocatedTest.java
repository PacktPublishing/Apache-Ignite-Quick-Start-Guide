package com.computegrid.query;

import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.SoccerClub;
import com.computegrid.query.dto.SoccerPlayer;
import com.computegrid.query.dto.SoccerPlayerKey;

public class SQLQueryCollocatedTest {

	private static final String CLUB_SQL_CACHE = "club_sql_cache";
	private static final String PLAYER_SQL_CACHE = "player_sql_cache";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<SoccerPlayerKey, SoccerPlayer> playerCacheConfig = new CacheConfiguration<>();
		playerCacheConfig.setName(PLAYER_SQL_CACHE);
		playerCacheConfig.setIndexedTypes(SoccerPlayerKey.class, SoccerPlayer.class);
		playerCacheConfig.setCacheMode(CacheMode.PARTITIONED);

		CacheConfiguration<Long, SoccerClub> clubCacheConfig = new CacheConfiguration<>();
		clubCacheConfig.setName(CLUB_SQL_CACHE);
		clubCacheConfig.setIndexedTypes(Long.class, SoccerClub.class);
		clubCacheConfig.setCacheMode(CacheMode.PARTITIONED);

		cfg.setCacheConfiguration(playerCacheConfig, clubCacheConfig);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<SoccerPlayerKey, SoccerPlayer> playerCache = Ignition.ignite()
					.getOrCreateCache(PLAYER_SQL_CACHE);
			IgniteCache<Long, SoccerClub> clubCache = Ignition.ignite().getOrCreateCache(CLUB_SQL_CACHE);

			SoccerClub barcelona = new SoccerClub(1l, "Barcelona");
			SoccerClub chelsea = new SoccerClub(2l, "Chelsea");

			clubCache.put(barcelona.getId(), barcelona);
			clubCache.put(chelsea.getId(), chelsea);

			long id = 1;
			SoccerPlayer suarez = new SoccerPlayer(id++, "Luis Suárez", 578699.00d, barcelona.getId());
			SoccerPlayer messi = new SoccerPlayer(id++, "Leo Messi", 200000.00d, barcelona.getId());
			SoccerPlayer hazard = new SoccerPlayer(id++, "Eden Hazard", 178999.00d, chelsea.getId());

			playerCache.put(new SoccerPlayerKey(suarez.getId(), suarez.getClubId()), suarez);
			playerCache.put(new SoccerPlayerKey(messi.getId(), messi.getClubId()), messi);
			playerCache.put(new SoccerPlayerKey(hazard.getId(), hazard.getClubId()), hazard);

			SqlQuery<SoccerPlayerKey, SoccerPlayer> sqlQuery = new SqlQuery<>(SoccerPlayer.class, "name = ?");

			QueryCursor<Entry<SoccerPlayerKey, SoccerPlayer>> resultCursor = playerCache
					.query(sqlQuery.setArgs("Eden Hazard"));
			resultCursor.forEach(e -> {
				System.out.println(e.getValue());
			});

			// Search 'Barcelona' players
			System.out.println("Find Barcelona players");
			sqlQuery = new SqlQuery<>(SoccerPlayer.class, "from SoccerPlayer, \"" + CLUB_SQL_CACHE + "\".SoccerClub "
					+ "where SoccerPlayer.clubId = SoccerClub.id and SoccerClub.name = ?");
			resultCursor = playerCache.query(sqlQuery.setArgs("Barcelona"));
			resultCursor.forEach(e -> {
				System.out.println(e.getValue());
			});

			System.out.println("Find name of each soccer player");
			SqlFieldsQuery fieldQry = new SqlFieldsQuery("select name from SoccerPlayer");
			FieldsQueryCursor<List<?>> playerNamecursor = playerCache.query(fieldQry);

			playerNamecursor.forEach(name -> {
				System.out.println(name);
			});

			System.out.println("Find average, max, min salary of players");
			fieldQry = new SqlFieldsQuery("select avg(salary), max(salary), min(salary) from SoccerPlayer");
			FieldsQueryCursor<List<?>> result = playerCache.query(fieldQry);

			result.forEach(r -> {
				System.out.println("avg=" + r.get(0) + " max=" + r.get(1) + " , min= " + r.get(2));
			});

			System.out.println("Find max, min salary of players group by club");
			fieldQry = new SqlFieldsQuery("select c.name , max(p.salary), min(p.salary) from SoccerPlayer p, \""
					+ CLUB_SQL_CACHE + "\".SoccerClub c where p.clubId = c.id group by c.name");

			result = playerCache.query(fieldQry);

			result.forEach(r -> {
				System.out.println("Club =" + r.get(0) + " max=" + r.get(1) + " , min= " + r.get(2));
			});
		}
	}
}
