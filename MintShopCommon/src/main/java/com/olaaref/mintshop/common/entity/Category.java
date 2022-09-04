package com.olaaref.mintshop.common.entity;

import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.common.entity.section.CategorySection;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category extends IdBasedEntity {
	@Column(name = "name", length = 128, nullable = false, unique = true)
	private String name;

	@Column(name = "alias", length = 64, nullable = false, unique = true)
	private String alias;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;

	@Column(name = "image")
	private String image;

	@Transient
	private String imagePath;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();
	
	@OneToMany(mappedBy = "category")
	private Set<CategorySection> categoriesSection;

	@Transient
	private boolean hasChildren;

	public Category() {
	}

	public Category(int id) {
		this.id = Integer.valueOf(id);
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(int id, String name, String alias) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.alias = alias;
	}

	public Category(String name, String alias, boolean enabled) {
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
	}

	public Category(String name, String alias, boolean enabled, Category parent) {
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
		this.parent = parent;
	}

	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}

	public static Category copyIdAndName(int id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(Integer.valueOf(id));
		copyCategory.setName(name);
		return copyCategory;
	}

	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setImage(category.getImage());
		return copyCategory;
	}

	public static Category copyFull(Category category, String name) {
		Category copyCategory = copyFull(category);
		copyCategory.setName(category.getName());
		return copyCategory;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAllParentIDs() {
		return this.allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImagePath() {
		if (this.id == null || this.image == null)
			return "/img/no-image.gif";
		return Constants.GCP_Base_URI + "/category-images/" + this.id + "/" + this.image;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Category getParent() {
		return this.parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		if (getChildren().isEmpty())
			return false;
		return true;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String toString() {
		return "Category [id=" + this.id + ", name=" + this.name + ", alias=" + this.alias + ", enabled=" + this.enabled
				+ ", parent=" + ((this.parent==null) ? "no parent" : this.parent.name) + "]";
	}

	public Set<CategorySection> getCategoriesSection() {
		return categoriesSection;
	}

	public void setCategoriesSection(Set<CategorySection> categoriesSection) {
		this.categoriesSection = categoriesSection;
	}
	
	public void addCategorySection(CategorySection categorySection) {
		this.categoriesSection.add(categorySection);
	}
}
