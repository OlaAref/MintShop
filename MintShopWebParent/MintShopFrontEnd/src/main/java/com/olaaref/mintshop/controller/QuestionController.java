package com.olaaref.mintshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.common.exception.QuestionNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;
import com.olaaref.mintshop.service.QuestionService;
import com.olaaref.mintshop.service.QuestionVoteService;

@Controller
@RequestMapping("/question")
public class QuestionController {
	
	private static final String REDIRECT_URL = "redirect:/question/customer/page/1?sortField=askedTime&sortDir=desc&keyword=";
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionVoteService voteService;
	
	@Autowired
	private HelperController helper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/list")
	public String listFirstPage() {
		return REDIRECT_URL;
	}
	
	@GetMapping("/customer/page/{pageNum}")
	public String listByCustomerByPage(@PathVariable("pageNum") int pageNum,
							 @RequestParam("sortField") String sortField,
							 @RequestParam("sortDir") String sortDir,
							 @RequestParam(value= "keyword", required = false) String keyword,
							 HttpServletRequest request,
							 Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		Page<Question> page = questionService.listByCustomerByPage(pageNum, sortField, sortDir, keyword, customer.getId());
		
		sendPageDataByCustomer(pageNum, sortField, sortDir, keyword, model, page);
		
		return "questions/customer-questions";
		
	}

	private void sendPageDataByCustomer(int pageNum, String sortField, String sortDir, String keyword, Model model, Page<Question> page) {
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long start = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE + 1;
		long end = start + QuestionService.QUESTIONS_PER_PAGE - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();
		
		model.addAttribute("questions", page.getContent());
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("moduleLink", "/question/customer");
	}
	
	@GetMapping("/all/{productAlias}/page/{pageNum}")
	public String listByProductFirstPage(@PathVariable("productAlias") String productAlias,
										 @PathVariable("pageNum") int pageNum,
									     @RequestParam("sortField")String sortField,
									     @RequestParam("sortDir") String sortDir,
										 RedirectAttributes redirectAttributes,
										 Model model,
										 HttpServletRequest request) {
		
		try {
			Product product = productService.getProductByAlias(productAlias);
			Customer customer = helper.getAuthenticatedCustomer(request);
			sendPageDataByProduct(pageNum, sortField, sortDir, product, model, customer);
			
			return "questions/product-questions";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/question/all/page/1?sortField=askedTime&sortDir=desc&keyword=";
		}
	}

	private void sendPageDataByProduct(int pageNum, String sortField, String sortDir, Product product, Model model, Customer customer) {
		
		Page<Question> page = questionService.listByProductByPage(pageNum, sortField, sortDir, product.getId());
		
		if(customer != null) {
			voteService.markQuestionVotedByCustomerForProduct(page.getContent(), product.getId(), customer.getId());
		}
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long start = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE +1;
		long end = start + QuestionService.QUESTIONS_PER_PAGE - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();
		
		model.addAttribute("product", product);
		model.addAttribute("questions", page.getContent());
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("pageTitle", "Questions for " + product.getShortName());
		model.addAttribute("moduleLink", "/question/all/"+product.getAlias());
		
		List<Category> categoryParents = categoryService.getCategoryParents(product.getCategory());
		model.addAttribute("categoryParents", categoryParents);
		
	}
	
	@GetMapping("/details/{id}")
	public String viewQuestionDetails(@PathVariable("id") Integer id, 
									  HttpServletRequest request, 
									  Model model, 
									  RedirectAttributes redirectAttributes) {
		
		try {
			Customer customer = helper.getAuthenticatedCustomer(request);
			Question question = questionService.getByCustomerAndId(customer.getId(), id);
			model.addAttribute("question", question);
			return "questions/question-detail-modal";
			
		} catch (QuestionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
	
}




















