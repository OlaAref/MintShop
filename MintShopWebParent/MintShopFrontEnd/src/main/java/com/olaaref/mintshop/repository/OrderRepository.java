package com.olaaref.mintshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
	public Page<Order> findAll(Integer customerId, Pageable pageable);
	
	@Query("SELECT DISTINCT o FROM Order o "
			+ "JOIN o.orderDetails od "
			+ "JOIN od.product p "
			+ "WHERE o.customer.id = ?2 "
			+ "AND (p.name LIKE %?1% OR o.orderStatus LIKE %?1%)")
	public Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);

	public Order findByIdAndCustomer(Integer orderId, Customer customer);
}
