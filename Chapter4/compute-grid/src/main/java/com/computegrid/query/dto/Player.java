package com.computegrid.query.dto;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QueryTextField;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	@QueryTextField()
	private String name;
	@QueryTextField
	private String team;
	private double salary;

	public Player(Long id, String name, String team, double salary) {
		this.id = id;
		this.name = name;
		this.team = team;
		this.salary = salary;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", team=" + team + ", salary=" + salary + "]";
	}

}
