package com.olaaref.mintshop.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.service.OrderService;
import com.olaaref.mintshop.service.ReviewService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	private static final String redirectUrl = "redirect:/order/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HelperController helper;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/list")
	public String listFirstPage() {
		return redirectUrl;
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,
							 HttpServletRequest request,
							 @RequestParam("sortField") String sortField,
							 @RequestParam("sortDir") String sortDir,
							 @RequestParam(name = "keyword", required = false) String keyword,
							 Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		Page<Order> ordersPage = orderService.listForCustomerByPage(pageNum, sortField, sortDir, keyword, customer);
		sendPageData(pageNum, ordersPage, sortField, sortDir, keyword, model);
		
		return "orders/list-orders";
	}

	public void sendPageData(int pageNum, Page<Order> page, String sortField, String sortDir, String keyword, Model model) {
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long start = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long end = start + OrderService.ORDERS_PER_PAGE - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();
		
		model.addAttribute("orders", page.getContent());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleLink", "/order");
		
	}
	
	@GetMapping("/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id,
									HttpServletRequest request,
									Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		Order order = orderService.getOrder(id, customer);
		
		setProductReviewableStatus(customer, order);
		
		model.addAttribute("order", order);
		
		return "orders/order-detail-modal";
	}

	private void setProductReviewableStatus(Customer customer, Order order) {
		
		Iterator<OrderDetail> details = order.getOrderDetails().iterator();
		
		while(details.hasNext()) {
			OrderDetail detail = details.next();
			Product product = detail.getProduct();
			Integer productId = product.getId();
			
			//check if the customer reviewed the product before
			boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, productId);
			product.setReviewedByCustomer(didCustomerReviewProduct);
			
			//if the customer not reviewed the product, check if he/she can review the product
			if(!didCustomerReviewProduct) {
				boolean canReview = reviewService.canCustomerReviewProduct(customer, productId);
				product.setCustomerCanReview(canReview);
			}
		}
		
	}

}
