package com.computegrid.closure;

import java.util.Arrays;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteClosure;

public class IgniteApply {

	@SuppressWarnings("serial")
	public static void main(String[] args) {

		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			IgniteCompute compute = ignite.compute();

			List<Integer> numersToAdd = Arrays.asList(2, 3, 5, 6, 7);
			Integer result = compute.apply(new IgniteClosure<List<Integer>, Integer>() {

				@Override
				public Integer apply(List<Integer> numbers) {
					int sum = numbers.stream().mapToInt(i -> i.intValue()).sum();
					return sum;
				}
			}, numersToAdd);
			
			System.out.println(String.format("The sum of %s is %s", numersToAdd, result));
		}
	}

}
