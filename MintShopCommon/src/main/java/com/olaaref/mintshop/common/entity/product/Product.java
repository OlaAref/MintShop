package com.olaaref.mintshop.common.entity.product;

import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.IdBasedEntity;
import com.olaaref.mintshop.common.entity.section.ProductSection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
public class Product extends IdBasedEntity {
	@Column(name = "name", unique = true, length = 256, nullable = false)
	private String name;

	@Column(name = "alias", unique = true, length = 256, nullable = false)
	private String alias;

	@Column(name = "short_description", length = 2560, nullable = false)
	private String shortDescription;

	@Column(name = "full_description", length = 65535, nullable = false, columnDefinition = "mediumtext")
	private String fullDescription;

	@CreationTimestamp
	@Column(name = "created_time")
	private LocalDateTime createdTime;

	@UpdateTimestamp
	@Column(name = "updated_time")
	private LocalDateTime updatedTime;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	@Column(name = "cost")
	private float cost;

	@Column(name = "price")
	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;

	@Column(name = "length")
	private float length;

	@Column(name = "width")
	private float width;

	@Column(name = "height")
	private float height;

	@Column(name = "weight")
	private float weight;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@OneToMany(mappedBy = "product")
	private Set<ProductSection> productsSection;

	@Column(name = "image")
	private String image;
	
	@Column(name = "review_count")
	private int reviewCount;
	
	@Column(name = "average_rating")
	private float averageRating;
	
	@Column(name = "question_count")
	private int questionCount;
	
	@Transient
	private int answeredQuestions;

	@Transient
	private String imagePath;

	@OneToMany(mappedBy = "product", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<ProductDetails> details = new ArrayList<>();
	
	@Transient
	private boolean customerCanReview;
	@Transient
	private boolean reviewedByCustomer;
	
	public Product() {
	}

	public Product(Integer id) {
		this.id = id;
	}
	
	public Product(String name) {
		this.name = name;
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

	public String getShortDescription() {
		return this.shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return this.fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public LocalDateTime getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return this.inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return this.cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return this.discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return this.length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return this.weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
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
		return Constants.GCP_Base_URI + "/product-images/" + this.id + "/" + this.image;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Set<ProductImage> getImages() {
		return this.images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	public void addExtraImage(String extraImage) {
		this.images.add(new ProductImage(extraImage, this));
	}

	public List<ProductDetails> getDetails() {
		return this.details;
	}

	public void setDetails(List<ProductDetails> details) {
		this.details = details;
	}

	public void addProductDetail(String name, String value) {
		this.details.add(new ProductDetails(name, value, this));
	}

	public void addProductDetail(Integer id, String name, String value) {
		this.details.add(new ProductDetails(id, name, value, this));
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}
	
	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public int getAnsweredQuestions() {
		return answeredQuestions;
	}

	public void setAnsweredQuestions(int answeredQuestions) {
		this.answeredQuestions = answeredQuestions;
	}

	public String toString() {
		return "Product [id=" + this.id + ", name=" + this.name + "]";
	}

	public boolean conatinsImage(String imageToCompare) {
		Iterator<ProductImage> iterator = this.images.iterator();
		while (iterator.hasNext()) {
			ProductImage productImage = iterator.next();
			if (productImage.getExtraImage().equals(imageToCompare))
				return true;
		}
		return false;
	}

	@Transient
	public float getDiscountedPrice() {
		if (this.discountPercent > 0.0F)
			return this.price * (100.0F - this.discountPercent) / 100.0F;
		return this.price;
	}

	@Transient
	public String getShortName() {
		if (this.name.length() > 50)
			return this.name.substring(0, 50) + "...";
		return this.name;
	}

	public boolean isCustomerCanReview() {
		return customerCanReview;
	}

	public void setCustomerCanReview(boolean customerCanReview) {
		this.customerCanReview = customerCanReview;
	}

	public boolean isReviewedByCustomer() {
		return reviewedByCustomer;
	}

	public void setReviewedByCustomer(boolean reviewedByCustomer) {
		this.reviewedByCustomer = reviewedByCustomer;
	}

	public Set<ProductSection> getProductsSection() {
		return productsSection;
	}

	public void setProductsSection(Set<ProductSection> productsSection) {
		this.productsSection = productsSection;
	}
	
	public void addProductSection(ProductSection productSection) {
		this.productsSection.add(productSection);
	}
	
	
}
