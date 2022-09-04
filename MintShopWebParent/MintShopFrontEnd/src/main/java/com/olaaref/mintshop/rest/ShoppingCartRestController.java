package com.olaaref.mintshop.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.exception.ShoppingCartException;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.service.CustomerService;
import com.olaaref.mintshop.service.ShoppingCartService;

@RestController
@RequestMapping("/cart")
public class ShoppingCartRestController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
								   @PathVariable("quantity") Integer quantity,
								   HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = shoppingCartService.addProductToCart(productId, quantity, customer);
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
			
		} catch (CustomerNotFoundException e) {
			return "You Must login to add this product to cart.";
		} catch (ShoppingCartException e) {
			return e.getMessage();
		}
		
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		
		String email = Utility.getEmailForAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No Authenticated Customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
							     @PathVariable("quantity") Integer quantity,
							     HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subTotal = shoppingCartService.updateQuantity(productId, quantity, customer);
			return String.valueOf(subTotal);
			
		} catch (CustomerNotFoundException e) {
			return "You Must login to change quantity of this product.";
		}
		
	}
	
	@DeleteMapping("/delete/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId,
								HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			shoppingCartService.removeProduct(productId, customer);
			
			return "The product has been removed from your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
		
	}
}
