package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.ReviewVote;
import com.olaaref.mintshop.repository.ReviewRepository;
import com.olaaref.mintshop.repository.ReviewVoteRepository;
import com.olaaref.mintshop.vote.VoteResult;
import com.olaaref.mintshop.vote.VoteType;

@Service
@Transactional
public class ReviewVoteService {
	
	@Autowired
	private ReviewVoteRepository voteRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType) {
		Review review = null;
		
		try {
			review = reviewRepository.findById(reviewId).get();
		} catch (NoSuchElementException e) {
			//if the review not exist
			return VoteResult.fail("The review with ID : " + reviewId + " no longer exists.");
		}
		
		ReviewVote vote = voteRepository.findByReviewAndCustomer(reviewId, customer.getId());
		if(vote != null) {
			//undo vote cases
			if(vote.isVotedUp() && voteType.equals(VoteType.UP)
				|| vote.isVotedDown() && voteType.equals(VoteType.DOWN)) {
				
				return undoVote(vote, reviewId, voteType);
			}
			//down vote case
			else if(vote.isVotedUp() && voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			}
			//up vote case
			else if(vote.isVotedDown() && voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}
		}
		//if no vote found, create new one
		else {
			vote = new ReviewVote();
			vote.setCustomer(customer);
			vote.setReview(review);
			
			if(voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}
			else if (voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			}
			
		}
		
		//save vote
		voteRepository.save(vote);
		//update vote count at review table
		reviewRepository.updateVoteCount(reviewId);
		
		//return success voteResult
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		String message = "You have successfully voted "+voteType+" that review.";
		return VoteResult.success(message, voteCount);
	}

	public VoteResult undoVote(ReviewVote reviewVote, Integer reviewId, VoteType voteType) {
		//delete vote from vote table
		voteRepository.delete(reviewVote);
		//update review vote count
		reviewRepository.updateVoteCount(reviewId);
		//get new vote count
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		
		String message = "You have unvoted " + voteType + " that review";
		return VoteResult.success(message, voteCount);
	}
	
	public Long countUpVotes(Integer reviewId) {
		return voteRepository.countUpVotes(reviewId);
	}
	
	public void markReviewsVotedByCustomerForProduct(List<Review> reviews, Integer productId, Integer customerId) {
		List<ReviewVote> votes = voteRepository.findByProductAndCustomer(productId, customerId);

		for(ReviewVote vote : votes) {
			Review votedReview = vote.getReview();
			
			if(reviews.contains(votedReview)) {
				int index = reviews.indexOf(votedReview);
				Review review = reviews.get(index);
				if(vote.isVotedUp()) {
					review.setVotedUpByCurrentCustomer(true);
				}
				else if(vote.isVotedDown()) {
					review.setVotedDownByCurrentCustomer(true);
				}
			}
		}
		
		reviews.forEach(review -> {
			System.out.println(review.getId() + ", UP : "+review.isVotedUpByCurrentCustomer()+", DOWN : "+review.isVotedDownByCurrentCustomer());
		});
	}
}

















