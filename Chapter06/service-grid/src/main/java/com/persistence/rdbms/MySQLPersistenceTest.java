package com.persistence.rdbms;

import java.io.Serializable;
import java.util.Date;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class MySQLPersistenceTest {

	public static void main(String[] args) {

		Ignite ignite = Ignition.start("cache-config.xml");
		ignite.cluster().active(true);
		IgniteCache<Long, Conference> cache = ignite.getOrCreateCache("conferenceCache");
		cache.loadCache(null);
		cache.put(3l, new Conference(3l, "GOTO;", new Date(), new Date()));
		cache.put(4l, new Conference(4l, "StrangeLoop;", new Date(), new Date()));

		System.out.println("done");

	}

}

class Conference implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Date startDateTime;
	private Date endDateTime;

	public Conference(Long id, String name, Date startDateTime, Date endDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	@Override
	public String toString() {
		return "Conference [id=" + id + ", name=" + name + ", startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

}
