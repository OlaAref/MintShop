package com.olaaref.mintshop.common.entity.section;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.IdBasedEntity;
import com.olaaref.mintshop.common.entity.product.Product;

@Entity
@Table(name = "products_sections")
public class ProductSection extends IdBasedEntity{
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(name = "position")
	private int position;

	public ProductSection() {
	}
	
	public ProductSection(Integer id) {
		this.id = id;
	}

	
	public ProductSection(Section section, Product product, int position) {
		this.section = section;
		this.product = product;
		this.position = position;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "ProductSection [section=" + section.getId() + ", product=" + product.getId() + ", position=" + position + "]";
	}
	
	
}
