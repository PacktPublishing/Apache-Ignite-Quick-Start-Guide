package com.microservices.services.conference;

import com.microservices.services.conference.model.Conference;

public interface ConferenceService {
	
	public static final String SERVICE_NAME = "ConferenceService";
	public static final String CACHE_NAME = "conferenceCache";

	public Long create(Conference conference);
	public boolean update(Long confId, Conference conference);
	public boolean delete(Conference conference);
	public Conference find(Long id);
}
