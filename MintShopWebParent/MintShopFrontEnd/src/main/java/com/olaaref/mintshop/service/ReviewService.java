package com.olaaref.mintshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ReviewNotFoundException;
import com.olaaref.mintshop.repository.OrderDetailRepository;
import com.olaaref.mintshop.repository.ProductRepository;
import com.olaaref.mintshop.repository.ReviewRepository;

@Service
@Transactional
public class ReviewService {

	public static final int REVIEWS_PER_PAGE = 5;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	public Page<Review> listByCustomerByPage(int pageNum, String sortField, String sortDir, String keyword, int customerId){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
		
		Page<Review> page = null;
		
		if(keyword == null || keyword.equals("")) {
			page = reviewRepository.findByCustomer(customerId, pageable);
		}
		else {
			page = reviewRepository.findByCustomer(customerId, keyword, pageable);
		}
		
		return page;
	}
	
	public Page<Review> listByProductByPage(int pageNum, String sortField, String sortDir, Product product){
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
		
		return reviewRepository.findByProduct(product, pageable);
	}
	
	public Page<Review> listByProductAndStarsByPage(int pageNum, String sortField, String sortDir, Product product, int stars){
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
		
		return reviewRepository.findByProductAndRating(product, stars, pageable);
	}
	
	public Review getByCustomerAndId(Integer customerId, Integer reviewId) throws ReviewNotFoundException {
		Review review = reviewRepository.findByCustomerAndId(customerId, reviewId);
		if(review == null) {
			throw new ReviewNotFoundException("Customer does not have any review with id : "+reviewId);
		}
		return review;
	}
	
	public List<Review> getByProductId(Integer productId){
		return reviewRepository.findByProduct(productId);
	}
	
	public Map<Integer, Integer> getRatingPercentages(Integer productId){
		Map<Integer, Integer> percentages = new HashMap<>();
		initializeMap(percentages);
		
		//get count of each rate
		List<Review> reviews = reviewRepository.findByProduct(productId);
		reviews.forEach(review -> {
			Integer key = review.getRating();
			Integer value = percentages.get(key);
			percentages.replace(key, value+1);
		});
		
		//get percentage of each rate
		Product product = productRepository.getById(productId);
		int reviewCount = product.getReviewCount();
		percentages.entrySet().forEach(entry -> {
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			Integer percent = 0;
			if(reviewCount != 0) {
				percent = (value * 100) / reviewCount;
			}
			percentages.replace(key, percent);
		});
		
		return percentages;
	}

	private void initializeMap(Map<Integer, Integer> percentages) {
		percentages.put(1, 0);
		percentages.put(2, 0);
		percentages.put(3, 0);
		percentages.put(4, 0);
		percentages.put(5, 0);
		
	}
	
	public Page<Review> listThreeOfMostRecentReviewsByProduct(Product product){
		
		Sort sort = Sort.by("createdTime").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		return reviewRepository.findByProduct(product, pageable);
	}
	
	public Page<Review> listTopThreeReviewsByProduct(Product product){
		
		Sort sort = Sort.by("votes").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		return reviewRepository.findByProduct(product, pageable);
	}
	
	public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
		Long count = reviewRepository.countByCustomerAndProduct(customer.getId(), productId);
		return count > 0L;
	}
	
	public boolean canCustomerReviewProduct(Customer customer, Integer productId) {
		Long count = orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId, customer.getId(), OrderStatus.DELIVERED);
		return count > 0L;
	}
	
	public Review save(Review review) {
		//save the review
		Review savedReview = reviewRepository.save(review);
		
		//update product table
		productRepository.updateReviewCountAndAverageRating(savedReview.getProduct().getId());
		
		return savedReview;
	}
	
	
}















