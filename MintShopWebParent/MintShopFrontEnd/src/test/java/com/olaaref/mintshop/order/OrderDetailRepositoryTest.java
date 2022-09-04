package com.olaaref.mintshop.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.repository.OrderDetailRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTest {
	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Test
	public void countByProductAndCustomerAndOrderStatusTest() {
		Integer productId = 13;
		Integer customerId = 43;
		OrderStatus status = OrderStatus.DELIVERED;
		Long count = detailRepository.countByProductAndCustomerAndOrderStatus(productId, customerId, status);
		
		assertThat(count).isGreaterThan(0L);
		System.out.println(count);
	}
}
