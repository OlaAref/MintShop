package com.olaaref.mintshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private HelperController helper;
	
	@GetMapping("/list")
	public String showAddressBook(HttpServletRequest request, Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		List<Address> addresses = addressService.listAddressBook(customer);
		
		boolean isPrimaryAddressDefualt = true;
		for (Address address : addresses) {
			if(address.isDefaultForShopping()) {
				isPrimaryAddressDefualt = false;
				break;
			}
		}
		
		model.addAttribute("addresses", addresses);
		model.addAttribute("customer", customer);
		model.addAttribute("isPrimaryAddressDefualt", isPrimaryAddressDefualt);
		
		return "addresses/list-addresses";
	}
	
	@GetMapping("/toAdd")
	public String toAddNewAddressPage(Model model) {
		
		model.addAttribute("address", new Address());
		model.addAttribute("countries", addressService.listAllCountries());
		model.addAttribute("pageTitle", "Add");
		
		return "addresses/address-form";
	}
	
	@PostMapping("/add")
	public String saveAddress(@ModelAttribute("address") Address address,
							  RedirectAttributes redirectAttributes,
							  HttpServletRequest request) {
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		address.setCustomer(customer);
		addressService.saveAddress(address);
		
		redirectAttributes.addFlashAttribute("message", "The address has been saved successfully.");
		
		String redirectOption = request.getParameter("redirect");
		String redirectUrl = "redirect:/address/list";
		
		if(redirectOption != null && redirectOption.equals("checkout")) {
			redirectUrl += "?redirect=checkout";
		}
		
		return redirectUrl;
	}
	
	@GetMapping("/edit/{id}")
	public String editAddress(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		Address address = addressService.get(id, customer.getId());
		
		model.addAttribute("address", address);
		model.addAttribute("countries", addressService.listAllCountries());
		model.addAttribute("pageTitle", "Edit");
		
		return "addresses/address-form";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer id,
		  						RedirectAttributes redirectAttributes,
		  						HttpServletRequest request){
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		addressService.deletAddress(id, customer.getId());
		
		redirectAttributes.addFlashAttribute("message", "Address with ID : " + id + " has been deleted.");
		
		return "redirect:/address/list";
	}
	
	@GetMapping("/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer id,
									RedirectAttributes redirectAttributes,
									HttpServletRequest request) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(id, customer.getId());
		
		
		String redirectOption = request.getParameter("redirect");
		String redirectUrl = "redirect:/address/list";
		
		if(redirectOption != null && redirectOption.equals("cart")) {
			redirectUrl = "redirect:/cart/list";
		}
		else if(redirectOption.equals("checkout")) {
			redirectUrl = "redirect:/checkout/checkoutForm";
		}

		return redirectUrl;
	}
	
	
}
