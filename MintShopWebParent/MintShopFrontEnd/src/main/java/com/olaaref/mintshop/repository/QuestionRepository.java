package com.olaaref.mintshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.product.Product;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1")
	public Page<Question> findByCustomer(Integer customerId, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND (q.product.name LIKE %?2%"
			+ "OR q.asker.firstName LIKE %?2% OR q.asker.lastName LIKE %?2%"
			+ "OR q.questionContent LIKE %?2% OR q.answerContent LIKE %?2%)")
	public Page<Question> findByCustomer(Integer customerId, String keyword, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.answerer.id = ?1")
	public Page<Question> findByAnswerer(Integer userId, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND q.id = ?2")
	public Question findByCustomerAndId(Integer customerId, Integer questionId);
	
	public Page<Question> findByProduct(Product product, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.product.id = ?1 AND q.approvalStatus = true")
	public Page<Question> findByProduct(Integer productId, Pageable pageable);
	
	@Query("SELECT q.votes FROM Question q WHERE q.id = ?1")
	public Integer getVoteCount(Integer questionId);
	
	@Query("UPDATE Question q SET q.votes = "
			+ "COALESCE((SELECT SUM(v.vote) FROM QuestionVote v WHERE v.question.id = ?1), 0)"
			+ "WHERE q.id = ?1")
	@Modifying
	public void updateVoteCount(Integer questionId);
	
	@Query("UPDATE Question q SET q.approvalStatus = true WHERE q.id = ?1")
	@Modifying
	public void updateApprovalStatus(Integer questionId);

	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND q.id = ?2")
	public Question getByCustomerAndId(Integer customerId, Integer questionId);
	
}





















