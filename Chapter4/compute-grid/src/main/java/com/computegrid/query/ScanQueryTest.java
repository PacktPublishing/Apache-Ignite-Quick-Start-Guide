package com.computegrid.query;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.Player;

public class ScanQueryTest {

	private static final String PLAYER_SCAN_CACHE = "Player_Scan_Cache";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Long, Player> playerCache = Ignition.ignite().getOrCreateCache(PLAYER_SCAN_CACHE);
			long id = 1l;
			playerCache.put(id, new Player(id++, "Leo Messi", "Barcelona", 996999995.00d));
			playerCache.put(id, new Player(id++, "Christiano Ronaldo", "Juventus", 2000000.00d));
			playerCache.put(id, new Player(id++, "Paul Pogba", "Manchester United", 1000000.00d));
			playerCache.put(id, new Player(id++, "Neymar", "PSG", 99699999.00d));
			playerCache.put(id, new Player(id++, "Luis Suárez", "Barcelona", 578699.00d));

			System.out.println("Barcelona Soccer Players");
			QueryCursor<Entry<Long, Player>> barcelonaPlayersCursor = playerCache
					.query(new ScanQuery<Long, Player>((i, p) -> p.getTeam().equalsIgnoreCase("Barcelona")));

			barcelonaPlayersCursor.forEach(e -> {
				System.out.println(e.getValue());
			});

			System.out.println("Rich Soccer Players");
			QueryCursor<Entry<Long, Player>> richPlayers = playerCache
					.query(new ScanQuery<Long, Player>((i, p) -> p.getSalary() > 1000000));
			
			richPlayers.forEach(e -> {
				System.out.println(e.getValue());
			});

		}
	}

}
