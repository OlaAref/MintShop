package com.olaaref.mintshop.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.dao.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer() {
		Integer countryId = 101; //India
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setEmail("cus2@mail.mail");
		customer.setFirstName("Customer2");
		customer.setLastName("Test2");
		customer.setPassword("123");
		customer.setPhoneNumber("322-858-5571");
		customer.setCountry(country);
		customer.setState("Mumbai");
		customer.setCity("Maharashtra");
		customer.setAddressLine1("Lower	Parel (West)");
		customer.setPostalCode("400017");
		
		Customer savedCustomer = customerRepository.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertThat(customers).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		Customer customer = customerRepository.findById(1).get();
		customer.setLastName("Test1");
		Customer updatedCustomer = customerRepository.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo("Test1");
	}
	
	@Test
	public void testGetCustomer() {
		Optional<Customer> optionalCustomer = customerRepository.findById(1);
		assertThat(optionalCustomer).isPresent();
		System.out.println(optionalCustomer.get());
	}
	
	@Test
	public void testDeleteCustomer() {
		customerRepository.deleteById(2);
		Optional<Customer> deletedCustomer = customerRepository.findById(2);
		assertThat(deletedCustomer).isNotPresent();
	}
	
	@Test
	public void testfindByEmail() {
		String email = "cus@mail.mail";
		Customer customer = customerRepository.findByEmail(email);
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testfindByVerificationCode() {
		String code = "code_123";
		//Customer customer = customerRepository.findByVerificationCode(code);
		//assertThat(customer).isNotNull();
		//System.out.println(customer);
	}
	
	@Test
	public void testUpdateEnabledStatus() {
		customerRepository.updateEnabledStatus(1, false);
		Customer customer = customerRepository.findById(1).get();
		assertThat(customer.isEnabled()).isTrue();
	}
}
