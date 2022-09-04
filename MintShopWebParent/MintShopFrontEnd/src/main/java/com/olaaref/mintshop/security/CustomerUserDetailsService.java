package com.olaaref.mintshop.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.repository.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		
		if(customer == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		return new CustomerUserDetails(customer);
	}

}
