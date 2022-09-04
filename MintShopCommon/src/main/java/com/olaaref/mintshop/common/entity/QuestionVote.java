package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "question_votes")
public class QuestionVote extends IdBasedEntity{

	private static final int VOTE_UP_POINT = 1;
	private static final int VOTE_DOWN_POINT = -1;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	
	@Column(name = "vote")
	private int vote;
	
	public QuestionVote() {
		
	}
	
	public QuestionVote(Integer id) {
		this.id = id;
	}

	public QuestionVote(Customer customer, Question question, int vote) {
		this.customer = customer;
		this.question = question;
		this.vote = vote;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}
	
	public void voteUp() {
		this.vote = VOTE_UP_POINT;
	}
	
	public void voteDown() {
		this.vote = VOTE_DOWN_POINT;
	}
	
	@Transient
	public boolean isVotedUp() {
		return this.vote == VOTE_UP_POINT;
	}
	
	@Transient
	public boolean isVotedDown() {
		return this.vote == VOTE_DOWN_POINT;
	}

	@Override
	public String toString() {
		return "QuestionVote [customer=" + customer.getFullName() + ", question=" + question.getId() + ", vote=" + vote + "]";
	}
	
	
}
