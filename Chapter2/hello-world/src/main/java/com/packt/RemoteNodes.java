package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class RemoteNodes {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCluster cluster = ignite.cluster();
			
			//Get remote server group
			ClusterGroup forRemotes = cluster.forRemotes();
			
			//Get a compute task for remote servers
			IgniteCompute compute = ignite.compute(forRemotes);
			
			//broadcast the computation to all nodes
			compute.broadcast(() -> {
				System.out.println("*******************\r\nOnly remote nodes\r\n*************************");
			});
		}
	}
}
