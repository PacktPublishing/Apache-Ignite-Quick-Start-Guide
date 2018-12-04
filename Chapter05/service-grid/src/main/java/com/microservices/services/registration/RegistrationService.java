package com.microservices.services.registration;

import com.microservices.services.registration.model.Registration;

public interface RegistrationService {
	public static final String SERVICE_NAME = "RegistrationService";
	public static final String CACHE_NAME = "registrationCache";
	
	public String register(Registration registration);

}
