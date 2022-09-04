package com.olaaref.mintshop.shippingRate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ShippingRateNotFoundException;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.dao.ShippingRateRepository;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.service.ShippingRateService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTest {
	
	@MockBean
	private ShippingRateRepository shippingRepository;
	@MockBean
	private ProductRepository productRepository;
	
	@InjectMocks
	private ShippingRateService shippingService;
	
	@Test
	public void testCalculateShippingCost_NoRateFound() {
		Integer productIds = 1;
		Integer countryId = 233;
		String state = "ABC";
		
		Mockito.when(shippingRepository.findByCountryAndState(countryId, state)).thenReturn(null);
		
		assertThrows(ShippingRateNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				shippingService.calculateShippingCost(productIds, countryId, state);
			}
			
		});
	}
	
	@Test
	public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException, ProductNotFoundException {
		Integer productIds = 1;
		Integer countryId = 233;
		String state = "New York";
		
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10);
		
		Mockito.when(shippingRepository.findByCountryAndState(countryId, state)).thenReturn(shippingRate);
		
		Product product = new Product();
		product.setWeight(5);
		product.setHeight(3);
		product.setWidth(4);
		product.setLength(8);
		
		Mockito.when(productRepository.getById(productIds)).thenReturn(product);
		
		float shippingCost = shippingService.calculateShippingCost(productIds, countryId, state);
		
		assertEquals(50, shippingCost);
	}

}
