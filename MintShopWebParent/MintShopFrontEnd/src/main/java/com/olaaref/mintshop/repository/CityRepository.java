package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olaaref.mintshop.common.entity.City;
import com.olaaref.mintshop.common.entity.State;

public interface CityRepository extends JpaRepository<City, Integer> {

	List<City> findAllByStateOrderByNameAsc(State state);
	
	List<City> findAllByStateNameOrderByNameAsc(String stateName);
}
