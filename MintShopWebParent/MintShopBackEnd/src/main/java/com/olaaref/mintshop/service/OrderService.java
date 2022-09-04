package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.common.entity.order.OrderTrack;
import com.olaaref.mintshop.common.exception.OrderNotFoundException;
import com.olaaref.mintshop.dao.CountryRepository;
import com.olaaref.mintshop.dao.OrderRepository;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class OrderService {
	
	public static final int ORDERS_PER_PAGE = 10;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		
		if("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		}
		else {
			sort = Sort.by(sortField);
		}
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;
		
		if(keyword != null) {
			page = orderRepository.findAll(keyword, pageable);
		}
		else {
			page = orderRepository.findAll(pageable);
			
		}
		
		
		helper.updateModelAttributes(pageNum, page);
	}
	
	public Order getById(Integer id) throws OrderNotFoundException {
		
		try {
			return orderRepository.findById(id).get();
			
		} catch (NoSuchElementException e) {
			throw new OrderNotFoundException("There is no order found with ID : " + id);
		}
	}
	
	public void deleteOrder(Integer id) throws OrderNotFoundException {
		Long count = orderRepository.countById(id);
		if(count == null || count == 0) {
			throw new OrderNotFoundException("There is no order found with ID : " + id);
		}
		orderRepository.deleteById(id);
	}
	
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}

	public void save(Order formOrder) {
		Order dbOrder = orderRepository.getById(formOrder.getId());
		
		formOrder.setOrderTime(dbOrder.getOrderTime());
		
		formOrder.setCustomer(dbOrder.getCustomer());
		
		orderRepository.save(formOrder);
		
	}
	
	public void updateStatus(Integer orderId, String status) {
		Order dbOrder = orderRepository.findById(orderId).get();
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);
		
		if(!dbOrder.hasStatus(statusToUpdate)) {
			List<OrderTrack> orderTracks = dbOrder.getOrderTracks();
			
			OrderTrack track = new OrderTrack();
			track.setStatus(statusToUpdate);
			track.setNotes(statusToUpdate.defaultDescription());
			track.setOrder(dbOrder);
			
			orderTracks.add(track);
			
			dbOrder.setOrderStatus(statusToUpdate);
			
			orderRepository.save(dbOrder);
		}
	}
}








































