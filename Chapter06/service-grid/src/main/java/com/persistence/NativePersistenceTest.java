package com.persistence;

import java.io.Serializable;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class NativePersistenceTest {

	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);

		
		CacheConfiguration<String, MyPojo> config = new CacheConfiguration<>();
		config.setName("NativePersistence");
		//config.setBackups(1);
		config.setIndexedTypes(String.class, MyPojo.class);

		cfg.setCacheConfiguration(config);

		Ignite ignite = Ignition.start(cfg);
		ignite.cluster().active(true);
		
		IgniteCache<String, MyPojo> myPojoCache = ignite.getOrCreateCache(config);
		myPojoCache.removeAll();
		IntStream.range(1, 100000).forEach(i -> {
			System.out.println(String.format("putting %s in cache", i));
			myPojoCache.put(String.valueOf(i), new MyPojo(String.valueOf(i)));
		});

	}

}

class MyPojo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;

	public MyPojo(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "MyPojo [message=" + message + "]";
	}

}
