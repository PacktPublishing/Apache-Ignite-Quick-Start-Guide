package com.computegrid.closure;

import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteFuture;

public class Broadcast {

	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			// Get a compute task
			IgniteCompute compute = ignite.compute();

			// broadcast the computation to all nodes
			IgniteCallable<String> callableJob = new IgniteCallable<String>() {

				private static final long serialVersionUID = 1L;

				@Override
				public String call() throws Exception {
					System.out.println("Executing in a cluster");
					return String.valueOf(System.currentTimeMillis());
				}
			};
			
			IgniteFuture<Collection<String>> asyncFuture = compute.broadcastAsync(callableJob);

			while (!asyncFuture.isDone()) {
				System.out.println("Waiting for response");
			}

			asyncFuture.get().forEach(result -> {
				System.out.println(result);
			});
		}
	}

}
