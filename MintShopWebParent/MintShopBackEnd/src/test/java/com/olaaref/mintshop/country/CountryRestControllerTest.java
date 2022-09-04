package com.olaaref.mintshop.country;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.dao.CountryRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	CountryRepository countryRepository;
	
	@Test
	@WithMockUser(username = "div@mail.com", password = "123", roles = "ADMIN")
	public void testListAll() throws Exception {
		String url = "/countries/list";
		MvcResult result = this.mockMvc.perform(get(url))
					.andExpect(status().isOk())
					.andDo(print())
					.andReturn();
		String jsonCountries = result.getResponse().getContentAsString();
		System.out.println(jsonCountries);
		
		Country[] pojoCountries = objectMapper.readValue(jsonCountries, Country[].class);
		
		for (Country country : pojoCountries) {
			System.out.println(country);
		}
	}
	
	@Test
	@WithMockUser(username = "div@mail.com", password = "123", roles = "ADMIN")
	public void testSaveCountry() throws Exception {
		String url = "/countries/save";
		String countryName = "Canada";
		Country country = new Country(countryName, "CA", "");
		
		MvcResult result = this.mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(country))
					.with(csrf()))
					.andExpect(status().isOk())
					.andDo(print())
					.andReturn();
		String response = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(response);
		
		Optional<Country> savedCountry = countryRepository.findById(countryId);
		System.out.println(savedCountry.get());
		
	}
	
	@Test
	@WithMockUser(username = "div@mail.com", password = "123", roles = "ADMIN")
	public void testUpdateCountry() throws Exception {
		String url = "/countries/save";
		Integer countryId = 7;
		String countryName = "Canada";
		Country country = new Country(countryId, countryName, "CA", "");
		
		this.mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(country))
					.with(csrf()))
					.andExpect(status().isOk())
					.andDo(print())
					.andExpect(content().string(String.valueOf(countryId)));
		
		Optional<Country> savedCountry = countryRepository.findById(countryId);
		System.out.println(savedCountry.get());
		
	}
	
	@Test
	@WithMockUser(username = "div@mail.com", password = "123", roles = "ADMIN")
	public void testDeleteCountry() throws Exception {
		
		Integer countryId = 7;
		String url = "/countries/delete"+countryId;
		
		mockMvc.perform(get(url))
		.andExpect(status().isOk());
		
		Optional<Country> deletedCountry = countryRepository.findById(countryId);
		assertThat(deletedCountry).isNotPresent();
	}
}
