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

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.common.exception.ArticleNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.ArticleService;
import com.olaaref.mintshop.service.UserService;

@Controller
@RequestMapping("/article")
public class ArticleController {
	private final String REDIRECT_URL = "redirect:/article/page/1?sortField=id&sortDir=asc";
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
	public String listArticles() {
		return REDIRECT_URL;
	}
	
	@GetMapping("/page/{pageNum}")
	public String listArticlesByPage(@PathVariable("pageNum") int pageNum, 
									 @PagingAndSortingParam(listName = "articles", moduleUrl = "/article") PagingAndSortingHelper helper,
									 Model model,
									 RedirectAttributes redirectAttributes) {
		articleService.listByPage(pageNum, helper);
		return "articles/list-articles";
	}
	
	@GetMapping("/new")
	public String newArticleForm(Model model) {
		model.addAttribute("article", new Article());
		model.addAttribute("pageTitle", "Add Article");
		return "articles/article-form";
	}
	
	@PostMapping("/save")
	public String saveArticle(@ModelAttribute("article") Article article,
							  Model model,
							  RedirectAttributes redirectAttributes,
							  @AuthenticationPrincipal MintshopUserDetails loggedUser) {
		
		String email = loggedUser.getUsername();
		User user = userService.getUserByEmail(email);
		article.setUser(user);

		articleService.saveArticle(article);
		
		redirectAttributes.addFlashAttribute("message", "Article ("+article.getTitle()+") Saved successfully.");
		return REDIRECT_URL;
	}
	
	@GetMapping("/edit/{articleAlias}")
	public String editArticleForm(@PathVariable("articleAlias") String articleAlias, Model model, RedirectAttributes redirectAttributes) {
		try {
			Article article = articleService.findArticleByAlias(articleAlias);
			model.addAttribute("article", article);
			model.addAttribute("pageTitle", "Edit Article");
			return "articles/article-form";
		} catch (ArticleNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("/details/{articleAlias}")
	public String articleDetailsModal(@PathVariable("articleAlias") String articleAlias, Model model, RedirectAttributes redirectAttributes) {
		try {
			Article article = articleService.findArticleByAlias(articleAlias);
			model.addAttribute("article", article);
			return "articles/article-details-modal";
		} catch (ArticleNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("/delete/{articleAlias}")
	public String deleteArticle(@PathVariable("articleAlias") String articleAlias, Model model, RedirectAttributes redirectAttributes) {
		try {
			Article article = articleService.findArticleByAlias(articleAlias);
			
			articleService.deleteArticle(article);
			
			redirectAttributes.addFlashAttribute("deleteMessag", "Article has been Deleted successfully.");
			return REDIRECT_URL;
		} catch (ArticleNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("/{articleAlias}/published/{status}")
	public String updatePublishedStatus(@PathVariable("status") boolean status,
									    @PathVariable("articleAlias") String articleAlias,
									    RedirectAttributes redirectAttributes) {
		
		try {
			articleService.updatePublishedStatus(articleAlias, status);
			
			String published = (status == true) ? "published" : "unpublished";
			redirectAttributes.addFlashAttribute("publishedMessag", "Article has been "+published+" successfully.");
			return REDIRECT_URL;
		} catch (ArticleNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
}























