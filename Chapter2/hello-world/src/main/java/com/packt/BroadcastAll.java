package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class BroadcastAll {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			//Get a compute task
			IgniteCompute compute = ignite.compute();
			
			//broadcast the computation to all nodes
			compute.broadcast(() -> {
				System.out.println("Broadcasting to all nodes");
			});
		}
	}
}
