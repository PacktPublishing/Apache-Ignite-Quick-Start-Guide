package com.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.datagrid.entity.Club;
import com.datagrid.entity.Player;

@SpringBootApplication
public class DatagridApplication {

	public static void main(String[] args) {


		try {
			IgniteConfiguration cfg = new IgniteConfiguration();
			cfg.setIgniteInstanceName("MyGrid121");

			Ignite ignite = Ignition.start(cfg);
			
			ignite.getOrCreateCache("org.hibernate.cache.spi.UpdateTimestampsCache");
			ignite.getOrCreateCache("org.hibernate.cache.internal.StandardQueryCache");

			CacheConfiguration<Integer, Club> clubConfig = new CacheConfiguration<>();
			clubConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
			clubConfig.setIndexedTypes(Integer.class, Club.class);
			clubConfig.setName("com.datagrid.entity.Club");
			ignite.getOrCreateCache(clubConfig);

			CacheConfiguration<Integer, Player> playerConfig = new CacheConfiguration<>();
			playerConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
			playerConfig.setIndexedTypes(Integer.class, Player.class);
			playerConfig.setName("com.datagrid.entity.Player");
			ignite.getOrCreateCache(playerConfig);

		} catch (Exception e) {
			
		}
		
		SpringApplication.run(DatagridApplication.class, args);

	}
}
