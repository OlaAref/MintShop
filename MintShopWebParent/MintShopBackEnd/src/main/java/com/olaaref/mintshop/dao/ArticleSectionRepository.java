package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.section.ArticleSection;
import com.olaaref.mintshop.common.entity.section.Section;

@Repository
public interface ArticleSectionRepository extends JpaRepository<ArticleSection, Integer> {

	public ArticleSection findFirstByOrderByPositionDesc();
	public ArticleSection findFirstBySectionOrderByPositionDesc(Section section);
	public ArticleSection findBySectionAndArticle(Section section, Article article);
	public List<ArticleSection> findBySectionOrderByPositionAsc(Section section);
	public List<ArticleSection> findBySection(Section section);
	
}
