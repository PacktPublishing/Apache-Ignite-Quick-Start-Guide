package com.microservices.services.conference;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import com.microservices.services.conference.model.Conference;

public class ConferenceServiceImpl implements Service, ConferenceService {
	private static final long serialVersionUID = 1L;
	
	@IgniteInstanceResource
	private Ignite ignite;
	
	private static Long id;
	
	 private IgniteCache<Long, Conference> conferenceCache;

	@Override
	public Long create(Conference conference) {
		conference.setId(id++);
		conferenceCache.put(conference.getId(), conference);
		return conference.getId();
	}

	@Override
	public boolean delete(Conference conference) {
		return conferenceCache.remove(conference.getId());
	}

	@Override
	public Conference find(Long id) {
		return conferenceCache.get(id);
	}

	@Override
	public void cancel(ServiceContext ctx) {
		conferenceCache.clear();
		System.out.println("in cancel method. On node:" + ignite.cluster().localNode());

	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
		conferenceCache = ignite.getOrCreateCache(ConferenceService.CACHE_NAME);
		System.out.println("initializing Conference Service");

	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		id = 1l;
		System.out.println("In execute method. On node: " + ignite.cluster().localNode());

	}

	@Override
	public boolean update(Long confId, Conference conference) {
		conferenceCache.put(confId, conference);
		return true;
	}

}
