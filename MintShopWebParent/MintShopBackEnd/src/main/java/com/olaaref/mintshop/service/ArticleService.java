package com.olaaref.mintshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.ArticleType;
import com.olaaref.mintshop.common.exception.ArticleNotFoundException;
import com.olaaref.mintshop.dao.ArticleRepository;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class ArticleService {
	
	public static final int ARTICLES_PER_PAGE = 5;

	@Autowired
	private ArticleRepository articleRepository;
	
	public List<Article> listAllArticles(){
		return articleRepository.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		 helper.listEntities(pageNum, ARTICLES_PER_PAGE, articleRepository);
	}
	
	public String checkUniqueness(Integer articleId, String alias) {
		
		boolean isNew = (articleId == null || articleId == 0);
		Article dbArticle = articleRepository.findByAlias(alias);
		
		if(isNew && dbArticle != null) {
			return "Duplicate";
		}
		if(dbArticle != null && dbArticle.getId() != articleId) {
			return "Duplicate";
		}
		
		return "OK";
	}
	
	public void updatePublishedStatus(String alias, boolean published) throws ArticleNotFoundException {
		Article article = articleRepository.findByAlias(alias);
		if(article == null) throw new ArticleNotFoundException("There is no article with this alias.");
		articleRepository.updatePulishedStatus(article.getId(), published);
	}

	public Article saveArticle(Article article) {
		Article savedArticle = articleRepository.save(article);
		return savedArticle;
	}

	public Article findArticleByAlias(String articleAlias) throws ArticleNotFoundException {
		Article article = articleRepository.findByAlias(articleAlias);
		
		if(article == null) throw new ArticleNotFoundException("There is no article with this alias.");
		
		return article;
	}

	public void deleteArticle(Article article) throws ArticleNotFoundException {
		Long count = articleRepository.countByAlias(article.getAlias());
		if(count == 0) {
			throw new ArticleNotFoundException("There is no article with this alias.");
		}
		articleRepository.delete(article);
	}
	
	public List<Article> getMenuBoundArticles(){
		return articleRepository.findByArticleType(ArticleType.MENU_BOUND);
	}
}
