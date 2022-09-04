package com.olaaref.mintshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{
	
	public Brand findByName(String name);
}
