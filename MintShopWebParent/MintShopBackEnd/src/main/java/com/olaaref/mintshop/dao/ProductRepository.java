package com.olaaref.mintshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface ProductRepository extends EntityPagingRepository<Product, Integer> {

	Product findByName(String name);
	
	Long countById(Integer id);

	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	void updateEnabledStatus(Integer id, boolean status);
	
	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.brand.name, ' ', p.category.name) LIKE %?1%")
	Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%")
	Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
			+ "AND"
			+ "(CONCAT(p.name, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.brand.name, ' ', p.category.name) LIKE %?3%)")
	Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	Page<Product> searchProductsByName(String keyword, Pageable pageable);
}
