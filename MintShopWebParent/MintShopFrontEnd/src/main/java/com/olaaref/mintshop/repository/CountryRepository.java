package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.olaaref.mintshop.common.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

	List<Country> findAllByOrderByNameAsc();
	
	@Query("SELECT c FROM Country c WHERE c.code = ?1")
	Country findByCode(String code);
}
