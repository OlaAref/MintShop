package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Question;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.common.exception.QuestionNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.QuestionService;
import com.olaaref.mintshop.service.UserService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	private final String REDIRECT_URL = "redirect:/question/page/1?sortField=askedTime&sortDir=asc";
	
	@GetMapping("/list")
	public String listAllQuestion() {
		return REDIRECT_URL;
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,
							@PagingAndSortingParam(listName = "questions", moduleUrl = "/question") PagingAndSortingHelper helper,
							Model model) {
		questionService.listQuestionsByPage(pageNum, helper);
		return "questions/list-questions";
	}
	
	@GetMapping("/details/{id}")
	public String viewQuestionDetails(@PathVariable("id") Integer id,
									  Model model,
									  RedirectAttributes redirectAttributes) {
		try {
			Question question = questionService.getQuestionById(id);
			model.addAttribute("question", question);
			return "questions/question-detail-modal";
		} catch (QuestionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("/edit/{id}")
	public String viewEditQuestionForm(@PathVariable("id") Integer id,
									  Model model,
									  RedirectAttributes redirectAttributes) {
		try {
			Question question = questionService.getQuestionById(id);
			model.addAttribute("question", question);
			return "questions/question-form";
		} catch (QuestionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@PostMapping("/save")
	public String saveQuestion(@ModelAttribute("question") Question question, 
							   @AuthenticationPrincipal MintshopUserDetails loggedUser,
							   RedirectAttributes redirectAttributes) throws QuestionNotFoundException {
		//set the current user as the answerer
		String email = loggedUser.getUsername();
		User user = userService.getUserByEmail(email);
		question.setAnswerer(user);
		
		questionService.save(question);
		
		redirectAttributes.addFlashAttribute("message", "with id "+question.getId());
		return REDIRECT_URL;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteQuestion(@PathVariable("id") Integer id,
							  	 RedirectAttributes redirectAttributes) {
		
		try {
			questionService.deleteQuestion(id);
			redirectAttributes.addFlashAttribute("deleteMessag", "Question with id : "+id+" has been deleted successfully.");
			return REDIRECT_URL;
		} catch (QuestionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("/{id}/approved/{status}")
	public String updateApprovalStatus(@PathVariable("id") Integer id,
									   @PathVariable("status") boolean status,
									   RedirectAttributes redirectAttributes) {
		questionService.updateApprovalStatus(id, status);
		redirectAttributes.addFlashAttribute("message", "Approval Status updated successfully.");
		return REDIRECT_URL;
	}
	
}
























