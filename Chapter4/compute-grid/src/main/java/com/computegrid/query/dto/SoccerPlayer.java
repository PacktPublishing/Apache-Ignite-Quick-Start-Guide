package com.computegrid.query.dto;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class SoccerPlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	@QuerySqlField(index=true)
	private Long id;
	@QuerySqlField
	private String name;
	@QuerySqlField
	private double salary;
	@QuerySqlField(index=true)
	private Long clubId;
	public SoccerPlayer(Long id, String name, double salary, Long clubId) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.clubId = clubId;
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

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Long getClubId() {
		return clubId;
	}

	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", salary=" + salary + ", clubId=" + clubId + "]";
	}
	
	
}
