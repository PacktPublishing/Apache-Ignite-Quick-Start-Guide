package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.zk.ZookeeperDiscoverySpi;

public class HelloWorld {

	public static void main(String[] args) throws InterruptedException {
		try (Ignite ignite = Ignition.start()) {
			IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myFirstIgniteCache");

			for (int i = 0; i < 10; i++)
				cache.put(i, Integer.toString(i));

			for (int i = 0; i < 10; i++)
				System.out.println("Fetched [key=" + i + ", val=" + cache.get(i) + ']');
		}
	}

}
