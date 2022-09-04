package com.olaaref.mintshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	
	

}
