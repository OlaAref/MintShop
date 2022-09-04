package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Review;
import com.olaaref.mintshop.common.exception.ReviewNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	private final String REDIRECT_URL = "redirect:/review/page/1?sortField=createdTime&sortDir=desc";
	
	@GetMapping("/list")
	public String listAllReviews(Model model) {
		return REDIRECT_URL;
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, 
							 @PagingAndSortingParam(listName = "reviews", moduleUrl = "/review") PagingAndSortingHelper helper, 
							 Model model) {
		reviewService.listReviewsByPage(pageNum, helper);
		return "reviews/list-reviews";
	}
	
	@GetMapping("/details/{id}")
	public String viewReviewDetails(@PathVariable("id") int id, 
							 		Model model,
							 		RedirectAttributes redirectAttributes) {
		try {
			Review review = reviewService.getReviewById(id);
			model.addAttribute("review", review);
			return "reviews/review-detail-modal";
		} catch (ReviewNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
	@PostMapping("/save")
	public String saveReview(@ModelAttribute("review") Review review, RedirectAttributes redirectAttributes) throws ReviewNotFoundException {
		reviewService.save(review);
		redirectAttributes.addFlashAttribute("message", "with id "+review.getId());
		return REDIRECT_URL;
	}
	
	@GetMapping("/edit/{id}")
	public String editReviewDetails(@PathVariable("id") int id, 
							 		Model model,
							 		RedirectAttributes redirectAttributes) {
		try {
			Review review = reviewService.getReviewById(id);
			model.addAttribute("review", review);
			return "reviews/review-form";
		} catch (ReviewNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
	@GetMapping("/delete/{id}")
	public String deletereview(@PathVariable("id") int id, 
							   RedirectAttributes redirectAttributes) {
		try {
			reviewService.deleteReview(id);
			redirectAttributes.addFlashAttribute("deleteMessag", "Review with id : "+id+" has been deleted successfully.");
			return REDIRECT_URL;
		} catch (ReviewNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
}
