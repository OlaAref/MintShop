package com.olaaref.mintshop.reviewVote;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.ReviewVote;
import com.olaaref.mintshop.repository.CustomerRepository;
import com.olaaref.mintshop.repository.ReviewRepository;
import com.olaaref.mintshop.repository.ReviewVoteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewVoteRepositoryTest {
	
	@Autowired
	private ReviewVoteRepository voteRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void createReviewVoteTableTest() {
		
	}
	
	@Test
	public void createReviewVoteTest() {
		Integer reviewId = 1;
		Integer customerId = 20;
		
		Review review = reviewRepository.getById(reviewId);
		Customer customer = customerRepository.getById(customerId);
		
		ReviewVote vote = new ReviewVote();
		vote.setReview(review);
		vote.setCustomer(customer);
		vote.voteUp();
		
		ReviewVote savedVote = voteRepository.save(vote);
		System.out.println(savedVote);
	}
	
	@Test
	public void findByReviewAndCustomerTest() {
		Integer reviewId = 1;
		Integer customerId = 20;
		
		ReviewVote vote = voteRepository.findByReviewAndCustomer(reviewId, customerId);
		System.out.println(vote);
	}
	
	@Test
	public void findByProductAndCustomerTest() {
		Integer reviewId = 1;
		Integer customerId = 20;
		
		List<ReviewVote> list = voteRepository.findByProductAndCustomer(20, customerId);
		list.forEach(System.out::println);
	}
	
	@Test
	public void countUpVotesTest() {
		Integer reviewId = 1;
		
		Long votes = voteRepository.countUpVotes(reviewId);
		System.out.println(votes);
	}
}
