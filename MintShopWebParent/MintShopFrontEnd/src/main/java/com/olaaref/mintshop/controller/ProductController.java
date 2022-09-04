package com.olaaref.mintshop.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;
import com.olaaref.mintshop.service.QuestionService;
import com.olaaref.mintshop.service.QuestionVoteService;
import com.olaaref.mintshop.service.ReviewService;
import com.olaaref.mintshop.service.ReviewVoteService;

@Controller
public class ProductController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewVoteService reviewVoteService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionVoteService questionVoteService;
	
	@Autowired
	private HelperController helper;
	
	
	@GetMapping("/p/{product-alias}")
	public String viewProductDetail(@PathVariable("product-alias") String productAlias, 
									HttpServletRequest request,
									Model model) {
		
		try {
			Product product = productService.getProductByAlias(productAlias);
			List<Category> categoryParents = categoryService.getCategoryParents(product.getCategory());
			
			Page<Review> reviews = reviewService.listTopThreeReviewsByProduct(product);
			Map<Integer, Integer> percentages = reviewService.getRatingPercentages(product.getId());
			
			Page<Question> questions = questionService.listTopThreeQuestionsByProduct(product.getId());
			questionService.countAnsweredQuestiosForProduct(product.getId());
			
			Customer customer = helper.getAuthenticatedCustomer(request);
			if (customer != null) {
				reviewVoteService.markReviewsVotedByCustomerForProduct(reviews.getContent(), product.getId(), customer.getId());
				questionVoteService.markQuestionVotedByCustomerForProduct(questions.getContent(), product.getId(), customer.getId());
				boolean isProductReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
				if (isProductReviewed) {
					//if reviewed before
					model.addAttribute("isProductReviewed", isProductReviewed);
				} else {
					//if not reviewed
					boolean canReview = reviewService.canCustomerReviewProduct(customer, product.getId());
					model.addAttribute("canReview", canReview);
				} 
			}
			
			
			
			model.addAttribute("categoryParents", categoryParents);
			model.addAttribute("product", product);
			model.addAttribute("productName", product.getShortName());
			model.addAttribute("reviews", reviews);
			model.addAttribute("percentages", percentages);
			model.addAttribute("questions", questions.getContent());
			
			return "product/product-details";
			
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	

	@GetMapping("/search")
	public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		
		return searchByPage(1, keyword, model);
			
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@PathVariable("pageNum") int pageNum,
						 @RequestParam("keyword") String keyword, 
						 Model model) {
		
		Page<Product> productsPage = productService.search(keyword, pageNum);
		
		long start = (pageNum - 1) * ProductService.SEARCH_RESULT_PER_PAGE + 1;
		long end = start + ProductService.SEARCH_RESULT_PER_PAGE - 1;
		if(end > productsPage.getTotalElements()) end = productsPage.getTotalElements();
			
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", productsPage.getTotalElements());
		model.addAttribute("totalPages", productsPage.getTotalPages());
		model.addAttribute("keyword", keyword);
		
		return "product/search-result";
			
	}
}














