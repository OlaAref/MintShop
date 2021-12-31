package com.olaaref.mintshop.common.entity.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.IdBasedEntity;

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
	
	@Lob
	@Column(name = "image")
	private byte[] image;
	
	@Transient
	private String imgBase64;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<ProductImage>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetails> details = new ArrayList<ProductDetails>();
	
	public Product() {
		super();
	}

	public Product(Integer id) {
		super();
		this.id = id;
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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}
	
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImgBase64() {
		return Base64.getEncoder().encodeToString(this.image);
	}

	public void setImgBase64(String imgBase64) {
		this.imgBase64 = imgBase64;
	}

	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}
	
	public void addExtraImage(byte[] extraImage) {
		this.images.add(new ProductImage(extraImage, this));
	}

	public List<ProductDetails> getDetails() {
		return details;
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

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name +"]";
	}

	public boolean conatinsImage(byte[] imageToCompare) {

		Iterator<ProductImage> iterator = images.iterator();
		
		while (iterator.hasNext()) {
			ProductImage productImage = (ProductImage) iterator.next();
			if(Arrays.equals(productImage.getExtraImage(), imageToCompare)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public float getDiscountedPrice() {
		if(this.discountPercent > 0) {
			return this.price * ((100 - this.discountPercent) / 100);
		}
		
		return this.price;
	}
	
	@Transient
	public String getShortName() {
		if(this.name.length() > 50) {
			return this.name.substring(0, 50) + "...";
		}
		return this.name;
	}

	
	
	
}
