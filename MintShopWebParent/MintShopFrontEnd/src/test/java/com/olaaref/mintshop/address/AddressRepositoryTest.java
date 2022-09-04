package com.olaaref.mintshop.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.repository.AddressRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {

	@Autowired
	private AddressRepository addressRepository;
	
	@Test
	public void testAddNew() {
		Address address1 = new Address();
		address1.setFirstName("Ola");
		address1.setLastName("Ahmed");
		address1.setPhoneNumber("12345678");
		address1.setAddressLine1("25 street");
		address1.setCity("Röhrenbach");
		address1.setState("Cairo Governorate");
		address1.setCountry(new Country(65));
		address1.setPostalCode("12347");
		address1.setDefaultForShopping(false);
		address1.setCustomer(new Customer(3));
		Address savedAddress = addressRepository.save(address1);
		assertThat(savedAddress).isNotNull();
	}
	
	@Test
	public void testAddNew2() {
		Address address = new Address();
		address.setFirstName("Ola");
		address.setLastName("Ahmed");
		address.setPhoneNumber("47581693");
		address.setAddressLine1("18 ca");
		address.setCity("Ḩalwān");
		address.setState("Cairo Governorate");
		address.setCountry(new Country(65));
		address.setPostalCode("54875");
		address.setDefaultForShopping(true);
		address.setCustomer(new Customer(3));
		Address savedAddress = addressRepository.save(address);
		assertThat(savedAddress).isNotNull();
	}
	
	@Test
	public void testFindByCustomer() {
		List<Address> addresses = addressRepository.findByCustomer(new Customer(3));
		addresses.forEach(System.out::println);
		assertThat(addresses.size()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByIdAndCustomer() {
		Address address = addressRepository.findByIdAndCustomer(1, 3);
		System.out.println(address);
		assertThat(address).isNotNull();
	}
	
	@Test
	public void testUpdate() {
		Address address = addressRepository.getById(1);
		address.setAddressLine1("26 street");
		Address savedAddress = addressRepository.save(address);
		assertThat(savedAddress).isNotNull();
		
	}
	
	@Test
	public void testDeleteByIdAndCustomer() {
		addressRepository.deleteByIdAndCustomer(2, 3);
	}
	
	@Test
	public void testUpdateDefaultAddress() {
		addressRepository.setDefaultAddress(1);
	}
	
	@Test
	public void testNonDefaultAddresses() {
		addressRepository.setNonDefaultForOthers(3, 3);
	}
	
	@Test
	public void testFindDefaultAddressByCustomer() {
		Address address = addressRepository.findDefaultAddressByCustomer(1);
		assertThat(address).isNotNull();
		System.out.println(address);
	}
}




























