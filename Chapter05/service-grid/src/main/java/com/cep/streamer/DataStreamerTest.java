package com.cep.streamer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.cep.streamer.dto.StockStatus;

public class DataStreamerTest {

	public static final String OHLC_CACHE = "ohlc_status";
	static DateFormat format= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static void main(String[] args) throws IOException, URISyntaxException, Exception {

		URI uri = new DataStreamerTest().getClass().getClassLoader().getResource("intraday_1min_AAPL.csv").toURI();
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		CacheConfiguration<String, StockStatus> config = new CacheConfiguration<>();
		config.setExpiryPolicyFactory(FactoryBuilder.factoryOf(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, 5))));
		config.setName(OHLC_CACHE);
		config.setIndexedTypes(String.class, StockStatus.class);
		
		cfg.setCacheConfiguration(config);
		
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<String, StockStatus> marketDataCache = Ignition.ignite().getOrCreateCache(config);

			try (IgniteDataStreamer<String, StockStatus> mktStmr = ignite.dataStreamer(OHLC_CACHE)) {
				mktStmr.allowOverwrite(true);
				mktStmr.perNodeParallelOperations(2);
				
				while (true) {
					try (Stream<String> stream = Files.lines(Paths.get(uri))) {
						stream.forEach(str -> {
							StringTokenizer st = new StringTokenizer(str, ",");
							StockStatus ohlc = new StockStatus();
							try {
								ohlc.setTimestamp(format.parse(st.nextToken()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							ohlc.setOpen(Double.parseDouble(st.nextToken()));
							ohlc.setHigh(Double.parseDouble(st.nextToken()));
							ohlc.setLow(Double.parseDouble(st.nextToken()));
							ohlc.setClose(Double.parseDouble(st.nextToken()));
							ohlc.setVolume(Long.parseLong(st.nextToken()));
							ohlc.setSymbol("AAPL");

							mktStmr.addData(ohlc.getSymbol(), ohlc);
							System.out.println(String.format("Adding for %s", ohlc));
						});
					}
				}
			}

		}
	}

}
