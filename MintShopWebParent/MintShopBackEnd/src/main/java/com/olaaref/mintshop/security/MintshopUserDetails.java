package com.olaaref.mintshop.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.olaaref.mintshop.common.entity.Role;
import com.olaaref.mintshop.common.entity.User;

public class MintshopUserDetails implements UserDetails {
	
	private User user;
	
	public MintshopUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//get the collection roles of the user
		Collection<Role> roles = user.getRoles();
		
		//get the granted authority
		return roles
					.stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public String getFullname() {
		return user.getFullName();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}

}
