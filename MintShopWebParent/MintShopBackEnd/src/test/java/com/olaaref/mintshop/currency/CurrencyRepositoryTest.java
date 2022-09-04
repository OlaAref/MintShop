package com.olaaref.mintshop.currency;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Currency;
import com.olaaref.mintshop.dao.CurrencyRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Test
	public void testCreatTable() {
		
	}
	
	@Test
	public void testCreatCurrency() {
		Currency dollar = new Currency("United States Dollar", "USD", "$");
		currencyRepository.save(dollar);
	}
	
	@Test
	public void testGetCurrency() {
		List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();
		currencies.forEach(currency -> System.out.println(currency));
	}
}
