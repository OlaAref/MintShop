package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.common.exception.ShippingRateAlreadyExistException;
import com.olaaref.mintshop.common.exception.ShippingRateNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.ShippingRateService;

@Controller
@RequestMapping({ "/shippingRate" })
public class ShippingRateController {
	private String listRedirectUrl = "redirect:/shippingRate/page/1?sortField=country&sortDir=asc";

	@Autowired
	private ShippingRateService shippingRateService;

	@GetMapping({ "/list" })
	public String list() {
		return this.listRedirectUrl;
	}

	@GetMapping({ "/page/{pageNum}" })
	public String listByPage(@PathVariable("pageNum") int pageNum,
			@PagingAndSortingParam(listName = "rates", moduleUrl = "/shippingRate") PagingAndSortingHelper helper) {
		this.shippingRateService.listByPage(pageNum, helper);
		return "shippingRates/list-shippingRates";
	}

	@GetMapping({ "/toAdd" })
	public String toAddPage(Model model) {
		model.addAttribute("countries", this.shippingRateService.listAllCountries());
		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("pageTitle", "Add");
		return "shippingRates/shippingRate-form";
	}

	@PostMapping({ "/add" })
	public String addShippingRate(ShippingRate rate, RedirectAttributes redirectAttributes) {
		try {
			this.shippingRateService.save(rate);
			redirectAttributes.addFlashAttribute("message", "for " + rate.getState());
		} catch (ShippingRateAlreadyExistException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return this.listRedirectUrl;
	}

	@GetMapping({ "/edit/{id}" })
	public String loadShippingRate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			ShippingRate rate = this.shippingRateService.getById(id);
			model.addAttribute("countries", this.shippingRateService.listAllCountries());
			model.addAttribute("rate", rate);
			model.addAttribute("pageTitle", "Edit");
			return "shippingRates/shippingRate-form";
		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return this.listRedirectUrl;
		}
	}

	@GetMapping({ "/delete/{id}" })
	public String deleteRate(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			ShippingRate rate = this.shippingRateService.getById(id);
			this.shippingRateService.delete(id);
			redirectAttributes.addFlashAttribute("deleteMessag",
					"Shipping rate for " + rate.getState() + " has been deleted successfully.");
		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return this.listRedirectUrl;
	}

	@GetMapping({ "/cod/{id}/enabled/{status}" })
	public String updateCodSupported(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			ShippingRate rate = this.shippingRateService.getById(id);
			this.shippingRateService.updateCodSupported(id, status);
			redirectAttributes.addFlashAttribute("message", "for " + rate.getState());
		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return this.listRedirectUrl;
	}
}
