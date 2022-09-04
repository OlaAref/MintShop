package com.olaaref.mintshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.QuestionNotFoundException;
import com.olaaref.mintshop.repository.ProductRepository;
import com.olaaref.mintshop.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {
	public static final int QUESTIONS_PER_PAGE = 4;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	//list questions for specific customer
	public Page<Question> listByCustomerByPage(int pageNum, String sortField, 
					String sortDir, String keyword, Integer customerId){
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE, sort);
		
		Page<Question> page = null;
		if(keyword == null || keyword.equals("")) {
			page = questionRepository.findByCustomer(customerId, pageable);
		}
		else {
			page = questionRepository.findByCustomer(customerId, keyword, pageable);
		}
		
		return page;
	}
	
	//list questions for specific product
	public Page<Question> listByProductByPage(int pageNum, String sortField, String sortDir, Integer productId){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE, sort);
		Page<Question> page = questionRepository.findByProduct(productId, pageable);
		return page;
	}
	
	public Page<Question> listTopThreeQuestionsByProduct(Integer productId){
		Sort sort = Sort.by("votes").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);
		return questionRepository.findByProduct(productId, pageable);
	}
	
	public Question save(Question question) {
		Question savedQuestion = questionRepository.save(question);
		productRepository.updateQuestionCount(savedQuestion.getProduct().getId());
		return savedQuestion;
	}

	public Question getByCustomerAndId(Integer customerId, Integer questionId) throws QuestionNotFoundException {
		Question question = questionRepository.getByCustomerAndId(customerId, questionId);
		
		if(question == null) {
			throw new QuestionNotFoundException("Customer does no have any question with id : " + questionId);
		}
		
		return question;
	}
	
	public void countAnsweredQuestiosForProduct(Integer productId) {
		int answeredQuestions = 0;
		List<Question> questions = listByProductByPage(1, "id", "asc", productId).getContent();
		for(Question q : questions) {
			if(q.isAnswered()) {
				answeredQuestions++;
			}
		}
		Product product = productRepository.getById(productId);
		product.setAnsweredQuestions(answeredQuestions);
	}
	
}





















