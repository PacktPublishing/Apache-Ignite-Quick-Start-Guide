package com.microservices.services.registration;

import java.util.UUID;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.ServiceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import com.microservices.services.conference.ConferenceService;
import com.microservices.services.conference.model.Conference;
import com.microservices.services.registration.model.Registration;

public class RegistrationServiceImpl implements Service, RegistrationService {
	private static final long serialVersionUID = 1L;

	@IgniteInstanceResource
	private Ignite ignite;

	@ServiceResource(serviceName = ConferenceService.SERVICE_NAME)
	private ConferenceService conferenceService;

	private IgniteCache<String, Registration> registrationCache;

	@Override
	public void cancel(ServiceContext ctx) {
		registrationCache.clear();
		System.out.println("in cancel method. On node:" + ignite.cluster().localNode());
	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
		registrationCache = ignite.getOrCreateCache(RegistrationService.CACHE_NAME);
		System.out.println("initializing Registration Service");

	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		System.out.println("In execute method. On node: " + ignite.cluster().localNode());
	}

	@Override
	public String register(Registration registration) {
		Conference conference = conferenceService.find(registration.getConferenceId());
		if (conference == null) {
			throw new RuntimeException(
					String.format("No Conference found for ID = %s", registration.getConferenceId()));
		}

		if (conference.getNoOfRegistration() + 1 >= conference.getCapacity()) {
			throw new RuntimeException(String.format("Capacity exceeded for conf = %s, total capacity = %s ",
					registration.getConferenceId(), conference.getCapacity()));
		}

		String registrationId = UUID.randomUUID().toString();
		registration.setId(registrationId);

		registrationCache.put(registrationId, registration);

		conference.setNoOfRegistration(conference.getNoOfRegistration() + 1);
		conferenceService.update(conference.getId(), conference);

		return registrationId;
	}

}
