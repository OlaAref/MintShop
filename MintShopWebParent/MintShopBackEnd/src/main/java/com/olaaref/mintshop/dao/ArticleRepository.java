package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.ArticleType;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface ArticleRepository extends EntityPagingRepository<Article, Integer> {
	
	@Query("SELECT a FROM Article a WHERE (a.title LIKE %?1% OR a.content LIKE %?1%)")
	public Page<Article> findAll(String keyword, Pageable pageable);

	public Article findByAlias(String alias);

	@Query("UPDATE Article a SET a.published = ?2 WHERE a.id = ?1")
	@Modifying
	public void updatePulishedStatus(Integer articleId, boolean published);
	
	public Long countByAlias(String alias);

	public List<Article> findByArticleType(ArticleType menuBound);

	public long countByArticleType(ArticleType type);

	public long countByPublished(boolean status);

	
}
