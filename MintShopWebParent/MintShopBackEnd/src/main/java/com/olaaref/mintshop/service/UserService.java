package com.olaaref.mintshop.service;


import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Role;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exception.UserNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.user.RoleRepository;
import com.olaaref.mintshop.user.UserRepositoy;

@Service
@Transactional
public class UserService {
	
	public static final int USERS_PER_PAGE = 4;
	
	@Autowired
	private UserRepositoy userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return userRepository.findAll(Sort.by("firstName").ascending());
	}
	
	public List<Role> listAllRoles(){
		return roleRepository.findAll();
	}
	
	public void listAllUsersByPage(int pageNumber, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNumber, USERS_PER_PAGE, userRepository);
		
	}
	
	public User saveUser(User user) {
		
		if(user.getId() != null) {
			//update case
			//do not change pass
			User existingUser = userRepository.getById(user.getId());
			user.setPassword(existingUser.getPassword());
		}
		else {
			//add case
			//encode pass
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		User savedUser = userRepository.save(user);
		return savedUser;
		
	}
	
	public User updateAccount(User formUser) {
		
		User dbUser = userRepository.getById(formUser.getId());
		
		if(formUser.getImage() != null) {
			dbUser.setImage(formUser.getImage());
		}
		
		dbUser.setFirstName(formUser.getFirstName());
		dbUser.setLastName(formUser.getLastName());
		
		return userRepository.save(dbUser);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		
		User userByEmail = userRepository.findByEmail(email);
		if(userByEmail == null) return true;
		
		if(id == null) {
			if(userByEmail != null) return false;
		}
		else {
			if(userByEmail.getId() != id) return false;
		}
		
		return true;
	}

	public User getById(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with id : " + id);
		}
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void deleteUser(Integer id) throws UserNotFoundException {
		
		Long countById = userRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("No user found with id : "+id);
		}

		userRepository.deleteById(id);
	}
	
	public void updateEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}

}



















