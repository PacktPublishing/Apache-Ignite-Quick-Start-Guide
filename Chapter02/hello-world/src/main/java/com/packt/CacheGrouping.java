package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class CacheGrouping {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCluster cluster = ignite.cluster();

			// All nodes on which cache with name "data" is deployed,
			// either in client or server mode.
			ClusterGroup cacheGroup = cluster.forCacheNodes("data");

			// All data nodes responsible for caching data for "data".
			ClusterGroup dataGroup = cluster.forDataNodes("data");

			// All client nodes that access "data".
			ClusterGroup clientGroup = cluster.forClientNodes("data");

			IgniteCompute cacheGroupCompute = ignite.compute(cacheGroup);
			IgniteCompute dataGroupCompute = ignite.compute(dataGroup);
			IgniteCompute clientGroupCompute = ignite.compute(clientGroup);

			// broadcast the computation to cache group nodes
			cacheGroupCompute.broadcast(() -> {
				System.out.println("********\r\nCache Group Only\r\n***********");
			});

			// broadcast the computation to data group nodes
			dataGroupCompute.broadcast(() -> {
				System.out.println("*********\r\nData Group Only\r\n************");
			});

			// broadcast the computation to client group nodes
			clientGroupCompute.broadcast(() -> {
				System.out.println("*********\r\n Client Group Only\r\n*******");
			});
		}
	}
}
