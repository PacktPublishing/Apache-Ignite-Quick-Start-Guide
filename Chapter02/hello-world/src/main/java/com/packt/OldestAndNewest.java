package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class OldestAndNewest {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCluster cluster = ignite.cluster();
			
			//Get the oldest node
			ClusterGroup forOldest = cluster.forOldest();
			
			//Get a compute task for the oldest servers
			IgniteCompute compute = ignite.compute(forOldest);
			
			//broadcast the computation to all nodes
			compute.broadcast(() -> {
				System.out.println("*******************\r\nOld is Gold!!!\r\n*************************");
			});
			
			
			//Get the youngest node
			ClusterGroup youngest = cluster.forYoungest();
			
			//Get a compute task for the youngest node
			compute = ignite.compute(youngest);
			
			//broadcast the computation to all nodes
			compute.broadcast(() -> {
				System.out.println("*******************\r\nYoung blood!!!\r\n*************************");
			});
		}
	}
}
