package com.computegrid.mapreduce.priority;

import java.util.Arrays;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.collision.priorityqueue.PriorityQueueCollisionSpi;

public class RandomPriority {
	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		PriorityQueueCollisionSpi colSpi = new PriorityQueueCollisionSpi();
		colSpi.setParallelJobsNumber(10);
		cfg.setCollisionSpi(colSpi);

		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCompute compute = ignite.compute();
			List<String> response = compute.execute(MyTaskAdapter.class, Arrays.asList("bar1", "bar2","bar3", "bar4", "bar5"));
			List<String> response2 = compute.execute(MyTaskAdapter.class, Arrays.asList("car1","car2"));
			System.out.println(response);
		}
	}
}
