package com.olaaref.mintshop.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.controller.HelperController;
import com.olaaref.mintshop.service.ProductService;
import com.olaaref.mintshop.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionRestController {
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private HelperController helper;
	
	@PostMapping("/save")
	public String saveQuestion(@RequestParam("productId") Integer productId,
							   @RequestParam("questionContent") String questionContent,
						       Model model,
						       HttpServletRequest request) {
		
		try {
			Product product = productService.getProductById(productId);
			Customer customer = helper.getAuthenticatedCustomer(request);
			
			Question question = new Question();
			question.setProduct(product);
			question.setAsker(customer);
			question.setQuestionContent(questionContent);
			
			Question savedQuestion = questionService.save(question);
			model.addAttribute("question", savedQuestion);
			
			return "Your Question has been posted and waiting for approval.";
			
		} catch (ProductNotFoundException e) {
			return "Error happened while we try to send your question.";
		}
		
	}
}
