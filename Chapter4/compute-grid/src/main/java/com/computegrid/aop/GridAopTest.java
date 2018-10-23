package com.computegrid.aop;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.gridify.Gridify;
import org.apache.ignite.compute.gridify.aop.spring.GridifySpringEnhancer;
import org.apache.ignite.configuration.IgniteConfiguration;

public class GridAopTest {

	@Gridify
	public void sayItLoud(String msg) {
		System.out.println("Hey " + msg + "?");
	}

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			GridAopTest test = new GridAopTest();
			test = GridifySpringEnhancer.enhance(test);
			test.sayItLoud("who are you!");
		}
	}

}
