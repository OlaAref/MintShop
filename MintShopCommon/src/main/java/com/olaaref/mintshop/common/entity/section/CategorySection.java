package com.olaaref.mintshop.common.entity.section;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "categories_sections")
public class CategorySection extends IdBasedEntity{

	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Column(name = "position", nullable = false)
	private int position;
	
	public CategorySection() {
	}
	
	public CategorySection(Integer id) {
		this.id = id;
	}

	public CategorySection(Section section, Category category, int position) {
		this.section = section;
		this.category = category;
		this.position = position;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "CategorySection [ID= " + id + " section=" + section.getId() + ", category=" + category.getId() + ", position=" + position + "]";
	}
	
	
}
