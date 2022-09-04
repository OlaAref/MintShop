package com.olaaref.mintshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.service.CustomerService;

@Component
public class HelperController {
	@Autowired
	private CustomerService customerService;
	
	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailForAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
