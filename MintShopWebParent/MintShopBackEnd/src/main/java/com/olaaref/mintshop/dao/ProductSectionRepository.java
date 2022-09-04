package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.entity.section.ProductSection;
import com.olaaref.mintshop.common.entity.section.Section;


@Repository
public interface ProductSectionRepository extends JpaRepository<ProductSection, Integer> {
	
	public ProductSection findFirstByOrderByPositionDesc();
	public ProductSection findFirstBySectionOrderByPositionDesc(Section section);
	
	public ProductSection findBySectionAndProduct(Section section, Product product);
	public List<ProductSection> findBySectionOrderByPositionAsc(Section section);
	public List<ProductSection> findBySection(Section section);
}
