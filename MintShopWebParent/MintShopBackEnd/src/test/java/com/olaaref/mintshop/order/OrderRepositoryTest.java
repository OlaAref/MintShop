package com.olaaref.mintshop.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.common.entity.order.OrderTrack;
import com.olaaref.mintshop.common.entity.order.PaymentMethod;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.dao.OrderRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateOrder() {
		Customer customer = testEntityManager.find(Customer.class, 1);
		Product product = testEntityManager.find(Product.class, 1);
		
		Order order = new Order();
		order.copyAddressFromCustomer();
		
		order.setShippingCost(10);
		order.setProductCost(product.getCost());
		order.setTax(0);
		order.setSubTotal(product.getPrice());
		order.setTotal(product.getPrice() + 10);
		
		order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		order.setOrderStatus(OrderStatus.NEW);
		order.setDeliverDate(LocalDate.now().plusDays(5));
		order.setDeliverDays(5);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(order);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubTotal(product.getPrice() * 1);
		orderDetail.setUnitPrice(product.getPrice());
		
		order.getOrderDetails().add(orderDetail);
		
		Order savedOrder = orderRepository.save(order);
		System.out.println(savedOrder);
		
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateOrderWithTwoProducts() {
		Customer customer = testEntityManager.find(Customer.class, 2);
		Product product1 = testEntityManager.find(Product.class, 3);
		Product product2 = testEntityManager.find(Product.class, 7);
		
		Order order = new Order();
		order.setCustomer(customer);
		order.copyAddressFromCustomer();
		
		order.setShippingCost(30);
		order.setProductCost(product1.getCost() + product2.getCost());
		order.setTax(0);
		float subtotal = product1.getPrice() * 1 + product2.getPrice() * 2;
		order.setSubTotal(subtotal);
		order.setTotal(subtotal + 30);
		
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderStatus(OrderStatus.PROCESSING);
		order.setDeliverDate(LocalDate.now().plusDays(3));
		order.setDeliverDays(3);
		
		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setProduct(product1);
		orderDetail1.setOrder(order);
		orderDetail1.setProductCost(product1.getCost());
		orderDetail1.setShippingCost(10);
		orderDetail1.setQuantity(1);
		orderDetail1.setSubTotal(product1.getPrice() * 1);
		orderDetail1.setUnitPrice(product1.getPrice());
		
		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setProduct(product2);
		orderDetail2.setOrder(order);
		orderDetail2.setProductCost(product2.getCost());
		orderDetail2.setShippingCost(20);
		orderDetail2.setQuantity(2);
		orderDetail2.setSubTotal(product2.getPrice() * 2);
		orderDetail2.setUnitPrice(product2.getPrice());
		
		order.getOrderDetails().add(orderDetail1);
		order.getOrderDetails().add(orderDetail2);
		
		Order savedOrder = orderRepository.save(order);
		System.out.println(savedOrder);
		
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}

	
	@Test
	public void testListOrders() {
		List<Order> orders = orderRepository.findAll();
		orders.forEach(System.out::println);
		
		assertThat(orders).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testUpdateOrder() {
		Order order = orderRepository.findById(2).get();
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderStatus(OrderStatus.SHIPPING);
		
		Order savedOrder = orderRepository.save(order);
		
		assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPING);
		
		
	}
	
	@Test
	public void testGetOrder() {
		Order order = orderRepository.findById(2).get();
		System.out.println(order);
	}
	
	@Test
	public void testDeleteOrder() {
		orderRepository.deleteById(2);
		Optional<Order> order = orderRepository.findById(2);
		assertThat(order).isNotPresent();
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Order order = orderRepository.findById(11).get();
		
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setStatus(OrderStatus.PROCESSING);
		track.setNotes(OrderStatus.PROCESSING.defaultDescription());
		
		order.getOrderTracks().add(track);
		
		Order updatedOrder = orderRepository.save(order);
		
		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}
	
	@Test
	public void findByOrderTimeBetweenTest() throws ParseException {
		
		LocalDateTime startDate = LocalDate.parse("2021-12-12").atStartOfDay();
		LocalDateTime endDate = LocalDate.parse("2021-12-15").atStartOfDay();
		System.out.println(startDate);
		List<Order> orders = orderRepository.findByOrderTimeBetween(startDate, endDate);
		
		assertThat(orders.size()).isGreaterThan(0);
		
		for(Order order : orders) {
			//Order(Integer id, float productCost, float subTotal, float total, LocalDateTime orderTime)
			System.out.printf("%s | %.2f | %.2f | %.2f | %s \n", 
					order.getId(), order.getProductCost(), order.getSubTotal(), order.getTotal(), order.getOrderTime());
		}
	}
}





















