package com.olaaref.mintshop.common.entity;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 45, unique = true)
	private String name;
	
	@Lob
	@Column(name = "logo", length = 128)
	private byte[] logo;
	
	@Transient
	private String logoBase64;
	
	@Transient
	private Set<String> categoriesNames;
	
	@ManyToMany
	@JoinTable(
				name = "brands_categories",
				joinColumns = @JoinColumn(name = "btrand_id"),
				inverseJoinColumns = @JoinColumn(name = "category_id")
				)
	private Set<Category> categories = new HashSet<Category>();

	public Brand() {
		super();
	}
	
	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Brand(String name) {
		super();
		this.name = name;
	}

	public Brand(Integer id, String name, byte[] logo, Set<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getLogoBase64() {
		return Base64.getEncoder().encodeToString(this.logo);
	}

	public void setLogoBase64(String logoBase64) {
		this.logoBase64 = logoBase64;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<String> getCategoriesNames() {
		Set<String> namesList = new HashSet<String>();
		categories.forEach(category -> namesList.add(category.getName()));
		return namesList;
	}

	public void setCategoriesNames(Set<String> categoriesNames) {
		this.categoriesNames = categoriesNames;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + "]";
	}
	
}
