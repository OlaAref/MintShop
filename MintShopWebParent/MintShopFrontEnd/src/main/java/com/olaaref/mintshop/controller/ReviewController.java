package com.olaaref.mintshop.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.common.exception.ReviewNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;
import com.olaaref.mintshop.service.ReviewService;
import com.olaaref.mintshop.service.ReviewVoteService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private static final String REDIRECT_URL = "redirect:/review/page/1?sortField=createdTime&sortDir=desc&keyword=";
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewVoteService voteService;
	
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
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,
							 @RequestParam("sortField") String sortField,
							 @RequestParam("sortDir") String sortDir,
							 @RequestParam(value= "keyword", required = false) String keyword,
							 HttpServletRequest request,
							 Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		Page<Review> page = reviewService.listByCustomerByPage(pageNum, sortField, sortDir, keyword, customer.getId());
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long start = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		long end = start + ReviewService.REVIEWS_PER_PAGE - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();
		
		model.addAttribute("reviews", page.getContent());
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("moduleLink", "/review");
		
		return "reviews/customer-reviews";
		
	}
	
	@GetMapping("/ratings/all/{productAlias}")
	public String listByProductFirstPage(@PathVariable("productAlias") String productAlias,
										 RedirectAttributes redirectAttributes,
										 Model model,
										 HttpServletRequest request) {
		
		return listByProductByPage("all", productAlias, 1, "createdTime", "desc", redirectAttributes, model, request);
	}
	
	@GetMapping("/ratings/{stars}/{productAlias}/page/{pageNum}")
	public String listByProductByPage(@PathVariable("stars") String stars, 
									  @PathVariable("productAlias") String productAlias,
									  @PathVariable("pageNum") int pageNum,
									  @RequestParam("sortField")String sortField,
									  @RequestParam("sortDir") String sortDir,
									  RedirectAttributes redirectAttributes,
									  Model model,
									  HttpServletRequest request) {
		try {
			Product product = productService.getProductByAlias(productAlias);
			Customer customer = helper.getAuthenticatedCustomer(request);
			sendPageDataByProduct(stars, pageNum, sortField, sortDir, product, model, customer);
			
			return "reviews/reviews-product";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}

	private void sendPageDataByProduct(String stars, int pageNum, String sortField, String sortDir, Product product, Model model, Customer customer) {
		
		Page<Review> page = null;
		
		switch (stars) {
		case "all":
			page = reviewService.listByProductByPage(pageNum, sortField, sortDir, product);
			break;
		
		case "1":
			page = reviewService.listByProductAndStarsByPage(pageNum, sortField, sortDir, product, 1);
			break;
			
		case "2":
			page = reviewService.listByProductAndStarsByPage(pageNum, sortField, sortDir, product, 2);
			break;
			
		case "3":
			page = reviewService.listByProductAndStarsByPage(pageNum, sortField, sortDir, product, 3);
			break;
			
		case "4":
			page = reviewService.listByProductAndStarsByPage(pageNum, sortField, sortDir, product, 4);
			break;
			
		case "5":
			page = reviewService.listByProductAndStarsByPage(pageNum, sortField, sortDir, product, 5);
			break;

		}
		
		if(customer != null) {
			voteService.markReviewsVotedByCustomerForProduct(page.getContent(), product.getId(), customer.getId());
		}
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long start = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE +1;
		long end = start + ReviewService.REVIEWS_PER_PAGE - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();
		
		model.addAttribute("product", product);
		model.addAttribute("reviews", page.getContent());
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("pageTitle", "Reviews for " + product.getShortName());
		model.addAttribute("moduleLink", "/review/ratings/");
		model.addAttribute("stars", stars);
		
		Map<Integer, Integer> percentages = reviewService.getRatingPercentages(product.getId());
		model.addAttribute("percentages", percentages);
		
		List<Category> categoryParents = categoryService.getCategoryParents(product.getCategory());
		model.addAttribute("categoryParents", categoryParents);
		
	}
	
	@GetMapping("/details/{id}")
	public String viewReviewDetails(@PathVariable("id") Integer id, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			Customer customer = helper.getAuthenticatedCustomer(request);
			Review review = reviewService.getByCustomerAndId(customer.getId(), id);
			model.addAttribute("review", review);
			return "reviews/review-detail-modal";
		} catch (ReviewNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
	@GetMapping("/write_review/product/{productId}")
	public String viewReviewForm(@PathVariable("productId") Integer productId, 
								 Model model,
								 HttpServletRequest request) {
		try {
			Review review = new Review();
			Product product = productService.getProductById(productId);
			
			model.addAttribute("product", product);
			model.addAttribute("review", review);
			
			Customer customer = helper.getAuthenticatedCustomer(request);
			boolean isProductReviewed = reviewService.didCustomerReviewProduct(customer, productId);
			
			if(isProductReviewed) {
				//if the product reviewed before
				model.addAttribute("isProductReviewed", isProductReviewed);
			}
			else {
				//if the product not reviewed before, check it customer can review
				boolean canReview = reviewService.canCustomerReviewProduct(customer, productId);
				model.addAttribute("canReview", canReview);
			}
			
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
		return "reviews/review-form";
	}
	
	@PostMapping("/save")
	public String saveReview(@ModelAttribute("review") Review review, 
						     @RequestParam("productId") Integer productId,
						     Model model,
						     HttpServletRequest request) {
		
		try {
			Product product = productService.getProductById(productId);
			Customer customer = helper.getAuthenticatedCustomer(request);
			
			review.setCustomer(customer);
			review.setProduct(product);
			
			Review savedReview = reviewService.save(review);
			model.addAttribute("review", savedReview);
			
			return "reviews/review-saved";
			
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
	}
	
}




















