package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ClientAccessCache {
	public static void main(String[] args) throws InterruptedException {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			
			IgniteCache<Long, String> cache = ignite.getOrCreateCache("data");
			while(true) {
				long longValue = System.currentTimeMillis();
				cache.put(longValue, String.valueOf(longValue));
				
				System.out.println(cache.get(longValue));
				Thread.sleep(1000);
			}
		}
	}
}
