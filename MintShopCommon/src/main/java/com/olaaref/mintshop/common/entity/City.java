package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
public class City extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 45)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	public City() {
		super();
	}

	public City(String name, State state) {
		super();
		this.name = name;
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", state=" + state.getName() + "]";
	}
	
	
}
