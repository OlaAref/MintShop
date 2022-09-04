package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.exception.ArticleNotFoundException;
import com.olaaref.mintshop.service.ArticleService;

@Controller
@RequestMapping("/a")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/{articleAlias}")
	public String showArticle(@PathVariable("articleAlias") String articleAlias,
							  Model model) {
		try {
			Article article = articleService.getByAlias(articleAlias);
			model.addAttribute("article", article);
			return "articles/article-page";
		} catch (ArticleNotFoundException e) {
			return "error/404";
		}
	}
}
