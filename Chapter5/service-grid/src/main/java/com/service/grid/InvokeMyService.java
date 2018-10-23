package com.service.grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class InvokeMyService {

	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			MyDateService myDateService = ignite.services().serviceProxy("myDateService", MyDateService.class, false);
			System.out.println(myDateService.getTime());
			System.out.println("Now undeploy the service");
			ClusterGroup cacheGrp = ignite.cluster().forServers();
			IgniteServices svcs = ignite.services(cacheGrp);
			svcs.cancel("myDateService");
		}
	}

}
