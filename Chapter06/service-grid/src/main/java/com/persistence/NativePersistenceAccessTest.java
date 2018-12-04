package com.persistence;

import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class NativePersistenceAccessTest {

	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);

		Ignite ignite = Ignition.start(cfg);
		ignite.cluster().active(true);
		IgniteCache<String, MyPojo> myPojoCache = ignite.getOrCreateCache("NativePersistence");
		IntStream.range(1, 100000).forEach(i -> {
			System.out.println(myPojoCache.get(String.valueOf(i)));
		});

	}

}

