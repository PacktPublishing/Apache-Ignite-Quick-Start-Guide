package com.service.grid;

import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

public class MyDateServiceImpl implements Service, MyDateService {

	private static final String INIT = "init@";

	private static final long serialVersionUID = 1L;

	@IgniteInstanceResource
	private Ignite ignite;
	
	 private IgniteCache<String, Date> dateCache;

	@Override
	public Date getTime() {
		Date reply = dateCache.get(INIT);
		System.out.println("in getTime() method with response = "+reply);
		return reply;
	}

	@Override
	public void cancel(ServiceContext ctx) {
		System.out.println("in cancel method. On node:\" + ignite.cluster().localNode()");
	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
		dateCache = ignite.getOrCreateCache("myDateCache");
		dateCache.put(INIT, new Date());
		System.out.println("in init method");
	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		System.out.println("in execute method. On node: " + ignite.cluster().localNode());
	}

}
