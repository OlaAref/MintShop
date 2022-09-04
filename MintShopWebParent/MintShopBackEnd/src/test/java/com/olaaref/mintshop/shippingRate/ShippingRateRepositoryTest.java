package com.olaaref.mintshop.shippingRate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.dao.ShippingRateRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTest {

	@Autowired
	private ShippingRateRepository shippingRateRepository;
	
	@Test
	public void testCreateNew() {
		Country country = new Country(233);
		ShippingRate shippingRate1 = new ShippingRate();
		shippingRate1.setRate(15.5f);
		shippingRate1.setDays(5);
		shippingRate1.setCountry(country);
		shippingRate1.setState("Hawaii");
		shippingRate1.setCodSupported(true);
		
		ShippingRate shippingRate2 = new ShippingRate();
		shippingRate2.setRate(19.7f);
		shippingRate2.setDays(7);
		shippingRate2.setCountry(country);
		shippingRate2.setState("Texas");
		shippingRate2.setCodSupported(false);
		
		ShippingRate savedRate = shippingRateRepository.save(shippingRate2);
		System.out.println(savedRate);
		assertThat(savedRate).isNotNull();
	}
	
	@Test
	public void testUpdate() {
		ShippingRate rate = shippingRateRepository.findById(1).get();
		rate.setRate(15.3f);
		ShippingRate savedRate = shippingRateRepository.save(rate);
		System.out.println(rate);
		assertThat(savedRate.getRate()).isEqualTo(15.3f);
	}
	
	@Test
	public void testDelete() {
		shippingRateRepository.deleteById(2);
	}
	
	@Test
	public void testFindAll() {
		List<ShippingRate> rates = shippingRateRepository.findAll();
		rates.forEach(System.out::println);
		assertThat(rates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCountryAndState() {
		int countryId = 233;
		String state = "Texas";
		ShippingRate rate = shippingRateRepository.findByCountryAndState(countryId, state);
		System.out.println(rate);
		assertThat(rate.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCodSupported() {
		int rateId = 2;
		shippingRateRepository.updateCodSupported(rateId, true);
	}
	
}
