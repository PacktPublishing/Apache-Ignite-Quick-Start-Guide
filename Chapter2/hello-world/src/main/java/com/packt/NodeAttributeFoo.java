package com.packt;

import java.util.HashMap;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class NodeAttributeFoo {
	public static void main(String[] args) throws InterruptedException {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		HashMap<String, Object> userAttrb = new HashMap<>();
		userAttrb.put("FOO", "BAR");
		cfg.setUserAttributes(userAttrb);
		try (Ignite ignite = Ignition.start(cfg)) {
	       while(true) {
			Thread.sleep(1000);
	       }
		}
	}
}
