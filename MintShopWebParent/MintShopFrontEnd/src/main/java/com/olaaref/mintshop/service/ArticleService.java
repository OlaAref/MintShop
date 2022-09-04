package com.olaaref.mintshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.exception.ArticleNotFoundException;
import com.olaaref.mintshop.repository.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	public Article getByAlias(String alias) throws ArticleNotFoundException {
		Article article = articleRepository.findByAlias(alias);
		if(article == null) {
			throw new ArticleNotFoundException("No Article found with alias ("+alias+").");
		}
		return article;
	}
}
