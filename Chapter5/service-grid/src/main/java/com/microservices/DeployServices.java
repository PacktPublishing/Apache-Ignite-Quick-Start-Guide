package com.microservices;

import java.util.HashMap;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.services.ServiceConfiguration;

import com.microservices.config.NodeAttributeFilter;
import com.microservices.services.conference.ConferenceService;
import com.microservices.services.conference.ConferenceServiceImpl;
import com.microservices.services.registration.RegistrationService;
import com.microservices.services.registration.RegistrationServiceImpl;

public class DeployServices {

	private static final String REGISTRATION_NODE = "registration.node";
	private static final String CONFERENCE_NODE = "conference.node";

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		HashMap<String, Object> userAttrs = new HashMap<>();
		userAttrs.put(CONFERENCE_NODE, true);
		userAttrs.put(REGISTRATION_NODE, true);
		cfg.setUserAttributes(userAttrs);
		
		ServiceConfiguration confServiceConfig = new ServiceConfiguration();
		confServiceConfig.setCacheName(ConferenceService.CACHE_NAME);
		confServiceConfig.setService(new ConferenceServiceImpl());
		confServiceConfig.setName(ConferenceService.SERVICE_NAME);
		confServiceConfig.setNodeFilter(new NodeAttributeFilter(CONFERENCE_NODE));
		confServiceConfig.setMaxPerNodeCount(1);
		confServiceConfig.setTotalCount(3);

		ServiceConfiguration regisServiceConfig = new ServiceConfiguration();
		regisServiceConfig.setCacheName(RegistrationService.CACHE_NAME);
		regisServiceConfig.setService(new RegistrationServiceImpl());
		regisServiceConfig.setName(RegistrationService.SERVICE_NAME);
		regisServiceConfig.setNodeFilter(new NodeAttributeFilter(REGISTRATION_NODE));
		regisServiceConfig.setMaxPerNodeCount(1);
		regisServiceConfig.setTotalCount(3);
		cfg.setServiceConfiguration(confServiceConfig, regisServiceConfig);
		
		Ignition.start(cfg);

	}

}
