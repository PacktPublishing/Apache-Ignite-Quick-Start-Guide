package com.datastructure;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class SemaphoreTest {

	private static final int MAX_RUN = 5;
	private static final String MY_SEMAPHORE = "mySemaphore";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		cfg.setPeerClassLoadingEnabled(true);
		Ignite ignite = Ignition.start(cfg);

		final IgniteSemaphore semaphore = ignite.semaphore(MY_SEMAPHORE, 1, true, true);
		System.out.println("available permits = " + semaphore.availablePermits());
		ExecutorService executorService = ignite.executorService();
		executorService.submit(new Consumer(MY_SEMAPHORE, MAX_RUN));
		executorService.submit(new Producer(MY_SEMAPHORE, MAX_RUN));

	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

}

class Producer implements Runnable {
	private IgniteSemaphore semaphore;
	private int maxRun = 0;

	Producer(String name, int max) {
		semaphore = Ignition.ignite().semaphore(name, 0, true, false);
		this.maxRun = max;
	}

	@Override
	public void run() {
		int run = 0;
		while (true) {
			if (run != 0) {
				System.out.println("Waiting to produce ");
			}
			semaphore.acquire();
			run++;
			System.out.println("Produced  "+run+" at "+ SemaphoreTest.getCurrentTimeStamp());
			if (run >= maxRun) {
				System.out.println("Stopping production after " + run + " runs");
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

class Consumer implements Runnable {
	private IgniteSemaphore semaphore;
	private int maxRun = 0;

	Consumer(String name, int max) {
		semaphore = Ignition.ignite().semaphore(name, 0, true, false);
		this.maxRun = max;
	}

	@Override
	public void run() {
		int run = 0;
		while (true) {
			System.out.println("Waiting to consume");
			semaphore.release();
			run++;
			System.out.println("Consumed "+run+" at " + SemaphoreTest.getCurrentTimeStamp());
			if (run >= maxRun) {
				System.out.println("Stopping consumption after " + run + " runs");
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}