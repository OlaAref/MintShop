package com.olaaref.mintshop.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping({ "/" })
	public String viewMainPage() {
		return "index";
	}

	@GetMapping({ "/login" })
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
				|| authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
			return "login";
		return "redirect:/";
	}
}
