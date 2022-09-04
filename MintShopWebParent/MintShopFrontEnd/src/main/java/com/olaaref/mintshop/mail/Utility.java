package com.olaaref.mintshop.mail;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import com.olaaref.mintshop.oauth.CustomerOAuth2User;
import com.olaaref.mintshop.setting.CurrencySettingBag;
import com.olaaref.mintshop.setting.EmailSettingBag;

@Component
public class Utility {
	
	public static String getSiteUrl(HttpServletRequest request) {
		//get the full url
		String requestUrl = request.getRequestURL().toString();
		//delete the part after context path (site name)
		String siteUrl = requestUrl.replace(request.getServletPath(), "");
		
		return siteUrl;
	}

	
	public static JavaMailSender prepareMailSender(EmailSettingBag emailBag) {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(emailBag.getHost());
		mailSender.setPort(emailBag.getPort());
		mailSender.setUsername(emailBag.getUsername());
		mailSender.setPassword(emailBag.getPassword());
		
		Properties mailProperties = new Properties();
		
		mailProperties.put("mail.smtp.auth", emailBag.getSmtpAuth());
		mailProperties.put("mail.smtp.starttls.required", emailBag.getSmtpSecured());
		mailProperties.put("mail.smtp.ssl.trust", "*");
		mailProperties.put("mail.debug", "true");
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
	
	public static String getEmailForAuthenticatedCustomer(HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		String customerEmail = null;
		
		if(principal == null) return null;
		
		//if customer logged in with email
		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
			customerEmail = principal.getName();
		}
		//if customer logged in with google or facebook account
		else if(principal instanceof OAuth2AuthenticationToken) {
			
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}
		
		return customerEmail;
	}
	
	public static String formatCurrency(float amount, CurrencySettingBag currencySettings) {
		String symbol = currencySettings.getSymbol();
		String symbolPosition = currencySettings.getSymbolPosition();
		String decimalPointType = currencySettings.getDecimalPointType();
		String thousanPointType = currencySettings.getThousanPointType();
		int decimalDigits = currencySettings.getDecimalDigits();
		
		String pattern = symbolPosition.equals("Before Price") ? symbol : "";
		pattern += "###,###";
		if(decimalDigits > 0) {
			pattern += ".";
			for (int i = 1; i <= decimalDigits; i++) {
				pattern += "#";
			}
		}
		pattern += symbolPosition.equals("After Price") ? symbol : "";
		
		char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';
		char thousandSeparator = thousanPointType.equals("POINT") ? '.' : ',';
		
		DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance();
		formatSymbols.setDecimalSeparator(decimalSeparator);
		formatSymbols.setGroupingSeparator(thousandSeparator);
		
		
		DecimalFormat formatter = new DecimalFormat(pattern, formatSymbols);
		
		return formatter.format(amount);
	}
	
	
}









