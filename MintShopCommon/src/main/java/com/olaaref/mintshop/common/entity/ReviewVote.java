package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "review_votes")
public class ReviewVote extends IdBasedEntity{
	
	private static final int VOTE_UP_POINT = 1;
	private static final int VOTE_DOWN_POINT = -1;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "review_id")
	private Review review;
	
	@Column(name = "votes")
	private int votes;
	

	public ReviewVote() {
	}
	
	public ReviewVote(Integer id) {
		this.id = id;
	}

	public ReviewVote(Customer customer, Review review, int votes) {
		this.customer = customer;
		this.review = review;
		this.votes = votes;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public void voteUp() {
		this.votes = VOTE_UP_POINT;
	}
	
	public void voteDown() {
		this.votes = VOTE_DOWN_POINT;
	}
	
	@Transient
	public boolean isVotedUp() {
		return this.votes == VOTE_UP_POINT;
	}
	
	@Transient
	public boolean isVotedDown(){
		return this.votes == VOTE_DOWN_POINT;
	}

	@Override
	public String toString() {
		return "ReviewVote [customer=" + customer.getFullName() + ", review=" + review.getId() + ", votes=" + votes + "]";
	}
	
	
}
