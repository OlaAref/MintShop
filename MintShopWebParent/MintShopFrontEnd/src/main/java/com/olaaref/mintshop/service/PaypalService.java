package com.olaaref.mintshop.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.olaaref.mintshop.checkout.paypal.PaypalApiException;
import com.olaaref.mintshop.checkout.paypal.PaypalOrderResponse;
import com.olaaref.mintshop.setting.PaymentSettingBag;

@Component
public class PaypalService {
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	
	@Autowired
	private SettingService settingService;
	
	public boolean validateOrder(String orderID) throws PaypalApiException {
		
		ResponseEntity<PaypalOrderResponse> response = sendReauestToPaypal(orderID);
		
		//check the response status
		HttpStatus statusCode = response.getStatusCode();
		
		if (!statusCode.equals(HttpStatus.OK)) {
			throwExceptionForNonOkResponse(statusCode);
		}
		
		//get the response body
		PaypalOrderResponse orderResponse = response.getBody();
		
		return orderResponse.validate(orderID);
		
	}

	private ResponseEntity<PaypalOrderResponse> sendReauestToPaypal(String orderID) {
		
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String baseUrl = paymentSettings.getURL();
		String CLIENT_ID = paymentSettings.getClientID();
		String CLIENT_SECRET = paymentSettings.getClientSecret();
		String requestUrl = baseUrl + GET_ORDER_API + orderID;
		
		//prepare the header of the request
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		header.add("Accept-Language", "en_US");
		header.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		//create request entity with the header
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(header);
		
		//send the request to paypal checkout url using restTamplate
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, PaypalOrderResponse.class);
		
		return response;
	}

	private void throwExceptionForNonOkResponse(HttpStatus statusCode) throws PaypalApiException {
		
		String message = null;
		
		switch (statusCode) {
		case NOT_FOUND: {
			message = "Order Id not found";
		}
		case BAD_REQUEST: {
			message = "Bad Request to Paypal Checkout API";
		}
		case INTERNAL_SERVER_ERROR: {
			message = "Paypal server error";
		}
		default:
			message = "Paypa return non-OK status code";
		}
		
		throw new PaypalApiException(message);
	}

}
