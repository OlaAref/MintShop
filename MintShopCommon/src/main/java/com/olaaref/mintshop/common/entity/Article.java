package com.olaaref.mintshop.common.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.olaaref.mintshop.common.entity.section.ArticleSection;

@Entity
@Table(name="articles")
public class Article extends IdBasedEntity{
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name = "title", length = 256, nullable = false)
	private String title;
	
	@Column(name = "content", length = 65535, nullable = false, columnDefinition = "mediumtext")
	private String content;
	
	@Column(name = "alias", length = 300, nullable = false, unique = true)
	private String alias;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "article_type", nullable = false, length = 12)
	private ArticleType articleType;
	
	@Column(name = "created_time")
	@CreationTimestamp
	private LocalDateTime createdTime;
	
	@Column(name = "updated_time")
	@UpdateTimestamp
	private LocalDateTime updatedTime;
	
	@Column(name = "published")
	private boolean published;
	
	@OneToMany(mappedBy = "article")
	private Set<ArticleSection> articlesSection;

	public Article() {
	}
	
	public Article(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ArticleType getArticleType() {
		return articleType;
	}

	public void setArticleType(ArticleType articleType) {
		this.articleType = articleType;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	@Override
	public String toString() {
		return "Article [user=" + user + ", title=" + title + ", articleType=" + articleType + ", updatedTime="
				+ updatedTime + ", published=" + published + "]";
	}

	public Set<ArticleSection> getArticlesSection() {
		return articlesSection;
	}

	public void setArticlesSection(Set<ArticleSection> articlesSection) {
		this.articlesSection = articlesSection;
	}
	
	public void addArticleSection(ArticleSection articleSection) {
		this.articlesSection.add(articleSection);
	}
}
