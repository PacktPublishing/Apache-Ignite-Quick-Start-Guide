package com.microservices.services.registration.model;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Registration implements Serializable {
	private static final long serialVersionUID = 1L;

	@QuerySqlField(index = true)
	private String id;

	@QuerySqlField(index = true)
	private final Long conferenceId;

	@QuerySqlField
	private final String email;

	@QuerySqlField
	private final String type;

	public Registration(Long conferenceId, String email, String type) {
		super();
		this.conferenceId = conferenceId;
		this.email = email;
		this.type = type;
	}

	
	public void setId(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}

	public Long getConferenceId() {
		return conferenceId;
	}

	public String getEmail() {
		return email;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Registration [id=" + id + ", conferenceId=" + conferenceId + ", email=" + email + ", type=" + type
				+ "]";
	}

}
