package com.cep.streamer;

import static com.cep.streamer.DataStreamerTest.OHLC_CACHE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.cep.streamer.dto.StockStatus;

public class DataStreamerQuery {

	public static void main(String[] args) throws IOException, URISyntaxException, Exception {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<String, StockStatus> marketDataCache = Ignition.ignite().getOrCreateCache(OHLC_CACHE);
			System.out.println(marketDataCache.get("AAPL"));

			SqlFieldsQuery query = new SqlFieldsQuery("SELECT max(s.high) from StockStatus s");
			while (true) {

				List<List<?>> res = marketDataCache.query(query).getAll();
				System.out.println("max for last 5 sec");
				System.out.println(res);
				Thread.sleep(1000);
			}

		}
	}

}
