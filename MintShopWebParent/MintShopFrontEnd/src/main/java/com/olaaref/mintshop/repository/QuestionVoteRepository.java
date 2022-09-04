package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.QuestionVote;

@Repository
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Integer> {
	
	@Query("SELECT v FROM QuestionVote v WHERE v.customer.id = ?1 AND v.question.id = ?2")
	public QuestionVote  findByCustomerAndQuestion(Integer customerId, Integer questionId);
	
	@Query("SELECT v From QuestionVote v WHERE v.customer.id = ?1 AND v.question.product.id = ?2")
	public List<QuestionVote> findByCustomerAndProduct(Integer customerId, Integer productId);
	
	
}
