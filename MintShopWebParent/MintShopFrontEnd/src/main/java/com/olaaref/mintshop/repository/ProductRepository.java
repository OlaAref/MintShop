package com.olaaref.mintshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) ORDER BY p.name ASC")
	Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);
	
	Product findByAlias(String alias);
	
	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
			+ "MATCH(name, short_description, full_description) AGAINST(?1)", 
			nativeQuery = true)
	Page<Product> search(String keyword, Pageable pageable);
	
	//COALESCE is a function take N params
	//and return first non-null value
	@Query("UPDATE Product p SET p.averageRating = COALESCE((SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1), 0), "
			+ "p.reviewCount  = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) "
			+ "WHERE p.id = ?1")
	@Modifying
	public void updateReviewCountAndAverageRating(Integer productId);
	
	@Query("UPDATE Product p SET "
			+ "p.questionCount  = (SELECT COUNT(q.id) FROM Question q WHERE q.product.id = ?1) "
			+ "WHERE p.id = ?1")
	@Modifying
	public void updateQuestionCount(Integer productId);
	
	public Page<Product> findByBrand(Brand brand, Pageable pageable);
	
}
