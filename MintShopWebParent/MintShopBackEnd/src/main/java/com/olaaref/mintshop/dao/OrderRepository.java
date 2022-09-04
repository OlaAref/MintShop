package com.olaaref.mintshop.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.paging.EntityPagingRepository;


public interface OrderRepository extends EntityPagingRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id, ' ', o.firstName, ' ', o.lastName, ' ', o.addressLine1, ' ', "
			+ "o.addressLine2, ' ', o.phoneNumber, ' ', o.country, ' ', o.state, ' ', o.city, ' ', o.postalCode, ' ', "
			+ "o.customer.firstName, ' ', o.customer.lastName, ' ', o.paymentMethod, ' ', o.orderStatus) LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	public Page<Order> findAll(Pageable pageable);
	
	public Optional<Order> findById(Integer id);
	
	public Long countById(Integer id);
	
	@Query("SELECT NEW com.olaaref.mintshop.common.entity.order.Order(o.id, o.productCost, o.subTotal, o.total, o.orderTime) FROM Order o "
			+ "WHERE o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
	
	public long countByOrderStatus(OrderStatus orderStatus);
}
