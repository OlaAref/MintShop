package com.olaaref.mintshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.service.AddressService;
import com.olaaref.mintshop.service.ShippingRateService;
import com.olaaref.mintshop.service.ShoppingCartService;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShippingRateService shippingRateService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private HelperController helper;
	
	@GetMapping("/list")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = helper.getAuthenticatedCustomer(request);
		List<CartItem> itemsList = shoppingCartService.listCartItems(customer);
		float estimatedTotal = 0.0f;
		for (CartItem item : itemsList) {
			estimatedTotal += item.getSubTotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shipRate = null;
		boolean isPrimaryAddressDefault = true;
		
		if(defaultAddress != null) {
			shipRate = shippingRateService.getShippingRateForAddress(defaultAddress);
			isPrimaryAddressDefault = false;
		}
		else {
			shipRate = shippingRateService.getShippingRateForCustomer(customer);
		}
		
		boolean supportedAddress = shipRate != null;
		
		model.addAttribute("items", itemsList);
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("isPrimaryAddressDefault", isPrimaryAddressDefault);
		model.addAttribute("supportedAddress", supportedAddress);

		return "cart/shopping-cart";
	}

}
