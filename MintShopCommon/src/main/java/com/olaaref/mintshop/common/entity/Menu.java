package com.olaaref.mintshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "menus")
public class Menu extends IdBasedEntity{
	
	@Column(name = "title", length = 256, nullable = false)
	private String title;
	
	@Column(name = "alias", length = 300, nullable = false, unique = true)
	private String alias;
	
	@OneToOne
	@JoinColumn(name = "article_id")
	private Article article;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "menu_type", length = 7, nullable = false)
	private MenuType menuType;
	
	@Column(name = "position")
	private int position;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	public Menu() {
		
	}
	
	public Menu(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public MenuType getMenuType() {
		return menuType;
	}

	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Transient
	public boolean isHeader() {
		return this.menuType.equals(MenuType.HEADER);
	}
	
	@Transient
	public boolean isFooter() {
		return this.menuType.equals(MenuType.FOOTER);
	}

	@Override
	public String toString() {
		return "Menu [title=" + title + ", article=" + article.getId() + ", menuType=" + menuType + ", position=" + position
				+ ", enabled=" + enabled + "]";
	}
	
	
	
}
