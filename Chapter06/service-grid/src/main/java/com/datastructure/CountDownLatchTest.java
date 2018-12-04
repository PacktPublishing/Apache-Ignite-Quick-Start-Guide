package com.datastructure;

import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCountDownLatch;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class CountDownLatchTest {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(false);
		cfg.setPeerClassLoadingEnabled(true);
		Ignite ignite = Ignition.start(cfg);

		final IgniteCountDownLatch latch = ignite.countDownLatch("myLatch", 10, false, true);

		ExecutorService executorService = ignite.executorService();
		IntStream.range(1, 11).forEach(i -> executorService.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(String.format("Executing using ExecutorService - Index %s", i));
				int value = latch.countDown();
				System.out.println("latch value now " + value);
			}
		}));

		System.out.println("Waiting for all threads to to complete");
		latch.await();
		System.out.println("Processing completed");

	}

}
