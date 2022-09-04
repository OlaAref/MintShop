package com.olaaref.mintshop.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.repository.ShippingRateRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRateRepositoryTest {

	@Autowired
	private ShippingRateRepository shippingRateRepository;
	
	@Test
	public void testFindByCountryAndState() {
		Country country = new Country(65);
		String state = "Dakahlia Governorate";
		ShippingRate rate = shippingRateRepository.findByCountryAndState(country, state);
		assertThat(rate).isNotNull();
		System.out.println(rate);
	}
}
