package com.olaaref.mintshop.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {
	
	@Id
	@Column(name = "series")
	private String series;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "last_used")
	private Date lastUsed;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	@Override
	public String toString() {
		return "PersistentLogins [series=" + series + ", username=" + username + ", token=" + token + ", lastUsed="
				+ lastUsed + "]";
	}
	
	
}
