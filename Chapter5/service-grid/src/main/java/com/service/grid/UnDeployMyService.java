package com.service.grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class UnDeployMyService {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			System.out.println("Now undeploy the service");
			ClusterGroup cacheGrp = ignite.cluster().forServers();
			IgniteServices svcs = ignite.services(cacheGrp);
			svcs.cancel("myDateService");
			System.out.println("done");
		}

	}

}
