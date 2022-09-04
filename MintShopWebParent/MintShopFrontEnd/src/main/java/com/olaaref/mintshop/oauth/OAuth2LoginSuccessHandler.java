package com.olaaref.mintshop.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.olaaref.mintshop.common.entity.AuthenticationType;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.service.CustomerService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
		
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String countryCode = request.getLocale().getISO3Country();
		String clientName = oauth2User.getClientName();
		
		Customer customer = customerService.getCustomerByEmail(email);

		AuthenticationType authenticationType = getAuthenticationType(clientName);
		
		if(customer == null) {
			//add new customer
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
		}
		else {
			//customer is exist, so update the authentication type
			oauth2User.setFullname(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
		
	}
	
	private AuthenticationType getAuthenticationType(String clientName) {
		
		if(clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		}
		else if(clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		}
		else {
			return AuthenticationType.DATABASE;
		}
	}

}
