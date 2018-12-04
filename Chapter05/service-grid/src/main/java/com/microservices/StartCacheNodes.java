package com.microservices;

import java.util.HashMap;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.microservices.config.NodeAttributeFilter;
import com.microservices.services.conference.ConferenceService;
import com.microservices.services.conference.model.Conference;
import com.microservices.services.registration.RegistrationService;
import com.microservices.services.registration.model.Registration;

public class StartCacheNodes {

	private static final String CACHE_NODE = "cache.node";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		HashMap<String, Object> userAttrs = new HashMap<>();
		userAttrs.put(CACHE_NODE, true);
		cfg.setUserAttributes(userAttrs);

		CacheConfiguration<Long, Conference> conferenceConfig = new CacheConfiguration<Long, Conference>();
		conferenceConfig.setTypes(Long.class, Conference.class);
		conferenceConfig.setName(ConferenceService.CACHE_NAME);
		conferenceConfig.setNodeFilter(new NodeAttributeFilter(CACHE_NODE));
		
		CacheConfiguration<String, Registration> registrationConfig = new CacheConfiguration<>();
		registrationConfig.setName(RegistrationService.CACHE_NAME);
		registrationConfig.setNodeFilter(new NodeAttributeFilter(CACHE_NODE));
		cfg.setCacheConfiguration(conferenceConfig, registrationConfig);
		
		Ignition.start(cfg);

	}

}
