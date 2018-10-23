package com.computegrid.query.dto;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class SoccerClub implements Serializable {
	private static final long serialVersionUID = 1L;
	@QuerySqlField(index = true)
	private final Long id;

	@QuerySqlField
	private final String name;

	public SoccerClub(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Club [id=" + id + ", name=" + name + "]";
	}

}
