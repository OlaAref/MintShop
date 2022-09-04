package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.product.Product;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1")
	public Page<Review> findByCustomer(Integer customerId, Pageable pageable);
	
	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND CONCAT(r.product.name, ' ', r.headline, ' ', r.comment) LIKE %?2%")
	public Page<Review> findByCustomer(Integer customerId, String keyword, Pageable pageable);
	
	@Query("SELECT r FROM Review r WHERE r.id = ?2 AND r.customer.id = ?1")
	public Review findByCustomerAndId(Integer customerId, Integer reviewId);
	
	@Query("SELECT r FROM Review r WHERE r.product.id = ?1")
	public List<Review> findByProduct(Integer productId);
	
	public Page<Review> findByProduct(Product product, Pageable pageable);
	
	public Page<Review> findByProductAndRating(Product product, int rating, Pageable pageable);
	
	@Query("SELECT COUNT(r.id) FROM Review r WHERE r.customer.id = ?1 AND r.product.id = ?2")
	public Long countByCustomerAndProduct(Integer customerId, Integer productId);
	
	@Query("UPDATE Review r SET r.votes = COALESCE((SELECT SUM(v.votes) FROM ReviewVote v WHERE v.review.id = ?1), 0) "
			+ "WHERE r.id = ?1")
	@Modifying
	public void updateVoteCount(Integer reviewId);
	
	@Query("SELECT r.votes FROM Review r WHERE r.id = ?1")
	public Integer getVoteCount(Integer reviewId);
}
