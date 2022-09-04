package com.olaaref.mintshop.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_details")
public class ProductDetails extends IdBasedEntity {
	
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	
	@Column(name = "value", nullable = false, length = 255)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductDetails() {
		super();
	}

	public ProductDetails(String name, String value, Product product) {
		super();
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public ProductDetails(Integer id, String name, String value, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductDetails [id=" + id + ", name=" + name + ", value=" + value + ", product=" + product.getName() + "]";
	}
	
	
}
