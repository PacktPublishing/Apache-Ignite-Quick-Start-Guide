package com.computegrid.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.gridify.Gridify;
import org.apache.ignite.compute.gridify.GridifyArgument;
import org.apache.ignite.compute.gridify.GridifyTaskAdapter;
import org.apache.ignite.compute.gridify.aop.spring.GridifySpringEnhancer;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.util.gridify.GridifyJobAdapter;
import org.jetbrains.annotations.Nullable;

public class RunLocalSchedulerTest {

	
	public static void main(String[] args) throws Exception {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (Ignite ignite = Ignition.start(cfg)) {
			ignite.scheduler().runLocal(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("now executed @"+new Date());
					
				}
			}, 10, TimeUnit.MICROSECONDS);
		
			Thread.sleep(1000);
		}
	}

}

