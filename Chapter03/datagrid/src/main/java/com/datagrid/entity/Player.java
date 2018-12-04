package com.datagrid.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "player")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Player {

	@Id
	@Column
	int playerno;

	@Column(name = "pname")
	String name;

	@Column
	int wages;

	@ManyToOne(optional=false)
    @JoinColumn(name="clubno",referencedColumnName="clubno")
	Club club;

	public int getPlayerno() {
		return playerno;
	}

	public void setPlayerno(int playerno) {
		this.playerno = playerno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWages() {
		return wages;
	}

	public void setWages(int wages) {
		this.wages = wages;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

}
