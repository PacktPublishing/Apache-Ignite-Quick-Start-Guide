package com.computegrid.schedule;

import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

public class BroadcastScheduleTest {

	
	public static void main(String[] args) throws Exception {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		try (final Ignite ignite = Ignition.start(cfg)) {
			   
			ignite.compute().broadcast(new IgniteCallable<Object>() {
			    
				private static final long serialVersionUID = 1L;
				@IgniteInstanceResource
			    Ignite ignite;

			    @Override
			    public Object call() throws Exception {
			        ignite.scheduler().scheduleLocal(new Runnable() {
			            @Override public void run() {
			                System.out.println(String.format("Executing at %s", new Date()));
			            }
			        }, "* * * * *");
			        return null;
			    }
			});
			
			
		}
	}

}

