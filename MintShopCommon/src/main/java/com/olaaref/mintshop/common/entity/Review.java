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
@Table(name = "reviews")
public class Review extends IdBasedEntity{
	
	@Column(name = "headline", length = 150, nullable = false)
	private String headline;
	
	@Column(name = "comment", length = 500, nullable = false)
	private String comment;
	
	@Column(name = "rating")
	private int rating;
	
	@Column(name = "votes")
	private Integer votes;
	
	@CreationTimestamp
	@Column(name = "created_time")
	private LocalDateTime createdTime;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Transient
	private int upVotes;
	
	@Transient
	private boolean votedUpByCurrentCustomer;
	@Transient
	private boolean votedDownByCurrentCustomer;

	public Review() {
		super();
	}
	
	public Review(Integer id) {
		this.id = id;
	}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public int getUpVotes() {
		if (this.votes > 0) {
			return votes;
		}
		else {
			return 0;
		}
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

	@Override
	public String toString() {
		return "Review [Id = "+id+", headline=" + headline + ", comment=" + comment + ", rating=" + rating + ", createdTime="
				+ createdTime + ", product=" + product.getShortName() + ", customer=" + customer.getFullName() + "]";
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
		Review other = (Review) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
