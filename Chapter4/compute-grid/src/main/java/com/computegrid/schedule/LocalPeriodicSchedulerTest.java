package com.computegrid.schedule;

import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class LocalPeriodicSchedulerTest {

	
	public static void main(String[] args) throws Exception {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (final Ignite ignite = Ignition.start(cfg)) {
			   ignite.scheduler().scheduleLocal(new Runnable() {
		            @Override public void run() {
		                System.out.println(String.format("Executing at %s", new Date()));
		            }
		        }, "* * * * *");
			   
			   System.in.read();
		}
	}

}

