package com.packt;

import java.util.Arrays;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

public class StaticIp {
	public static void main(String[] args) throws InterruptedException {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		 
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		 
		// Set initial IP addresses.
		// Note that you can optionally specify a port or a port range.
		ipFinder.setAddresses(Arrays.asList( "127.0.0.1:47500..47509"));
		 
		spi.setIpFinder(ipFinder);
		 
		IgniteConfiguration cfg = new IgniteConfiguration();
		 
		// Override default discovery SPI.
		cfg.setDiscoverySpi(spi);
		 
		cfg.setIgniteInstanceName("The Painter");
		// Start Ignite node.
		try(Ignite ignite =Ignition.start()){
			System.out.println("Node name "+ignite.name());
			Thread.sleep(9999999);
		}
	}

}
