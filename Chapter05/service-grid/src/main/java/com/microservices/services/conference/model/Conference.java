package com.microservices.services.conference.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Conference implements Serializable {
	private static final long serialVersionUID = 1L;

	@QuerySqlField(index = true)
	private Long id;

	@QuerySqlField
	private final String name;

	@QuerySqlField
	private final long capacity;

	@QuerySqlField
	private long noOfRegistration;

	@QuerySqlField
	private final Date startDate;

	@QuerySqlField
	private final Date endDate;

	public Conference(String name, long capacity, Date startDate, Date endDate) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getNoOfRegistration() {
		return noOfRegistration;
	}

	public void setNoOfRegistration(long noOfRegistration) {
		this.noOfRegistration = noOfRegistration;
	}

	public long getCapacity() {
		return capacity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "Conference [id=" + id + ", name=" + name + ", capacity=" + capacity + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
