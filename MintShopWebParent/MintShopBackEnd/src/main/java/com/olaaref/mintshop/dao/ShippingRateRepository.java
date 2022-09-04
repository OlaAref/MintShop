package com.olaaref.mintshop.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface ShippingRateRepository extends EntityPagingRepository<ShippingRate, Integer> {

	@Query("SELECT s FROM ShippingRate s WHERE CONCAT(s.country.name, ' ', s.state) LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT s FROM ShippingRate s WHERE s.country.id = ?1 AND s.state = ?2")
	public ShippingRate findByCountryAndState(Integer countryId, String state);
	
	@Query("UPDATE ShippingRate s SET s.codSupported = ?2 WHERE s.id = ?1")
	@Modifying
	public void updateCodSupported(Integer id, boolean status);
	
	public long countById(Integer id);

	public long countByCodSupported(boolean supported);
}
