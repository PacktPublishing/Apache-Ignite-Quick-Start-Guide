package com.deploy.monitoring;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMetrics;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class CacheMonitoring {

	public static void main(String[] args) throws InterruptedException {
		CacheConfiguration<Long, String> myCacheConf = new CacheConfiguration<>();
		String randomCacheName = "My Cache " + UUID.randomUUID().toString();
		myCacheConf.setName(randomCacheName);
		myCacheConf.setStatisticsEnabled(true);
		myCacheConf.setIndexedTypes(Long.class, String.class);

		// Starting the node.
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setCacheConfiguration(myCacheConf);
		Ignite ignite = Ignition.start(cfg);

		IgniteCache<Long, String> cache = ignite.getOrCreateCache(randomCacheName);
		ExecutorService svc = ignite.executorService();
		svc.submit(new Runnable() {

			@Override
			public void run() {
				while (true) {
					int val = new Random().nextInt(50000);
					String string = cache.get(Long.valueOf(val));
					for (int i = 0; i <= val; i++) {
						cache.put(Long.valueOf(i), "value=" + i);
					}
					
				}
			}
		});

		System.out.println("Cache Metrics...");
		while (true) {
			CacheMetrics metrics = cache.metrics();
			if (metrics.getCacheMisses() > 0 || metrics.getCacheHits() > 0) {
				System.out.println("*********************************");
				System.out.println("Avg Get time = " + metrics.getAverageGetTime());
				System.out.println("Avg Put time = " + metrics.getAveragePutTime());
				System.out.println("Cache Hits  = " + metrics.getCacheHits());
				System.out.println("Cache Misses = " + metrics.getCacheMisses());
				System.out.println("Cache Evictions = " + metrics.getCacheEvictions());
			}
			Thread.sleep(200);
		}

	}

}
