package com.olaaref.mintshop.checkout.paypal;


import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PaypalApiTest {

	private static final String BASE_URL = "https://api-m.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AZAMvoi6oxCyFVyYNDYzl3cqe7m8jef0vyyxj7XgSZcCKYhYML6H4pvW3Fk4qRg1X8knywSRgD6TfIkG";
	private static final String CLIENT_SECRET = "EHFrgtxxzhGKyYqz879CmWYwkjwYa-N4BmdiYJ-1St6Lvjh7_JhJEQAnUWUDGWANMv_4SLnRhBn7VQR3";
	
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "4P55785551340894A";
		String orderUrl = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(orderUrl, HttpMethod.GET, request, PaypalOrderResponse.class);
		
		PaypalOrderResponse orderResponse = response.getBody();
		System.out.println("Order ID : "  + orderResponse.getId());
		System.out.println("Validate : "  + orderResponse.validate(orderId));
	}
	
}
