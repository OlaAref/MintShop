package com.olaaref.mintshop.common.entity.section;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.IdBasedEntity;
import com.olaaref.mintshop.common.entity.product.Product;

@Entity
@Table(name = "sections")
public class Section extends IdBasedEntity{
	
	@Column(name = "heading", nullable = false, length = 128)
	private String heading;
	
	@Column(name = "description", nullable = true, columnDefinition = "text")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "section_type", length = 14)
	private SectionType sectionType;
	
	@Column(name = "position", nullable = false)
	private int position;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private List<ProductSection> productsSection;
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private List<CategorySection> categoriesSection;
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private List<BrandSection> brandsSection;
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
	private List<ArticleSection> articlesSection;
	
	@Transient
	private List<Category> categories = new ArrayList<>();
	
	@Transient
	private List<Brand> brands = new ArrayList<>();
	
	@Transient
	private List<Article> articles = new ArrayList<>();
	
	@Transient
	private List<Product> products = new ArrayList<>();
	
	public Section() {
	}
	
	public Section(Integer id) {
		this.id = id;
	}
	
	public Section(boolean enabled, SectionType sectionType) {
		this.enabled = enabled;
		this.sectionType = sectionType;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SectionType getSectionType() {
		return sectionType;
	}

	public void setSectionType(SectionType sectionType) {
		this.sectionType = sectionType;
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

	public List<ProductSection> getProductsSection() {
		if(this.productsSection != null) {
			Comparator<ProductSection> byPosition = Comparator.comparing(ProductSection::getPosition);
			this.productsSection.sort(byPosition);
		}
		return productsSection;
	}

	public void setProductsSection(List<ProductSection> productsSection) {
		this.productsSection = productsSection;
	}
	
	public void addProductSection(ProductSection productSection) {
		this.productsSection.add(productSection);
	}

	public List<CategorySection> getCategoriesSection() {
		if(this.categoriesSection != null) {
			Comparator<CategorySection> byPosition = Comparator.comparing(CategorySection::getPosition);
			this.categoriesSection.sort(byPosition);
		}
		return categoriesSection;
	}

	public void setCategoriesSection(List<CategorySection> categoriesSection) {
		this.categoriesSection = categoriesSection;
	}
	
	public void addCategorySection(CategorySection categorySection) {
		this.categoriesSection.add(categorySection);
	}

	public List<BrandSection> getBrandsSection() {
		if(this.brandsSection != null) {
			Comparator<BrandSection> byPosition = Comparator.comparing(BrandSection::getPosition);
			this.brandsSection.sort(byPosition);
		}
		return brandsSection;
	}

	public void setBrandsSection(List<BrandSection> brandsSection) {
		this.brandsSection = brandsSection;
	}
	
	public void addBrandSection(BrandSection brandSection) {
		this.brandsSection.add(brandSection);
	}

	public List<ArticleSection> getArticlesSection() {
		if(this.articlesSection != null) {
			Comparator<ArticleSection> byPosition = Comparator.comparing(ArticleSection::getPosition);
			this.articlesSection.sort(byPosition);
		}
		return articlesSection;
	}

	public void setArticlesSection(List<ArticleSection> articlesSection) {
		this.articlesSection = articlesSection;
	}
	
	public void addArticleSection(ArticleSection articleSection) {
		this.articlesSection.add(articleSection);
	}


	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Section [heading=" + heading + ", description=" + description + ", sectionType=" + sectionType
				+ ", position=" + position + ", enabled=" + enabled + "]";
	}
	
	
}
