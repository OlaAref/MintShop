package com.olaaref.mintshop.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.dao.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Test
	public void createReviewTest() {
		Product product = new Product(20);
		Customer customer = new Customer(1);
		
		Review review = new Review();
		review.setHeadline("Perfect for my needs.");
		review.setComment("nice to have good camera, high resolution screen, amazing speaker.");
		review.setProduct(product);
		review.setCustomer(customer);
		review.setRating(5);
		review.setCreatedTime(LocalDateTime.now());
		
		Review saved = reviewRepository.save(review);
		
		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void createReview2Test() {
		Product product = new Product(32);
		Customer customer = new Customer(1);
		
		Review review = new Review();
		review.setHeadline(" Frees up your CPU for better performance.");
		review.setComment("THIS IS NO GAMING CARD. That said, this video card does a great job of liberating your CPU "
				+ "for a better desktop experience. I bought this card for use with my desktop computer at school (I am a teacher).  "
				+ "It gives my aging 2006 computer a nice performance boost, and gives me the ability to use a projector while I am using my PC."
				+ " You can probably do some light 3D gaming on it, but if that is your primary goal, Get yourself a GTX 750 ti and call it a day. "
				+ "This card's BEST use is what I described here, and it does it very nicely.");
		review.setProduct(product);
		review.setCustomer(customer);
		review.setRating(5);
		review.setCreatedTime(LocalDateTime.now());
		
		Review saved = reviewRepository.save(review);
		
		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void listAllReviewsTest() {
		List<Review> reviews = reviewRepository.findAll();
		
		reviews.forEach(System.out::println);
		
		assertThat(reviews.size()).isGreaterThan(0);
	}
	
	@Test
	public void getReviewTest() {
		Review review = reviewRepository.findById(2).get();
		
		System.out.println(review);
		assertThat(review).isNotNull();
	}
	
	@Test
	public void updateReviewTest() {
		Review review = reviewRepository.findById(1).get();
		review.setRating(4);
		System.out.println(review);
		assertThat(review).isNotNull();
	}
	
	@Test
	public void deleteReviewTest() {
		reviewRepository.deleteById(2);

	}
	
}















