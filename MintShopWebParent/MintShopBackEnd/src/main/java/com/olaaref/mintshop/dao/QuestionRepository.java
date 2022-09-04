package com.olaaref.mintshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface QuestionRepository extends EntityPagingRepository<Question, Integer> {
	
	@Query("SELECT q FROM Question q WHERE q.product.name LIKE %?1% OR "
			+ "q.asker.firstName LIKE %?1% OR q.asker.lastName LIKE %?1% OR "
			+ "q.questionContent LIKE %?1% OR q.answerContent LIKE %?1%")
	public Page<Question> findAll(String keyword, Pageable pageble);
	
	public Long countById(Integer questionId);
	
	@Query("UPDATE Question q SET q.approvalStatus = ?2 WHERE q.id = ?1")
	@Modifying
	public void updateApprovalStatus(Integer questionId, boolean approvalStatus);

	public long countByApprovalStatus(boolean approved);

	@Query("SELECT COUNT(q.id) FROM Question q WHERE q.answerContent IS NOT NULL")
	public long countAnsweredQuestions();
	
	@Query("SELECT COUNT(q.id) FROM Question q WHERE q.answerContent IS NULL")
	public long countNotAnsweredQuestions();
}
