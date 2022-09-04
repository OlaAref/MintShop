package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer>{

	@Query("SELECT m FROM Menu m WHERE m.enabled = true")
	public List<Menu> findAll();
}
