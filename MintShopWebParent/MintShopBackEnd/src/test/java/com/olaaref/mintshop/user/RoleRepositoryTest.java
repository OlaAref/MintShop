package com.olaaref.mintshop.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Test
	public void testCreateRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		Role saveRole = roleRepository.save(roleAdmin);
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSales = new Role("Sales", "Manage product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
		Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
		Role roleAssistant = new Role("Assistant", "Manage questions and reviews");
		
		roleRepository.saveAll(List.of(roleSales, roleEditor, roleShipper, roleAssistant));
		
	} 

}
