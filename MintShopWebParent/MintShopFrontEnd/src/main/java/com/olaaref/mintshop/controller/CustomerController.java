package com.olaaref.mintshop.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.oauth.CustomerOAuth2User;
import com.olaaref.mintshop.security.CustomerUserDetails;
import com.olaaref.mintshop.service.CustomerService;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.setting.EmailSettingBag;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private HelperController helper;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> countries = customerService.listAllCountries();
		model.addAttribute("countries", countries);
		model.addAttribute("customer", new Customer());
		return "register/register-form";
	}
	
	@PostMapping("/createCustomer")
	public String createCustomer(@ModelAttribute("customer") Customer customer, 
								 HttpServletRequest request,
								 Model model) throws UnsupportedEncodingException, MessagingException {
		
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		
		return "register/register-success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSender mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		//set [[name]]
		content = content.replace("[[name]]", customer.getFullName());
		
		//set [[URL]]
		String verifyUrl = Utility.getSiteUrl(request) + "/customer/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyUrl);
		
		//set the content after change
		helper.setText(content, true);
		
		mailSender.send(message);
		
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String verifyCode) {
		boolean verified = customerService.verify(verifyCode);
		return "register/" + (verified ? "verify-success" : "verify-fail");
	}
	
	@GetMapping("/details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		model.addAttribute("customer", customer);
		model.addAttribute("countries", customerService.listAllCountries());
		
		return "customer/account-form";
	}
	
	@PostMapping("/updateAccountDetails")
	public String updateAccountDetails(@ModelAttribute("customer") Customer customer, Model model,
										RedirectAttributes redirectAttributes,
										HttpServletRequest request) {
		
		customerService.updateCustomer(customer);
		setFullnameForAuthenticatedCustomer(request, customer);
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
		
		String redirectOption = request.getParameter("redirect");
		String redirectUrl = "redirect:/customer/details";
		
		if(redirectOption.equals("address")) {
			redirectUrl = "redirect:/address/list";
		}
		else if(redirectOption.equals("cart")) {
			redirectUrl = "redirect:/cart/list";
		}
		else if(redirectOption.equals("checkout")) {
			redirectUrl = "redirect:/address/list?redirect=checkout";
		}
		
		return redirectUrl;
	}
	
	private void setFullnameForAuthenticatedCustomer(HttpServletRequest request, Customer customer) {
		
		Principal principal = request.getUserPrincipal();
		
		//if customer logged in with email
		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer dbAuthCustomer = userDetails.getCustomer();
			dbAuthCustomer.setFirstName(customer.getFirstName());
			dbAuthCustomer.setLastName(customer.getLastName());
		}
		//if customer logged in with google or facebook account
		else if(principal instanceof OAuth2AuthenticationToken) {
			
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			oauth2User.setFullname(customer.getFullName());
		}
		
	}
	
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		
		CustomerUserDetails userDetails = null;
		
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
		else if(principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
		
		return userDetails;
	}
	
	
}
















