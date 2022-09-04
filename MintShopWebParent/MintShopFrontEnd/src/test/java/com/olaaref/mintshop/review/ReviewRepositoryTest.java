package com.olaaref.mintshop.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.repository.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Test
	public void findByCustomerTest() {
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(0, 5, sort);
		Page<Review> reviews = reviewRepository.findByCustomer(1, pageable);
		List<Review> list = reviews.toList();
		list.forEach(System.out::println);
	}
	
	@Test
	public void findByCustomerAndKeywordTest() {
		String keyword = "to";
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(0, 5, sort);
		Page<Review> reviews = reviewRepository.findByCustomer(1, keyword, pageable);
		List<Review> list = reviews.toList();
		list.forEach(System.out::println);
	}
	
	@Test
	public void findByCustomerAndReviewIdTest() {
		Integer customerId = 1;
		Integer reviewId = 4;
		Review review = reviewRepository.findByCustomerAndId(customerId, reviewId);
		System.out.println(review);
	}
	
	@Test
	public void findByProductIdTest() {
		Integer productId = 29;
		List<Review> reviews = reviewRepository.findByProduct(productId);
		reviews.forEach(System.out::println);
	}
	
	@Test
	public void findByProductTest() {
		Product product = new Product(29);
		Pageable pageable = PageRequest.of(0, 3);
		Page<Review> reviews = reviewRepository.findByProduct(product, pageable);
		List<Review> list = reviews.getContent();
		list.forEach(System.out::println);
	}
	
	@Test
	public void findByProductAndRatingTest() {
		Product product = new Product(29);
		int rating = 1;
		Pageable pageable = PageRequest.of(0, 3);
		Page<Review> reviews = reviewRepository.findByProductAndRating(product, rating, pageable);
		List<Review> list = reviews.getContent();
		list.forEach(System.out::println);
		System.out.println(reviews.getTotalElements());
	}
	
	@Test
	public void countByCustomerAndProductTest() {
		Integer customerId = 43;
		Integer productId = 13;
		Long count = reviewRepository.countByCustomerAndProduct(customerId, productId);
		
		System.out.println(count);
		assertThat(count).isGreaterThan(0L);
		
	}
	
	@Test
	public void updateVoteCountTest() {
		Integer reviewId = 1;
		reviewRepository.updateVoteCount(reviewId);
	}
	
	@Test
	public void getVoteCountTest() {
		Integer reviewId = 1;
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		System.out.println(voteCount);
	}
}




























