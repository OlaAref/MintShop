package com.olaaref.mintshop.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Role;
import com.olaaref.mintshop.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepositoy userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("div@mail.com", "123", "David", "Joe");
		user.addRole(roleAdmin);
		
		User saveUser = userRepository.save(user);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		
		User user = new User("sam@mail.com", "123", "Sami", "Adel");
		
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
		
		User saveUser = userRepository.save(user);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUIsers() {
		
		Iterable<User> users = userRepository.findAll();
		users.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		
		User users = userRepository.findById(2).get();
		System.out.println(users);
		assertThat(users).isNotNull();
	}
	
	@Test
	public void testUpdateUser() {
		
		User user = userRepository.findById(2).get();
		System.out.println(user);
		user.setEmail("sam1@mail.com");
		user.setEnabled(true);
		userRepository.save(user);
		System.out.println(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		
		User user = userRepository.findById(2).get();
		//remove role Editor
		user.getRoles().remove(new Role(3));
		//add role SalsePerson
		user.addRole(new Role(2));
		
		userRepository.save(user);
		System.out.println(user);
	}
	
	@Test
	public void testDeleteUser() {
		
		userRepository.deleteById(2);
	}
	
	@Test
	public void testFindUserByEmail() {
		User user = userRepository.findByEmail("noraa@mail.com");
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateEnabled() {
		userRepository.updateEnabledStatus(1, true);
	}
	
	@Test
	public void testPageUsersList() {
		int pageSize = 4;
		int pageNumber = 1;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(pageable);
		
		List<User> userList = page.getContent();
		
		userList.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testSearchUser() {
		int pageSize = 4;
		int pageNumber = 0;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll("yon", pageable);
		
		List<User> userList = page.getContent();
		
		userList.forEach(user -> System.out.println(user));
		
	}
	

}
