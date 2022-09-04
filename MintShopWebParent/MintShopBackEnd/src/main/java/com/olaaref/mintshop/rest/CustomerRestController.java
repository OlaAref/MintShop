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
	
	@PostMapping("/check-email")
	public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email) {
		return customerService.isEmailUnique(id, email) ? "OK" : "Duplicate";
	}
}
