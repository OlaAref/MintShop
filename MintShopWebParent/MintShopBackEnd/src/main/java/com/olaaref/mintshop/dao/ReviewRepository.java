package com.olaaref.mintshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface ReviewRepository extends EntityPagingRepository<Review, Integer> {
	
	@Query("SELECT r FROM Review r WHERE CONCAT(r.headline, ' ', r.comment, ' ', r.product.name, ' ',"
			+ " r.customer.firstName, ' ', r.customer.lastName) LIKE %?1%")
	public Page<Review> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	@Query("SELECT COUNT(DISTINCT r.product) FROM Review r")
	public long countReviewedProducts();
}
