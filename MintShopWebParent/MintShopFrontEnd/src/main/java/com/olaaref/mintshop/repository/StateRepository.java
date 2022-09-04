package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.State;

public interface StateRepository extends JpaRepository<State, Integer> {

	List<State> findAllByCountryOrderByNameAsc(Country country);
	
	State findByName(String name);
}
