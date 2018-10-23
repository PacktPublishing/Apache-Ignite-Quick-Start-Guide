package com.computegrid.query;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.computegrid.query.dto.Player;

public class TextQueryTest {

	private static final String PLAYER_TEXT_CACHE = "Player_Text_Cache";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<Long, Player> playerCacheConfig = new CacheConfiguration<>();
		playerCacheConfig.setName(PLAYER_TEXT_CACHE);
		playerCacheConfig.setIndexedTypes(Long.class, Player.class);
		cfg.setCacheConfiguration(playerCacheConfig);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Long, Player> playerCache = Ignition.ignite().getOrCreateCache(PLAYER_TEXT_CACHE);

			long id = 1l;
			playerCache.put(id, new Player(id++, "Leo Messi", "Barcelona", 996999995.00d));
			playerCache.put(id, new Player(id++, "Christiano Ronaldo", "Juventus", 2000000.00d));
			playerCache.put(id, new Player(id++, "Paul Pogba", "Manchester United", 1000000.00d));
			playerCache.put(id, new Player(id++, "Neymar", "PSG", 99699999.00d));
			playerCache.put(id, new Player(id++, "Luis Suárez", "Barcelona", 578699.00d));

			System.out.println("United Soccer Players");
			TextQuery<Long, Player> txt = new TextQuery<>(Player.class, "United");

			QueryCursor<Entry<Long, Player>> unitedPlayerCursor = playerCache.query(txt);

			unitedPlayerCursor.forEach(e -> {
				System.out.println(e.getValue());
			});

			txt = new TextQuery<>(Player.class, "Barcenola~");

			System.out.println("Fuzzy search for 'Barcelona' as 'Barcenola'");
			QueryCursor<Entry<Long, Player>> fuzzyCursor = playerCache.query(txt);

			fuzzyCursor.forEach(e -> {
				System.out.println(e.getValue());
			});
			
			System.out.println("multifield search for name and team");
			txt = new TextQuery<>(Player.class, "name:\"Neymar\" OR team:\"Juventus\"");
			QueryCursor<Entry<Long, Player>> multiField = playerCache.query(txt);

			multiField.forEach(e -> {
				System.out.println(e.getValue());
			});
		}
	}

}
