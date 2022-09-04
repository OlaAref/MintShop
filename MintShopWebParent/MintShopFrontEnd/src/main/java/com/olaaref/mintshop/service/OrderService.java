package com.olaaref.mintshop.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.checkout.CheckoutInfo;
import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.common.entity.order.OrderTrack;
import com.olaaref.mintshop.common.entity.order.PaymentMethod;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.OrderNotFoundException;
import com.olaaref.mintshop.order.OrderReturnRequest;
import com.olaaref.mintshop.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
	public static final int ORDERS_PER_PAGE = 5;

	@Autowired
	private OrderRepository orderRepository;
	
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, 
							CheckoutInfo checkoutInfo, PaymentMethod paymentMethod) {
		
		Order order = new Order();
		
		order.setPaymentMethod(paymentMethod);
		order.setCustomer(customer);
		order.setProductCost(checkoutInfo.getProductCost());
		order.setSubTotal(checkoutInfo.getProductTotal());
		order.setShippingCost(checkoutInfo.getShippingCostTotal());
		order.setTax(0.0f);
		order.setTotal(checkoutInfo.getPaymentTotal());
		order.setDeliverDate(checkoutInfo.getDeliverDate());
		order.setDeliverDays(checkoutInfo.getDeliverDays());
		
		if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
			order.setOrderStatus(OrderStatus.PAID);
		}
		else {
			order.setOrderStatus(OrderStatus.NEW);
		}
		
		if(address == null) {
			order.copyAddressFromCustomer();
		}
		else {
			order.copyAddressFromAddressClass(address);
		}
		
		
		Set<OrderDetail> details = order.getOrderDetails();
		
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			OrderDetail detail = new OrderDetail();
			
			detail.setOrder(order);
			detail.setProduct(product);
			detail.setProductCost(product.getCost() * item.getQuantity());
			detail.setUnitPrice(product.getDiscountedPrice());
			detail.setQuantity(item.getQuantity());
			detail.setShippingCost(item.getShippingCost());
			detail.setSubTotal(item.getSubTotal());
			
			details.add(detail);
		}
		
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setStatus(OrderStatus.NEW);
		track.setNotes(OrderStatus.NEW.defaultDescription());
		
		order.getOrderTracks().add(track);
		
		return orderRepository.save(order);
	}
	
	public Page<Order> listForCustomerByPage(int pageNum, String sortField, String sortDir, String keyword, Customer customer) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;
		
		if(keyword != null) {
			page = orderRepository.findAll(keyword, customer.getId(), pageable);
		}
		else {
			page = orderRepository.findAll(customer.getId(), pageable);
		}
		
		return page;
	}
	
	public Order getOrder(Integer orderId, Customer customer) {
		return orderRepository.findByIdAndCustomer(orderId, customer);
	}
	
	public void setOrderReturnRequest(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
		
		Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
		
		if(order == null) {
			throw new OrderNotFoundException("There is no order with ID "+ request.getOrderId());
		}
		
		//check if the order is returned
		if(order.isReturnRequested()) return;
		
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setStatus(OrderStatus.RETURN_REQUESTED);
		
		String notes = "Reason : " + request.getReason();
		if(!request.getNote().equals("")) {
			notes += ". " + request.getNote();
		}
		track.setNotes(notes);
		
		order.getOrderTracks().add(track);
		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		
		orderRepository.save(order);
	}
}




















