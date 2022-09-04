package com.olaaref.mintshop.common.entity.section;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "articles_sections")
public class ArticleSection extends IdBasedEntity{

	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	
	@Column(name = "position")
	private int position;
	
	public ArticleSection() {
	}
	
	public ArticleSection(Integer id) {
		this.id = id;
	}

	public ArticleSection(Section section, Article article, int position) {
		this.section = section;
		this.article = article;
		this.position = position;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "ArticleSection [section=" + section.getId() + ", article=" + article.getId() + ", position=" + position + "]";
	}
	
	
}
