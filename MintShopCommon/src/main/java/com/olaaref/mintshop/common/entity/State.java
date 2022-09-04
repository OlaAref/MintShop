package com.olaaref.mintshop.common.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "states")
public class State extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 45)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<City> cities;

	public State() {
		super();
	}

	public State(int id) {
		super();
		this.id = id;
	}

	public State(String name, Country country) {
		super();
		this.name = name;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}
	
	public void addCity(City city) {
		if(city != null) {
			this.cities.add(city);
		}
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", country=" + country.getName() + "]";
	}
	
	
	
	
}
