package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface CategoryRepository extends EntityPagingRepository<Category, Integer> {
	
	Category findByName(@Param("name") String name);
	Category findByAlias(@Param("alias") String alias);
	
	public Long countById(Integer id);
	
	@Query("SELECT c FROM Category c WHERE CONCAT (c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories(Pageable pageable);
	
	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying//support for DML like 'UPDATE'
	public void updateEnabledStatus(Integer id, boolean enabledStatus);
	
	@Query("SELECT COUNT(c.id) FROM Category c")
	public long countCategories();
	
	@Query("SELECT COUNT(c.id) FROM Category c WHERE c.parent.id is NULL")
	public long countRootCategories();
	
	@Query("SELECT COUNT(c.id) FROM Category c WHERE c.enabled = true")
	public long countEnabledCategories();
	
	@Query("SELECT COUNT(c.id) FROM Category c WHERE c.enabled = false")
	public long countDisabledCategories();

}
