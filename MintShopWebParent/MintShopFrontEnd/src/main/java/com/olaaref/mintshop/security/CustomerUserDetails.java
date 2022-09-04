package com.olaaref.mintshop.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.olaaref.mintshop.common.entity.Customer;

public class CustomerUserDetails implements UserDetails {

	private Customer customer;
	
	public CustomerUserDetails(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// customer not have any authority
		return null;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}
	
	public String getFullname() {
		return customer.getFullName();
	}

	public Customer getCustomer() {
		return this.customer;
	}
}
