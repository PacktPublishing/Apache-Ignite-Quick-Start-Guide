package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ClientMode {

	public static void main(String[] args) throws InterruptedException {
		 
		IgniteConfiguration cfg = new IgniteConfiguration();
		//Set the client mode true
		cfg.setClientMode(true);
		
		try(Ignite ignite = Ignition.start(cfg)){
			System.out.println("Node name "+ignite.name());
		}
	}
}
