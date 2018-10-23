package com.computegrid.executor;

import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ExecutorServiceTest {

	
	public static void main(String[] args) throws Exception {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			ExecutorService executorService = ignite.executorService();
			IntStream.range(1, 10).forEach( i->
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(String.format("Executing using ExecutorService - Index %s", i));
					
				}
			}));
			
		}
	}

}

