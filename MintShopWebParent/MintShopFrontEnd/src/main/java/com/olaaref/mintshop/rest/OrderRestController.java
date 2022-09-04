package com.olaaref.mintshop.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.common.exception.OrderNotFoundException;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.order.OrderReturnRequest;
import com.olaaref.mintshop.order.OrderReturnResponse;
import com.olaaref.mintshop.service.CustomerService;
import com.olaaref.mintshop.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/return")
	@ResponseBody
	public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
													  HttpServletRequest servletRequest) {
		
		Customer customer = null;
		
		try {
			customer = getAuthenticatedCustomer(servletRequest);
		} catch (CustomerNotFoundException e) {
			return new ResponseEntity<>("Athuntication Required", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			orderService.setOrderReturnRequest(returnRequest, customer);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
	}
	
	public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailForAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer found.");
		}
		return customerService.getCustomerByEmail(email);
	}
}
