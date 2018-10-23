package com.computegrid.closure;

import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteRunnable;

public class IgniteRunnableAndCallable {

	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			
			IgniteCompute compute = ignite.compute();

			compute.run(new AdderRunnable(1, 2));
			
			
			IgniteFuture<Void> runAsync = compute.runAsync(new AdderRunnable(1, 2));
			while(!runAsync.isDone()) {
				System.out.println("Waiting for the job completion");
			}
			
			System.out.println("Job done");

			Integer result = compute.call(new AdderCallable(1, 2));
			
			System.out.println("Callable Job done with result "+result);
			
			IgniteFuture<Integer> callAsync = compute.callAsync(new AdderCallable(1, 2));
			
			while(!callAsync.isDone()) {
				System.out.println("Waiting for the callable job completion");
			}
			
			System.out.println("Callable Job done with result "+callAsync.get());
		}
	}

}

class AdderRunnable implements IgniteRunnable {
	private static final long serialVersionUID = 1L;
	private final int first;
	private final int second;

	public AdderRunnable(int first, int second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public void run() {
		System.out.println(String.format("In IgniteRunnable Adder Adding %s and %s the result is = %s ", first, second,
				(first + second)));

	}

}

class AdderCallable implements IgniteCallable<Integer> {
	private static final long serialVersionUID = 1L;
	private final int first;
	private final int second;

	public AdderCallable(int first, int second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public Integer call() throws Exception {
		int result = first + second;
		System.out.println(
				String.format("In IgniteCallable Adder Adding %s and %s the result is = %s ", first, second, result));

		return result;
	}

}
