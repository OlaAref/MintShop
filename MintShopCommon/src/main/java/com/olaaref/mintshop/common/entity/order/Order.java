package com.olaaref.mintshop.common.entity.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.olaaref.mintshop.common.entity.AbstractAddress;
import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Customer;

@Entity
@Table(name = "orders")
public class Order extends AbstractAddress {
	
	@Column(name = "country", length = 45)
	private String country;

	@Column(name = "shipping_cost")
	private float shippingCost;
	
	@Column(name = "product_cost", nullable = false)
	private float productCost;
	
	@Column(name = "subtotal", nullable = false)
	private float subTotal;
	
	@Column(name = "tax", nullable = false)
	private float tax;
	
	@Column(name = "total", nullable = false)
	private float total;
	
	@ManyToOne()
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<OrderDetail>();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 45, nullable = false)
	private OrderStatus orderStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", length = 45, nullable = false)
	private PaymentMethod paymentMethod;
	
	@CreationTimestamp
	@Column(name = "order_time", nullable = false)
	private LocalDateTime orderTime;
	
	@Column(name = "deliver_date")
	private LocalDate deliverDate;
	
	@Column(name = "deliver_days", length = 6)
	private int deliverDays;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();

	public Order() {
		
	}

	public Order(Integer id) {
		this.id = id;
	}

	public Order(Integer id, float productCost, float subTotal, float total, LocalDateTime orderTime) {
		this.id = id;
		this.productCost = productCost;
		this.subTotal = subTotal;
		this.total = total;
		this.orderTime = orderTime;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public LocalDate getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(LocalDate deliverDate) {
		this.deliverDate = deliverDate;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTrack(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", fisrtName=" + firstName + ", lastName=" + lastName + ", addressLine1="
				+ addressLine1 + ", addressLine2=" + addressLine2 + ", phoneNumber=" + phoneNumber + ", country="
				+ country + ", state=" + state + ", city=" + city + ", postalCode=" + postalCode + ", shippingCost="
				+ shippingCost + ", productCost=" + productCost + ", subTotal=" + subTotal + ", tax=" + tax + ", total="
				+ total + ", customer=" + customer.getFullName() + ", orderDetails=" + orderDetails + ", \norderStatus=" + orderStatus
				+ ", paymentMethod=" + paymentMethod + "]";
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCountry(customer.getCountry().getName());
		setState(customer.getState());
		setCity(customer.getCity());
		setPostalCode(customer.getPostalCode());
	}
	
	@Transient
	public String getDestination() {
		String destination = "";
		if(!city.isEmpty()) destination += city;
		if(state != null && !state.isEmpty()) destination += ", " + state;
		if(country != null && !country.isEmpty()) destination += ", " + country;
		return destination;
	}

	public void copyAddressFromAddressClass(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCountry(address.getCountry().getName());
		setState(address.getState());
		setCity(address.getCity());
		setPostalCode(address.getPostalCode());
	}
	
	@Transient
	public String getShippingAddress() {
		String address = firstName;
		if(lastName != null && !lastName.isEmpty()) address += " " + lastName;
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		if(!city.isEmpty()) address += ", " + city;
		if(state != null && !state.isEmpty()) address += ", " + state;
		address += ", " + country;
		if(!postalCode.isEmpty()) address += ".\n Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += ".\n Phone Number: " + phoneNumber;
		
		return address;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		return deliverDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public void setDeliverDateOnForm(String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.deliverDate = LocalDate.parse(date, formatter);
	}
	
	@Transient
	public String getRecipientName() {
		String name = firstName;
		if(lastName != null && !lastName.isEmpty()) name += " " + lastName;
		return name;
	}
	
	@Transient
	public String getRecipientAddress() {
		String address = addressLine1;
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		if(!city.isEmpty()) address += ", " + city;
		if(state != null && !state.isEmpty()) address += ", " + state;
		address += ", " + country;
		if(!postalCode.isEmpty()) address += ".\n " + postalCode;
		
		return address;
	}
	
	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}

	public boolean hasStatus(OrderStatus aStatus) {
		for (OrderTrack track : orderTracks) {
			if(track.getStatus().equals(aStatus)) {
				return true; 
			}
		}
		return false;
	}
	
	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	
	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}
	
	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}
	
	@Transient
	public String getProductNames() {
		//returned as html list
		String productNames = "";
		productNames = "<ul>";
		
		for (OrderDetail detail : orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
		}
		
		productNames += "</ul>";
		
		return productNames;
	}
	
}
