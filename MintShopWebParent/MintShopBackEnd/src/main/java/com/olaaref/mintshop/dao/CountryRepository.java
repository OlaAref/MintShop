package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olaaref.mintshop.common.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

	List<Country> findAllByOrderByNameAsc();
}
