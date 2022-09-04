package com.olaaref.mintshop.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Sales"})
	public void getReportForPast7DaysTest() throws Exception {
		String url = "/reports/sales_by_date/last_6_months";
		
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Sales"})
	public void getReportForCustomeDateRangeTest() throws Exception {
		String startDate = "2021-12-12";
		String endDate = "2021-12-15";
		String url = "/reports/sales_by_date/"+startDate+"/"+endDate;
		
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Sales"})
	public void getReportWithCategoryAndTimeTest() throws Exception {
		String period = "last_7_days";
		String groupBy = "category";
		String url = "/reports/"+groupBy+"/"+period;
		
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Sales"})
	public void getReportWithCategoryAndTimeRangeTest() throws Exception {
		String startDate = "2021-12-12";
		String endDate = "2021-12-15";
		String groupBy = "category";
		String url = "/reports/"+groupBy+"/"+startDate+"/"+endDate;
		
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "pass1", authorities = {"Sales"})
	public void getReportWithProductAndTimeTest() throws Exception {
		String period = "last_7_days";
		String groupBy = "product";
		String url = "/reports/"+groupBy+"/"+period;
		
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print());
	}

}
