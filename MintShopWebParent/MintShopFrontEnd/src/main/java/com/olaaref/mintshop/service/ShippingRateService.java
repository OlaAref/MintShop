package com.olaaref.mintshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.repository.ShippingRateRepository;

@Service
public class ShippingRateService {
	
	@Autowired
	private ShippingRateRepository shippingRateRepository;
	
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		String state = customer.getState();
		
		if(state == null || state.isEmpty()) {
			state = customer.getCity();
		}
		
		return shippingRateRepository.findByCountryAndState(customer.getCountry(), state);
	}
	
	public ShippingRate getShippingRateForAddress(Address address) {
		String state = address.getState();
		
		if(state == null || state.isEmpty()) {
			state = address.getCity();
		}
		
		return shippingRateRepository.findByCountryAndState(address.getCountry(), state);
				
	}

}
