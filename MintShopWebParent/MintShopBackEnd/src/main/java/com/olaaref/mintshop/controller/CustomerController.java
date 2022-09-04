package com.olaaref.mintshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.CustomerService;

@Controller
@RequestMapping({ "/customer" })
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@GetMapping({ "/list" })
	public String listAllCustomers() {
		return "redirect:/customer/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping({ "/page/{pageNum}" })
	public String listByPage(
			@PagingAndSortingParam(listName = "customers", moduleUrl = "/customer") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum) {
		this.customerService.listAllCustomersByPage(pageNum, helper);
		return "customers/list-customers";
	}

	@GetMapping({ "{id}/enabled/{status}" })
	public String updateEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
		this.customerService.updateEnabledStatus(id, status);
		String enabled = status ? "enabled" : "disabled";
		String message = "The customer " + this.customerService.getById(id).getFullName() + " has been " + enabled
				+ " successfully.";
		redirectAttributes.addFlashAttribute("enabledMessag", message);
		return "redirect:/customer/list";
	}

	@GetMapping({ "/detail/{id}" })
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Customer customer = this.customerService.getById(id);
			model.addAttribute("customer", customer);
			return "customers/customer-detail-modal";
		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/customer/list";
		}
	}

	@GetMapping({ "/edit/{id}" })
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Customer customer = this.customerService.getById(id);
			List<Country> countries = this.customerService.listAllCountries();
			model.addAttribute("customer", customer);
			model.addAttribute("countries", countries);
			model.addAttribute("pageTitle", customer.getFullName());
			return "customers/customer-form";
		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/customer/list";
		}
	}

	@PostMapping({ "/save" })
	public String saveCustomer(@ModelAttribute("customer") Customer customer, RedirectAttributes redirectAttributes)
			throws CustomerNotFoundException {
		this.customerService.save(customer);
		redirectAttributes.addFlashAttribute("message", customer.getFullName());
		return "redirect:/customer/list";
	}

	@GetMapping({ "/delete/{id}" })
	public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes)
			throws CustomerNotFoundException {
		String name = this.customerService.getById(id).getFullName();
		try {
			this.customerService.deleteCustomer(id);
			redirectAttributes.addFlashAttribute("deleteMessag",
					"Customer " + name + " has been deleted successfully.");
		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
		}
		return "redirect:/customer/list";
	}
}
