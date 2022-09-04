package com.olaaref.mintshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.user.UserRepositoy;

@Service
public class MintshopUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepositoy userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(userEmail);
		
		if(user == null) {
			throw new UsernameNotFoundException("Invalid email or password");
		}
		
		return new MintshopUserDetails(user);
	}

}
