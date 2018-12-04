package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.zk.ZookeeperDiscoverySpi;

public class HelloWorld {

	public static void main(String[] args) throws InterruptedException {
		ZookeeperDiscoverySpi zkDiscoSpi = new ZookeeperDiscoverySpi();

		zkDiscoSpi.setZkConnectionString("127.0.0.1:2181");
		zkDiscoSpi.setSessionTimeout(30_000);

		zkDiscoSpi.setZkRootPath("/apacheIgnite");
		zkDiscoSpi.setJoinTimeout(10_000);

		IgniteConfiguration cfg = new IgniteConfiguration();
		// Override default discovery SPI.
		cfg.setDiscoverySpi(zkDiscoSpi);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myFirstIgniteCache");

			for (int i = 0; i < 10; i++)
				cache.put(i, Integer.toString(i));

			for (int i = 0; i < 10; i++)
				System.out.println("Fetched [key=" + i + ", val=" + cache.get(i) + ']');
			Thread.sleep(999999999);
		}
	}

}
