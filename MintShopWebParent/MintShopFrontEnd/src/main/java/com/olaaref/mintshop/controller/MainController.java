package com.olaaref.mintshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.service.CategoryService;

@Controller
public class MainController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/")
	public String viewMainPage(Model model) {
		List<Category> nonChildrenCategories = categoryService.listNonChildrenCategories();
		model.addAttribute("categories", nonChildrenCategories);
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
		
	}

}
