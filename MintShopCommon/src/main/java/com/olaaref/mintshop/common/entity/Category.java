package com.olaaref.mintshop.common.entity;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
	
	@Lob
	@Column(name = "image")
	private byte[] image;

	@Transient
	private String imgBase64;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<Category>();
	
	@Transient
	private boolean hasChildren;
	
	public Category() {
		super();
	}
	
	public Category(int id) {
		this.id = id;
	}
	
	public Category(String name) {
		this.name = name;
	}

	public Category(int id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Category(String name, String alias, boolean enabled) {
		super();
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
	}

	public Category(String name, String alias, boolean enabled, Category parent) {
		super();
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
		copyCategory.setId(id);
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
		
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(category.getName());
		
		return copyCategory;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImgBase64() {
		if(this.image != null) {
			return Base64.getEncoder().encodeToString(this.image);
		}
		return null;
	}

	public void setImgBase64(String imgBase64) {
		this.imgBase64 = imgBase64;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		if(this.getChildren().isEmpty()) {
			return false;
		}
		
		return true;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", alias=" + alias + ", enabled=" + enabled + ", parent="
				+ parent + "]";
	}
	
	
}





















