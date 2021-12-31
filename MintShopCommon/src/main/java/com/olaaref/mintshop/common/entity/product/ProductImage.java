package com.olaaref.mintshop.common.entity.product;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity {
	
	@Lob
	@Column(name = "extra_image", nullable = false)
	private byte[] extraImage;
	
	@Transient
	private String extraImgBase64;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductImage() {
		super();
	}

	public ProductImage(Integer id, byte[] extraImage, Product product) {
		super();
		this.id = id;
		this.extraImage = extraImage;
		this.product = product;
	}

	public ProductImage(byte[] extraImage, Product product) {
		super();
		this.extraImage = extraImage;
		this.product = product;
	}

	public byte[] getExtraImage() {
		return extraImage;
	}

	public void setExtraImage(byte[] extraImage) {
		this.extraImage = extraImage;
	}

	public String getExtraImgBase64() {
		return Base64.getEncoder().encodeToString(this.extraImage);
	}

	public void setExtraImgBase64(String extraImgBase64) {
		this.extraImgBase64 = extraImgBase64;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductImage [id=" + id + ", extraImage=" + extraImage.length + ", product=" + product + "]";
	}
	
	
}
