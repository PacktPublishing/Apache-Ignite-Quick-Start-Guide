package com.datagrid.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "club")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Club {

	@Id
	@Column
	int clubno;

	@Column(name = "cname")
	String name;

	public int getClubno() {
		return clubno;
	}

	public void setClubno(int clubno) {
		this.clubno = clubno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
