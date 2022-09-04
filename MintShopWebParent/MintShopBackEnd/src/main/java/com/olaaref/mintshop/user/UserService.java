package com.olaaref.mintshop.user;


import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Role;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exception.UserNotFoundException;

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
	
	public Page<User> listAllUsersByPage(int pageNumber, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNumber-1, USERS_PER_PAGE, sort);
		
		if(keyword != null || keyword == "") {
			return userRepository.search(keyword, pageable);
		}
		
		return userRepository.findAll(pageable);
	}
	
	public void saveUser(User user) {
		
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
		
		userRepository.save(user);
		
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



















