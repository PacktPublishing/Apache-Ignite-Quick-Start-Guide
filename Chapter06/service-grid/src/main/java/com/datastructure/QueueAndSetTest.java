package com.datastructure;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCountDownLatch;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class QueueAndSetTest {

	private static final String QUEUE_NAME = "myQueue";
	private static final String SET_NAME = "mySet";
	private static final String LATCH_NAME = "syncLatch";

	public static void main(String[] args) throws InterruptedException {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		cfg.setPeerClassLoadingEnabled(true);
		Ignite ignite = Ignition.start(cfg);

		CollectionConfiguration collectionConfig = new CollectionConfiguration();
		collectionConfig.setCollocated(false);
		
		IgniteQueue<Integer> distributedQueue =ignite.queue(QUEUE_NAME, 0, collectionConfig);
		IgniteSet<Integer> distributedSet = ignite.set(SET_NAME, collectionConfig);
		IgniteCountDownLatch latch = ignite.countDownLatch(LATCH_NAME, 3, false, true);

		ExecutorService executorService = ignite.executorService();
		executorService.submit(new Printer(LATCH_NAME, SET_NAME, QUEUE_NAME));
		executorService.submit(new Publisher(LATCH_NAME, SET_NAME, QUEUE_NAME, 1,2,3));
		executorService.submit(new Publisher(LATCH_NAME, SET_NAME, QUEUE_NAME, 100,2,55));
		executorService.submit(new Publisher(LATCH_NAME, SET_NAME, QUEUE_NAME, 1000, 5000, 55, 3));
		
		executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
		System.out.println("Processing done");

	}
	
	

}
class Publisher implements Runnable{
	private final List<Integer> elements;
	private final IgniteSet<Integer> distributedSet;
	private final IgniteQueue<Integer> distributedQueue;
	private final IgniteCountDownLatch latch;
	
	Publisher(String latchName, String setName, String queueName, Integer...elements ){
		this.elements = Arrays.asList(elements);
		this.distributedSet= Ignition.ignite().set(setName, null);
		this.distributedQueue = Ignition.ignite().queue(queueName, 0, null);
		this.latch = Ignition.ignite().countDownLatch(latchName, 0, false, false);
	}
	@Override
	public void run() {
		elements.forEach(i->{
			distributedSet.add(i);
			distributedQueue.add(i);
			System.out.println(String.format("Adding %s", i));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		latch.countDown();
		
	}
	
}

class Printer implements Runnable{
	private final IgniteSet<Integer> distributedSet;
	private final IgniteQueue<Integer> distributedQueue;
	private final IgniteCountDownLatch latch;
	
	Printer(String latchName, String setName, String queueName ){
		this.distributedSet= Ignition.ignite().set(setName, null);
		this.distributedQueue = Ignition.ignite().queue(queueName, 0, null);
		this.latch = Ignition.ignite().countDownLatch(latchName, 0, false, false);
	}
	@Override
	public void run() {
		System.out.println("Wait for other threads to finish");
		latch.await();
		
		System.out.println("Start printing");
		for(Integer i : distributedSet) {
			System.out.println("Object found in Set - >" + i);
		}
		
		for(Integer i : distributedQueue) {
			System.out.println("Object found in Queue - >" + i);
		}
		
	}
	
}