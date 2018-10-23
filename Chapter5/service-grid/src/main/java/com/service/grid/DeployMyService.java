package com.service.grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class DeployMyService {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			// Cluster group which includes all caching nodes.
			ClusterGroup cacheGrp = ignite.cluster().forServers();

			// Get an instance of IgniteServices for the cluster group.
			IgniteServices svcs = ignite.services(cacheGrp);
			 
			// Deploy per-node singleton. An instance of the service
			// will be deployed on every node within the cluster group.
			svcs.deployNodeSingleton("myDateService", new MyDateServiceImpl());
		}

	}

}
