package com.packt;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

public class Multicast {
	public static void main(String[] args) throws InterruptedException {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		 
		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		 
		ipFinder.setMulticastGroup("239.255.255.250");
		 
		spi.setIpFinder(ipFinder);
		 
		IgniteConfiguration cfg = new IgniteConfiguration();
		 
		// Override default discovery SPI.
		cfg.setDiscoverySpi(spi);
		cfg.setIgniteInstanceName("The Painter");
		// Start Ignite node.
		try(Ignite ignite =Ignition.start(cfg)){
			System.out.println("Node name "+ignite.name());
			Thread.sleep(9999999);
		}
	}

}
