package com.olaaref.mintshop.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.order.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	@Query("SELECT NEW com.olaaref.mintshop.common.entity.order.OrderDetail(d.product.category.name, d.quantity, d.shippingCost, d.productCost, d.subTotal) "
			+ "FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
	public List<OrderDetail> findWithCategoryAndTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
	
	@Query("SELECT NEW com.olaaref.mintshop.common.entity.order.OrderDetail(d.quantity, d.product.name, d.shippingCost, d.productCost, d.subTotal) "
			+ "FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
	public List<OrderDetail> findWithProductAndTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
