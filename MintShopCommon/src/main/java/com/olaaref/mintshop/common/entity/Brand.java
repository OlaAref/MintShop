package com.olaaref.mintshop.common.entity;

import com.olaaref.mintshop.common.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand extends IdBasedEntity {
	@Column(name = "name", nullable = false, length = 45, unique = true)
	private String name;

	@Column(name = "logo", length = 128)
	private String logo;

	@Transient
	private String logoPath;

	@Transient
	private Set<String> categoriesNames;

	@ManyToMany
	@JoinTable(name = "brands_categories", joinColumns = { @JoinColumn(name = "btrand_id") }, inverseJoinColumns = {
			@JoinColumn(name = "category_id") })
	private Set<Category> categories = new HashSet<>();

	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Brand(String name) {
		this.name = name;
	}

	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoPath() {
		if (this.id == null || this.logo == null)
			return "/img/no-image.gif";
		return Constants.S3_BASE_URI + "/brand-logos/" + this.id + "/" + this.logo;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<String> getCategoriesNames() {
		Set<String> namesList = new HashSet<>();
		this.categories.forEach(category -> namesList.add(category.getName()));
		return namesList;
	}

	public void setCategoriesNames(Set<String> categoriesNames) {
		this.categoriesNames = categoriesNames;
	}

	public String toString() {
		return "Brand [id=" + this.id + ", name=" + this.name + "]";
	}

	public Brand() {
	}
}
