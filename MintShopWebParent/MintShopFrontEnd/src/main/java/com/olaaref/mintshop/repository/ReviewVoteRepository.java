package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.ReviewVote;

@Repository
public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Integer> {
	
	@Query("SELECT v FROM ReviewVote v WHERE v.review.id = ?1 AND v.customer.id = ?2")
	public ReviewVote findByReviewAndCustomer(Integer reviewId, Integer customerId);
	
	@Query("SELECT v FROM ReviewVote v WHERE v.review.product.id = ?1 AND v.customer.id = ?2")
	public List<ReviewVote> findByProductAndCustomer(Integer productId, Integer customerId);
	
	@Query("SELECT COUNT(v.id) FROM ReviewVote v WHERE v.review.id = ?1 AND v.votes = 1")
	public Long countUpVotes(Integer reviewId);
}
