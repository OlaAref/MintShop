package com.olaaref.mintshop.reviewVote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olaaref.mintshop.repository.ReviewRepository;
import com.olaaref.mintshop.vote.VoteResult;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewVoteRestControllerTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	public void anonymousVoteTest() throws Exception {
		String uri = "/votes/vote-review/1/up";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertFalse(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("You must log"));
	}
	
	@Test
	@WithMockUser(username = "olaaref24@gmail.com", password = "123")
	public void nonExistReviewVoteTest() throws Exception {
		String uri = "/votes/vote-review/123/up";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertFalse(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("no longer exists"));
	}
	
	@Test
	@WithMockUser(username = "olaaref24@gmail.com", password = "123")
	public void voteUpTest() throws Exception {
		String uri = "/votes/vote-review/3/up";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertTrue(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("You have successfully voted"));
	}
	
	@Test
	@WithMockUser(username = "olaaref24@gmail.com", password = "123")
	public void undoVoteUpTest() throws Exception {
		String uri = "/votes/vote-review/3/up";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertTrue(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("You have unvoted"));
	}
	
	@Test
	@WithMockUser(username = "olaaref24@gmail.com", password = "123")
	public void voteDownTest() throws Exception {
		String uri = "/votes/vote-review/4/down";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertTrue(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("You have successfully voted"));
	}
	
	@Test
	@WithMockUser(username = "olaaref24@gmail.com", password = "123")
	public void undoVoteDownTest() throws Exception {
		String uri = "/votes/vote-review/4/down";
		
		MvcResult mvcResult = mockMvc
			.perform(post(uri).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertTrue(voteResult.isSuccessful());
		assertThat(voteResult.getMessage().contains("You have unvoted"));
	}
	
}















