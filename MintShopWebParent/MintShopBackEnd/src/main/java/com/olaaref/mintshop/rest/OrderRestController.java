package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderRestController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/shipper/update/{id}/{status}")
	public Response updateOrderStatus(@PathVariable("id") Integer id,
								  @PathVariable("status") String status) {
		
		orderService.updateStatus(id, status);
		return new Response(id, status);
	}
	

}

class Response{
	private Integer orderId;
	private String status;
	
	public Response(Integer orderId, String status) {
		this.orderId = orderId;
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}