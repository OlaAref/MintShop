package com.olaaref.mintshop.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.controller.HelperController;
import com.olaaref.mintshop.service.QuestionVoteService;
import com.olaaref.mintshop.service.ReviewVoteService;
import com.olaaref.mintshop.vote.VoteResult;
import com.olaaref.mintshop.vote.VoteType;

@RestController
@RequestMapping("/votes")
public class VoteRestController {
	
	@Autowired
	private ReviewVoteService reviewVoteService;
	
	@Autowired
	private QuestionVoteService quesionVoteService;
	
	@Autowired
	private HelperController helper;
	
	@PostMapping("/vote-review/{reviewId}/{voteType}")
	public VoteResult voteReview(@PathVariable("reviewId") Integer reviewId,
							       @PathVariable("voteType") String voteType,
							       HttpServletRequest request) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
				
		if(customer == null) {
			return VoteResult.fail("You must login to vote the review.");
		}
		
		VoteType type = VoteType.valueOf(voteType.toUpperCase());
		
		return reviewVoteService.doVote(reviewId, customer, type);
	}
	
	@PostMapping("/vote-question/{questionId}/{voteType}")
	public VoteResult voteQuesion(@PathVariable("questionId") Integer questionId,
							   	   @PathVariable("voteType") String voteType,
							       HttpServletRequest request) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
				
		if(customer == null) {
			return VoteResult.fail("You must login to vote the Questione.");
		}
		
		VoteType type = VoteType.valueOf(voteType.toUpperCase());
		
		return quesionVoteService.doVote(questionId, customer, type);
	}
	
}
