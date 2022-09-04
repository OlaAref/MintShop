package com.olaaref.mintshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface CustomerRepository extends EntityPagingRepository<Customer, Integer> {
	
	Long countById(Integer id);
	
	Customer findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.firstName, ' ', c.lastName, ' ',"
			+ " c.email, ' ', c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ',"
			+ " c.state, ' ', c.country.name, ' ', c.postalCode) LIKE %?1%")
	Page<Customer> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT COUNT(c.id) FROM Customer c")
	public long countCusomers();
	
	@Query("SELECT COUNT(c.id) FROM Customer c WHERE c.enabled = true")
	public long countEnabledCusomers();
	
	@Query("SELECT COUNT(c.id) FROM Customer c WHERE c.enabled = false")
	public long countDisabledCustomers();
}
