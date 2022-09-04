package com.olaaref.mintshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	public Article findByAlias(String alias);
}
