package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/checkUnique")
	public String checkEmailUnique(@RequestParam("email") String email) {
		return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
