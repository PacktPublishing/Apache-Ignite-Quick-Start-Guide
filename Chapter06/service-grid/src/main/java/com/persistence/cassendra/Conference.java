package com.persistence.cassendra;

import java.io.Serializable;
import java.util.Date;

public class Conference implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Date startDateTime;
	private Date endDateTime;
	public Conference() {}
	public Conference(Integer id, String name, Date startDateTime, Date endDateTime) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

