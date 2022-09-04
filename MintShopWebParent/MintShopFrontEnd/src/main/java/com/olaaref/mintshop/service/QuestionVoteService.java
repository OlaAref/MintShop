package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.QuestionVote;
import com.olaaref.mintshop.repository.QuestionRepository;
import com.olaaref.mintshop.repository.QuestionVoteRepository;
import com.olaaref.mintshop.vote.VoteResult;
import com.olaaref.mintshop.vote.VoteType;

@Service
@Transactional
public class QuestionVoteService {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionVoteRepository voteRepository;
	
	public VoteResult undoVote(QuestionVote questionVote, Integer questionId, VoteType voteType) {
		//delete vote from table
		voteRepository.delete(questionVote);
		//update vote count in question table
		questionRepository.updateVoteCount(questionId);
		//get the new vote count
		Integer voteCount = questionRepository.getVoteCount(questionId);
		
		String message = "You have unvoted " + voteType + " that question.";
		return VoteResult.success(message, voteCount);
	}
	
	public void markQuestionVotedByCustomerForProduct(List<Question> questions, Integer productId, Integer customerId) {
		//get all votes for the current customer
		List<QuestionVote> votes = voteRepository.findByCustomerAndProduct(customerId, productId);
		
		for(QuestionVote vote : votes) {
			//get the question of this vote
			Question votedQuestion = vote.getQuestion();
			//check if this voted question exist in the list of question of this product
			if(questions.contains(votedQuestion)) {
				int index = questions.indexOf(votedQuestion);
				Question question = questions.get(index);
				if(vote.isVotedUp()) {
					question.setVotedUpByCurrentCustomer(true);
				}
				else if(vote.isVotedDown()) {
					question.setVotedDownByCurrentCustomer(true);
				}
			}
		}
	}
	
	public VoteResult doVote(Integer questionId, Customer customer, VoteType voteType) {
		
		Question question = null;
		
		try {
			//get the question from DB
			question = questionRepository.findById(questionId).get();
			
		} catch (NoSuchElementException e) {
			return VoteResult.fail("This question is no longer exists.");
		}
		
		//check if the customer voted this question
		QuestionVote vote = voteRepository.findByCustomerAndQuestion(customer.getId(), questionId);
	
		//if vote exist
		if(vote != null) {
			//undo vote case
			if(vote.isVotedUp() && voteType.equals(VoteType.UP)
				|| vote.isVotedDown() && voteType.equals(VoteType.DOWN)) {
				return undoVote(vote, questionId, voteType);
			}
			//vote down case
			else if(vote.isVotedUp() && voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			}
			//vote up case
			else if(vote.isVotedDown() && voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}
		}
		//if no vote found in DB, create new one
		else {
			vote = new QuestionVote();
			vote.setCustomer(customer);
			vote.setQuestion(question);
			
			if(voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}
			else if(voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			}
		}
		
		//save the vote
		voteRepository.save(vote);
		//update vote count in question table
		questionRepository.updateVoteCount(questionId);
		
		//return success VoteResult
		Integer voteCount = questionRepository.getVoteCount(questionId);
		String message = "You have successfully voted " + voteType + " that question.";
		return VoteResult.success(message, voteCount);
		
		
	}
}



























