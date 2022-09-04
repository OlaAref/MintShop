package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address extends AbstractAddress{
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(name = "default_address")
	private boolean defaultForShopping;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	public Address() {
	}

	public Address(Integer id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean isDefaultForShopping() {
		return defaultForShopping;
	}

	public void setDefaultForShopping(boolean defaultForShopping) {
		this.defaultForShopping = defaultForShopping;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		String address = firstName;
		if(lastName != null && !lastName.isEmpty()) address += " " + lastName;
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		if(!city.isEmpty()) address += ", " + city;
		if(state != null && !state.isEmpty()) address += ", " + state;
		address += ", " + country.getName();
		if(!postalCode.isEmpty()) address += ".\n Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += ".\n Phone Number: " + phoneNumber;
		
		return address;
	}

	
}