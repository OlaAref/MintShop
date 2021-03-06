package com.olaaref.mintshop.common.entity.product;

import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.common.entity.IdBasedEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity {
	@Column(name = "extra_image", nullable = false)
	private String extraImage;

	@Transient
	private String extraImagePath;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductImage() {
	}

	public ProductImage(Integer id, String extraImage, Product product) {
		this.id = id;
		this.extraImage = extraImage;
		this.product = product;
	}

	public ProductImage(String extraImage, Product product) {
		this.extraImage = extraImage;
		this.product = product;
	}

	public String getExtraImage() {
		return this.extraImage;
	}

	public void setExtraImage(String extraImage) {
		this.extraImage = extraImage;
	}

	public String getExtraImagePath() {
		if (this.id == null || this.extraImage == null)
			return "/img/no-image.gif";
		return Constants.S3_BASE_URI + "/product-images/" + this.product.getId() + "/extras/" + this.extraImage;
	}

	public void setExtraImagePath(String extraImagePath) {
		this.extraImagePath = extraImagePath;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String toString() {
		return "ProductImage [id=" + this.id + ", extraImage=" + this.extraImage + ", product=" + this.product + "]";
	}
}
