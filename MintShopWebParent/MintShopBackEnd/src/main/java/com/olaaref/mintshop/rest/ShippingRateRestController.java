package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.exception.ShippingRateNotFoundException;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.service.ShippingRateService;

@RestController
@RequestMapping("/shippingRates")
public class ShippingRateRestController {
	
	@Autowired
	private ShippingRateService shippingRateService;
	
	@PostMapping("/shippingCost")
	public String getShippingCost(Integer ProductId, Integer countryId, String state) throws ShippingRateNotFoundException, ProductNotFoundException {
		
		float shippingCost = shippingRateService.calculateShippingCost(ProductId, countryId, state);
		return String.valueOf(shippingCost);
	}

}
