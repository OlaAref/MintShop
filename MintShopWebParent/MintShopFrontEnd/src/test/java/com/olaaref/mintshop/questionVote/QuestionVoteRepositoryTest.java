package com.olaaref.mintshop.questionVote;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.QuestionVote;
import com.olaaref.mintshop.repository.CustomerRepository;
import com.olaaref.mintshop.repository.ProductRepository;
import com.olaaref.mintshop.repository.QuestionRepository;
import com.olaaref.mintshop.repository.QuestionVoteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionVoteRepositoryTest {
	
	@Autowired
	private QuestionVoteRepository voteRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void addVoteTest() {
		Question q = questionRepository.findById(9).get();
		Customer customer = customerRepository.findById(18).get();
		
		QuestionVote vote = new QuestionVote();
		vote.setCustomer(customer);
		vote.setQuestion(q);
		vote.voteUp();
		
		QuestionVote saved = voteRepository.save(vote);
		System.out.println(saved);
		
		questionRepository.updateVoteCount(9);
	}
	
	@Test
	public void findByCustomerAndQuestionTest() {
		QuestionVote vote = voteRepository.findByCustomerAndQuestion(18, 9);
		System.out.println(vote);
		
	}
	
	@Test
	public void findByCustomerAndProductTest() {
		List<QuestionVote> votes = voteRepository.findByCustomerAndProduct(18, 29);
		System.out.println(votes);
		
	}
}

























