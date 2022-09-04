package com.olaaref.mintshop.common.entity.section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.IdBasedEntity;

@Entity
@Table(name = "brands_sections")
public class BrandSection extends IdBasedEntity{

	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@Column(name = "position")
	private int position;
	
	public BrandSection() {
	}
	
	public BrandSection(Integer id) {
		this.id = id;
	}

	public BrandSection(Section section, Brand brand, int position) {
		this.section = section;
		this.brand = brand;
		this.position = position;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "BrandSection [section=" + section.getId() + ", brand=" + brand.getId() + ", position=" + position + "]";
	}
	
	
}
