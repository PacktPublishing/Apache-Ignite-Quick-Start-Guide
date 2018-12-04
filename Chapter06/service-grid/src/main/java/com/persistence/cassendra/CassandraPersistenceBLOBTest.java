package com.persistence.cassendra;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.store.cassandra.CassandraCacheStoreFactory;
import org.apache.ignite.cache.store.cassandra.datasource.DataSource;
import org.apache.ignite.cache.store.cassandra.persistence.KeyValuePersistenceSettings;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.datastax.driver.core.policies.RoundRobinPolicy;

public class CassandraPersistenceBLOBTest {

	public static void main(String[] args) throws IOException {

		// Configuring Cassandra's persistence
		DataSource cassandraDataSource = new DataSource();
		cassandraDataSource.setContactPoints("127.0.0.1");
		RoundRobinPolicy robinPolicy = new RoundRobinPolicy();
		cassandraDataSource.setLoadBalancingPolicy(robinPolicy);
		cassandraDataSource.setReadConsistency("ONE");
		cassandraDataSource.setWriteConsistency("ONE");
		
		//Read the persistence settings for Cassandra Key-Value persistance
		String file = CassandraPersistencePOJOTest.class.getClassLoader()
				.getResource("META-INF//conference-persistence-settings2.xml").getFile();
		String persistenceSettingsXml = FileUtils.readFileToString(new File(file), "utf-8");
		KeyValuePersistenceSettings persistenceSettings = new KeyValuePersistenceSettings(persistenceSettingsXml);
		
		
		//Define Cassandra cache store factory, set the cassandra dataSource and persistence settings
		CassandraCacheStoreFactory<Integer, Conference> cassandraCacheStoreFactory = new CassandraCacheStoreFactory<Integer, Conference>();
		cassandraCacheStoreFactory.setDataSource(cassandraDataSource);
		cassandraCacheStoreFactory.setPersistenceSettings(persistenceSettings);
		
		//Define the cache configuration, set the cassandraCacheStoreFactory as cacheStoreFactory , enable write through, write behind and read through
		CacheConfiguration<Integer, Conference> configuration = new CacheConfiguration<>();
		configuration.setName("cassandra_blob");
		configuration.setCacheStoreFactory(cassandraCacheStoreFactory);
		configuration.setWriteThrough(true);
		configuration.setWriteBehindEnabled(true);
		configuration.setReadThrough(true);

		// Sets the cache configuration
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setCacheConfiguration(configuration);

		// Starting Ignite
		Ignite ignite = Ignition.start(cfg);
		System.out.println("done");


		final IgniteCache<Integer, Conference> cache = ignite.getOrCreateCache("cassandra_blob");

		// Put some data
		cache.put(3, new Conference(3, "Linux", new Date(), new Date()));

		// Get some data
		Conference gotoConf = cache.get(3);
		System.out.println("Value: " + gotoConf);

	}

}
