package com.olaaref.mintshop.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.exception.QuestionNotFoundException;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.dao.QuestionRepository;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class QuestionService {
	public static final int QUESTIONS_PER_PAGE = 5;

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public void listQuestionsByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, QUESTIONS_PER_PAGE, questionRepository);
	}
	
	public Question getQuestionById(Integer id) throws QuestionNotFoundException {
		try {
			return questionRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new QuestionNotFoundException("There is no Question found with id : " + id);
		}
	}
	
	public void deleteQuestion(Integer questionId) throws QuestionNotFoundException {
		Long count = questionRepository.countById(questionId);
		if(count == null || count == 0) {
			throw new QuestionNotFoundException("There is no Question found with id : " + questionId);
		}
		else {
			questionRepository.deleteById(questionId);
		}
	}
	
	public Question save(Question question) throws QuestionNotFoundException {
		Question dbQuestion = getQuestionById(question.getId());
		
		dbQuestion.setQuestionContent(question.getQuestionContent());
		dbQuestion.setApprovalStatus(question.isApprovalStatus());
		
		String answer = question.getAnswerContent();
		if(answer != null){
            if(!answer.trim().equals("")) {
            	dbQuestion.setAnswerContent(question.getAnswerContent());
    			dbQuestion.setAnsweredTime(LocalDateTime.now());
    			dbQuestion.setAnswerer(question.getAnswerer());
            }
        }	
		
		Question savedQuestion = questionRepository.save(dbQuestion);
		//update question count at product table
		productRepository.updateQuestionCount(savedQuestion.getProduct().getId());
		
		return savedQuestion;
		
	}
	
	public void updateApprovalStatus(Integer questionId, boolean status) {
		questionRepository.updateApprovalStatus(questionId, status);
	}
	
}


















