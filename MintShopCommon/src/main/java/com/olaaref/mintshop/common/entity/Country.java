package com.olaaref.mintshop.common.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 45)
	private String name;
	
	@Column(name = "code", nullable = false, length = 5)
	private String code;
	
	@Column(name = "iso3", length = 3)
	private String iso3;
	
	@Column(name = "emoji")
	private String emoji;
	
	//one-to-many states
	@OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<State> states;
	
	

	public Country() {
		super();
	}

	public Country(Integer id) {
		super();
		this.id = id;
	}

	public Country(String name, String code, String emoji) {
		super();
		this.name = name;
		this.code = code;
		this.emoji = emoji;
	}

	public Country(Integer id, String name, String code, String emoji) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.emoji = emoji;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + ", emoji=" + emoji + "]";
	}
	
	
}
