package com.olaaref.mintshop.common.entity.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "order_track")
public class OrderTrack extends IdBasedEntity {

	@Column(name = "notes", length = 256)
	private String notes;
	
	@CreationTimestamp
	@Column(name = "updated_time")
	private LocalDateTime updatedTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 45, nullable = false)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public OrderTrack() {
		super();
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderTrack [notes=" + notes + ", updatedTime=" + updatedTime + ", status=" + status + ", order=" + order.getId()
				+ "]";
	}
	
	@Transient
	public String getUpdatedTimeOnForm() {
		return this.updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
	}
	
	public void setUpdatedTimeOnForm(String dateString) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
		try {
			this.updatedTime = LocalDateTime.parse(dateString, formatter);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
}
