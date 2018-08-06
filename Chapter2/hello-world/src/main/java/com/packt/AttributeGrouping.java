package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

public class AttributeGrouping {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCluster cluster = ignite.cluster();

			ClusterGroup fooBarGroup = cluster.forAttribute("FOO", "BAR");

			IgniteCompute fooBarGroupCompute = ignite.compute(fooBarGroup);

			// broadcast the computation to fooBar nodes
			fooBarGroupCompute.broadcast(() -> {
				System.out.println("********\r\nFOO BAR group\r\n***********");
			});

		}
	}
}
