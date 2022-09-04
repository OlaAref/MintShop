package com.olaaref.mintshop.question;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.repository.CustomerRepository;
import com.olaaref.mintshop.repository.ProductRepository;
import com.olaaref.mintshop.repository.QuestionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionRepositoryTest {
	
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void createQustionTableTest() {
		
	}
	
	@Test
	public void createQustionTest() {
		String questionContent = "Does it include a complete power cord and charger to charge computer?";
		Product product = productRepository.findById(29).get();
		Customer asker = customerRepository.findById(37).get();
		LocalDateTime askedTime = LocalDateTime.now();
		
		Question q = new Question();
		q.setQuestionContent(questionContent);
		q.setProduct(product);
		q.setAsker(asker);
		q.setAskedTime(askedTime);
		
		Question saved = questionRepository.save(q);
		System.out.println(saved);
	}
	
	@Test
	public void createQustion2Test() {
		String questionContent = "What change in these model from the previous model";
		Product product = productRepository.findById(29).get();
		Customer asker = customerRepository.findById(49).get();
		LocalDateTime askedTime = LocalDateTime.now();
		
		Question q = new Question();
		q.setQuestionContent(questionContent);
		q.setProduct(product);
		q.setAsker(asker);
		q.setAskedTime(askedTime);
		
		Question saved = questionRepository.save(q);
		System.out.println(saved);
	}
	
	@Test
	public void findByCustomerTest() {
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(0, 5, sort);
		
		Page<Question> questions = questionRepository.findByCustomer(49, pageable);
		System.out.println(questions.getContent());
	}
	
	@Test
	public void findByCustomerAndIdTest() {
		Question question = questionRepository.findByCustomerAndId(49, 11);
		System.out.println(question);
	}
	
	@Test
	public void findByProductTest() {
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(0, 5, sort);
		Product product = productRepository.findById(29).get();
		Page<Question> questions = questionRepository.findByProduct(product, pageable);
		System.out.println(questions.getContent());
	}
	
	@Test
	public void updateApprovalStatusTest() {
		questionRepository.updateApprovalStatus(9);
	}
	
}




































