package com.datastructure;

import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class IdGeneratorTest {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		cfg.setPeerClassLoadingEnabled(true);
		Ignite ignite = Ignition.start(cfg);

		IgniteAtomicSequence seq = ignite.atomicSequence("mySeq", 0, true);
		IgniteSemaphore semaphore = ignite.semaphore("mySync", 7, true, true);

		ExecutorService executorService = ignite.executorService();
		IntStream.range(1, 8).forEach(i -> executorService.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(String.format("Thread Index %s - Sequence id is %s", i, seq.incrementAndGet()));
				semaphore.release();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}));

		semaphore.acquire(7);
		System.out.println("Processing done");

	}

}
