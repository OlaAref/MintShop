package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.service.ArticleService;

@RestController
@RequestMapping("/articles")
public class ArticleRestController {

	@Autowired
	private ArticleService articleSerivce;
	
	@PostMapping("/check-unique")
	public String checkArticleAliasUnique(@RequestParam(name = "articleId", required = false) Integer articleId,
										  @RequestParam("articleAlias") String articleAlias) {
		return articleSerivce.checkUniqueness(articleId, articleAlias);
	}
	
	
}
