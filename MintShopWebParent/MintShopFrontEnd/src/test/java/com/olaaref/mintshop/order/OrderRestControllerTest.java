package com.olaaref.mintshop.order;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@WithUserDetails("olaaref24@gmail.com")
	public void testSendOrderReturnRequest() throws JsonProcessingException, Exception {
		String reason = "Received wrong product";
		String note = "Kindly return product and get money back.";
		OrderReturnRequest returnRequest = new OrderReturnRequest(11, reason, note);
		
		String requestUrl = "/orders/return";
		mockMvc.perform(
					post(requestUrl)
						.with(csrf())
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(returnRequest)))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
