package com.olaaref.mintshop.cartItem;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.repository.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTest {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testFindByCustomer() {
		List<CartItem> items = cartItemRepository.findByCustomer(testEntityManager.find(Customer.class, 3));
		items.forEach(System.out::println);
		assertThat(items.size()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Customer customer = testEntityManager.find(Customer.class, 3);
		Product product = testEntityManager.find(Product.class, 12);
		CartItem item = cartItemRepository.findByCustomerAndProduct(customer, product);
		System.out.println(item);
		assertThat(item.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSaveItem() {
		int customerId = 3;
		int productId = 8;
		CartItem cartItem = new CartItem();
		cartItem.setCustomer(testEntityManager.find(Customer.class, customerId));
		cartItem.setProduct(testEntityManager.find(Product.class, productId));
		cartItem.setQuantity(2);
		CartItem item = cartItemRepository.save(cartItem);
		assertThat(item).isNotNull();
		assertThat(item.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSaveItem2() {
		int customerId = 3;
		int productId = 12;
		CartItem cartItem = new CartItem();
		cartItem.setCustomer(testEntityManager.find(Customer.class, customerId));
		cartItem.setProduct(testEntityManager.find(Product.class, productId));
		cartItem.setQuantity(4);
		CartItem item = cartItemRepository.save(cartItem);
		assertThat(item).isNotNull();
		assertThat(item.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateQuantity() {
		int customerId = 3;
		int productId = 12;
		int quantity = 3;
		cartItemRepository.updateQuantity(quantity, customerId, productId);
		
		Customer customer = testEntityManager.find(Customer.class, customerId);
		Product product = testEntityManager.find(Product.class, productId);
		CartItem item = cartItemRepository.findByCustomerAndProduct(customer, product);
		assertThat(item.getQuantity()).isEqualTo(quantity);
	}
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		int customerId = 3;
		int productId = 12;
		cartItemRepository.deleteByCustomerAndProduct(customerId, productId);
		
		Customer customer = testEntityManager.find(Customer.class, customerId);
		Product product = testEntityManager.find(Product.class, productId);
		CartItem item = cartItemRepository.findByCustomerAndProduct(customer, product);
		
		assertThat(item).isNull();
	}
}
