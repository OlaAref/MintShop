package com.olaaref.mintshop.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.paging.EntityPagingRepository;


public interface OrderRepository extends EntityPagingRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id, ' ', o.firstName, ' ', o.lastName, ' ', o.addressLine1, ' ', "
			+ "o.addressLine2, ' ', o.phoneNumber, ' ', o.country, ' ', o.state, ' ', o.city, ' ', o.postalCode, ' ', "
			+ "o.customer.firstName, ' ', o.customer.lastName, ' ', o.paymentMethod, ' ', o.orderStatus) LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	public Page<Order> findAll(Pageable pageable);
	
	public Optional<Order> findById(Integer id);
	
	public Long countById(Integer id);
}
