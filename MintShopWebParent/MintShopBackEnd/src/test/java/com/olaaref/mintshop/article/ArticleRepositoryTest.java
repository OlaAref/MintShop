package com.olaaref.mintshop.article;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.ArticleType;
import com.olaaref.mintshop.dao.ArticleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Test
	public void createArticleTableTest() {
		
	}
	
	@Test
	public void getMenuBoundArticlesTest() {
		List<Article> articles = articleRepository.findByArticleType(ArticleType.MENU_BOUND);
		articles.forEach(System.out::println);
	}
}
