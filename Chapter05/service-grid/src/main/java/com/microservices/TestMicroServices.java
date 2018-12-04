package com.microservices;

import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.microservices.services.conference.ConferenceService;
import com.microservices.services.conference.model.Conference;
import com.microservices.services.registration.RegistrationService;
import com.microservices.services.registration.model.Registration;
import com.microservices.services.registration.model.RegistrationType;

public class TestMicroServices {

	public static void main(String[] args) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setClientMode(true);

		try (Ignite ignite = Ignition.start(cfg)) {

			ConferenceService conferenceSvc = ignite.services().serviceProxy(ConferenceService.SERVICE_NAME,
					ConferenceService.class, false);

			Long confId = conferenceSvc.create(new Conference("Microservices with Ignite", 5, new Date(), new Date()));
			System.out.println(String.format("The conf id is %s", confId));

			RegistrationService registrationSvc = ignite.services().serviceProxy(RegistrationService.SERVICE_NAME,
					RegistrationService.class, false);

			String registeredId = registrationSvc
					.register(new Registration(confId, "sujoy@yopmail.com", RegistrationType.General.name()));
			System.out.println(String.format("The registration id is %s", registeredId));
		}
	}

}
