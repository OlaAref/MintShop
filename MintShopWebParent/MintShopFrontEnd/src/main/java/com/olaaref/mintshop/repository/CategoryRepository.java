package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	List<Category> findAllEnabled();
	
	@Query("SELECT c FROM Category c WHERE c.alias = ?1 AND c.enabled = true")
	Category findByAliasEnabled(String alias);
	
}
