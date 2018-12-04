package com.deploy.monitoring;

import java.util.Collection;

import org.apache.ignite.DataRegionMetrics;
import org.apache.ignite.DataStorageMetrics;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class MemoryMonitoring {
	private static final int KB = 1024;
	private static final int MB = 1024 * 1024;
	private static final long GB = MB * 1024;

	public static void main(String[] args) {


		DataRegionConfiguration region1 = new DataRegionConfiguration();
		region1.setName("region1");
		region1.setMetricsEnabled(true);
		region1.setPersistenceEnabled(true);
		region1.setInitialSize(500 * MB);
		region1.setMaxSize(500 * MB);

		DataRegionConfiguration region2 = new DataRegionConfiguration();
		region2.setName("region2");
		region2.setPersistenceEnabled(true);
		region2.setMetricsEnabled(true);

		DataStorageConfiguration storageCfg = new DataStorageConfiguration();
		// set max size 6GB.
		storageCfg.getDefaultDataRegionConfiguration().setMaxSize(6L * GB);
		storageCfg.setMetricsEnabled(true);
		storageCfg.setPageSize(2 * KB);
		storageCfg.setDataRegionConfigurations(region1, region2);
		/*storageCfg.setSystemRegionInitialSize(50 * MB);
		storageCfg.setSystemRegionMaxSize(100 * MB);*/
		
		// Starting the node.
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setDataStorageConfiguration(storageCfg);
		Ignite ignite = Ignition.start(cfg);

		System.out.println("Data Region Metrics...");
		Collection<DataRegionMetrics> dataRegionMetrics = ignite.dataRegionMetrics();
		dataRegionMetrics.forEach(m -> {
			System.out.println(">>>*********************************<<<<");
			System.out.println(">>> Memory Region Name: " + m.getName());
			System.out.println(">>> OffHeapSize Size: " + m.getOffHeapSize());
			System.out.println(">>> Physical Memory Size: " + m.getPhysicalMemorySize());
			System.out.println(">>>*********************************<<<<");
		});
		
		System.out.println("Data Storage Metrics...");
		DataStorageMetrics dataStorageMetrics = ignite.dataStorageMetrics();
		System.out.println("_____________________________");
		System.out.println("Off-heap size = "+ dataStorageMetrics.getOffHeapSize());
		System.out.println("Allocated size= "+ dataStorageMetrics.getTotalAllocatedSize());
	}

}
