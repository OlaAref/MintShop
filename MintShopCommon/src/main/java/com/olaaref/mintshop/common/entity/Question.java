package com.olaaref.mintshop.common.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.olaaref.mintshop.common.entity.product.Product;

@Entity
@Table(name = "questions")
public class Question extends IdBasedEntity{
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "asker_id")
	private Customer asker;
	
	@ManyToOne
	@JoinColumn(name = "answerer_id")
	private User answerer;
	
	@Column(name = "question_content", length = 400, nullable = false)
	private String questionContent;
	
	@Column(name = "answer_content", length = 1000, nullable = true)
	private String answerContent;
	
	@CreationTimestamp
	@Column(name = "asked_time")
	private LocalDateTime askedTime;
	
	@Column(name = "answered_time")
	private LocalDateTime answeredTime;
	
	@Column(name = "approval_status")
	private boolean approvalStatus;
	
	@Column(name = "votes")
	private int votes;
	
	@Transient
	private boolean votedUpByCurrentCustomer;
	@Transient
	private boolean votedDownByCurrentCustomer;
	
	@Transient
	private boolean answered;
	
	
	public Question() {
	}
	
	public Question(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getAsker() {
		return asker;
	}

	public void setAsker(Customer asker) {
		this.asker = asker;
	}

	public User getAnswerer() {
		return answerer;
	}

	public void setAnswerer(User answerer) {
		this.answerer = answerer;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public LocalDateTime getAskedTime() {
		return askedTime;
	}

	public void setAskedTime(LocalDateTime askedTime) {
		this.askedTime = askedTime;
	}

	public LocalDateTime getAnsweredTime() {
		return answeredTime;
	}

	public void setAnsweredTime(LocalDateTime answeredTime) {
		this.answeredTime = answeredTime;
	}

	public boolean isApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public boolean isVotedUpByCurrentCustomer() {
		return votedUpByCurrentCustomer;
	}

	public void setVotedUpByCurrentCustomer(boolean votedUpByCurrentCustomer) {
		this.votedUpByCurrentCustomer = votedUpByCurrentCustomer;
	}

	public boolean isVotedDownByCurrentCustomer() {
		return votedDownByCurrentCustomer;
	}

	public void setVotedDownByCurrentCustomer(boolean votedDownByCurrentCustomer) {
		this.votedDownByCurrentCustomer = votedDownByCurrentCustomer;
	}

	public boolean isAnswered() {
		if(this.answerContent != null) {
			if(!this.answerContent.trim().equals("")) {
				return true;
			}
		}
		return false;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "Question [product=" + product.getId() 
				+ ", asker=" + (asker != null ? asker.getFullName() : "No Asker") 
				+ ", answerer=" + (answerer != null ? answerer.getFullName() : "No Answerer") + ", questionContent="
				+ questionContent + ", answerContent=" + answerContent + ", approvalStatus=" + approvalStatus + ", votes=" + votes
				+ "]";
	}
	
	
}
