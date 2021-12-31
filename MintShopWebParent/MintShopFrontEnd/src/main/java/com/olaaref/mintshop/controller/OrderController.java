package com.olaaref.mintshop.controller;

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
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.service.CustomerService;
import com.olaaref.mintshop.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	private static final String redirectUrl = "redirect:/order/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
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
		
		Customer customer = getAuthenticatedCustomer(request);
		Page<Order> ordersPage = orderService.listForCustomerByPage(pageNum, sortField, sortDir, keyword, customer);
		sendPageData(pageNum, ordersPage, sortField, sortDir, keyword, model);
		
		return "orders/list-orders";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utility.getEmailForAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(customerEmail);

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
		
		Customer customer = getAuthenticatedCustomer(request);
		Order order = orderService.getOrder(id, customer);
		
		model.addAttribute("order", order);
		
		return "orders/order-detail-modal";
	}

}
