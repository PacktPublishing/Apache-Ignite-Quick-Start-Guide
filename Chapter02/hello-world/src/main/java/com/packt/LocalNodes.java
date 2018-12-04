package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class LocalNodes {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCluster cluster = ignite.cluster();
			
			//Get local server group
			ClusterGroup forLocal = cluster.forLocal();
			
			//Get a compute task for local servers
			IgniteCompute compute = ignite.compute(forLocal);
			
			//broadcast the computation to local nodes
			compute.broadcast(() -> {
				System.out.println("*******************\r\nLocal Only\r\n*************************");
			});
		}
	}
}
