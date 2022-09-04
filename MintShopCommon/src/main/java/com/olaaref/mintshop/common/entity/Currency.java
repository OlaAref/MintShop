package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 64)
	private String name;
	
	@Column(name = "symbol", nullable = false, length = 3)
	private String symbol;
	
	@Column(name = "code", nullable = false, length = 4)
	private String code;

	public Currency() {
		super();
	}

	public Currency(String name, String code, String symbol) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", name=" + name + ", symbol=" + symbol + ", code=" + code + "]";
	}
	
	
}

