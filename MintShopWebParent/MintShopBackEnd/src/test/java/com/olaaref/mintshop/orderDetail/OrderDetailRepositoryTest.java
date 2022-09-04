package com.olaaref.mintshop.orderDetail;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.dao.OrderDetailRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTest {
	@Autowired
	private OrderDetailRepository detailsRepository;
	
	@Test
	public void findWithCategoryAndTimeBetweenTest() throws ParseException {
		LocalDateTime startDate = LocalDate.parse("2021-12-12").atStartOfDay();
		LocalDateTime endDate = LocalDate.parse("2021-12-15").atStartOfDay();
		
		List<OrderDetail> orders = detailsRepository.findWithCategoryAndTimeBetween(startDate, endDate);
		
		assertThat(orders.size()).isGreaterThan(0);
		
		for(OrderDetail detail : orders) {
			//String categoryName, int quantity, float shippingCost, float productCost, float subTotal
			System.out.printf("%s | %d | %.2f | %.2f | %.2f \n", 
							detail.getProduct().getCategory().getName(), 
							detail.getQuantity(), 
							detail.getShippingCost(), 
							detail.getProductCost(), 
							detail.getSubTotal());
		}
		
	}
	
	@Test
	public void findWithProductAndTimeBetweenTest() throws ParseException {
		LocalDateTime startDate = LocalDate.parse("2021-12-12").atStartOfDay();
		LocalDateTime endDate = LocalDate.parse("2021-12-15").atStartOfDay();
		
		List<OrderDetail> orders = detailsRepository.findWithProductAndTimeBetween(startDate, endDate);
		
		assertThat(orders.size()).isGreaterThan(0);
		System.out.printf("%-60s | %s |%6s|%6s| %6s \n","Product Name", "Q", "shipping", "Cost", "Subtotal");
		System.out.println("----------------------------------------------------------------------------------------");
		for(OrderDetail detail : orders) {
			
			System.out.printf("%-60s | %d | %6.2f | %6.2f | %6.2f \n", 
							detail.getProduct().getShortName(), 
							detail.getQuantity(), 
							detail.getShippingCost(), 
							detail.getProductCost(), 
							detail.getSubTotal());
		}
		
	}
}
