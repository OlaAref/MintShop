package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.exception.ReviewNotFoundException;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.dao.ReviewRepository;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class ReviewService {
	
	public final static int REVIEWS_PER_PAGE = 5;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ProductRepository productRepository; 
	
	public List<Review> getAllReviews(){
		return reviewRepository.findAll();
	}
	
	public void listReviewsByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepository);
	}
	
	public Review getReviewById(Integer id) throws ReviewNotFoundException {
		try {
			return reviewRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ReviewNotFoundException("There is no review found with id : " + id);
		}
	}
	
	public void deleteReview(Integer id) throws ReviewNotFoundException {
		Long count = reviewRepository.countById(id);
		if(count == null || count == 0) {
			throw new ReviewNotFoundException("Review Not Found with id : " + id);
		}
		else {
			reviewRepository.deleteById(id);
		}
		
	}
	
	public Review save(Review review) throws ReviewNotFoundException {
		
		Review dbReview = getReviewById(review.getId());
		
		dbReview.setHeadline(review.getHeadline());
		dbReview.setComment(review.getComment());
		
		Review savedReview = reviewRepository.save(dbReview);
		//update the review count and average rate at product table
		productRepository.updateReviewCountAndAverageRating(dbReview.getProduct().getId());
		
		return savedReview;
	}
}















