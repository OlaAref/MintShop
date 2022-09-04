package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.section.BrandSection;
import com.olaaref.mintshop.common.entity.section.Section;

@Repository
public interface BrandSectionRepository extends JpaRepository<BrandSection, Integer> {

	public BrandSection findFirstByOrderByPositionDesc();
	public BrandSection findFirstBySectionOrderByPositionDesc(Section section);
	public BrandSection findBySectionAndBrand(Section section, Brand brand);
	public List<BrandSection> findBySectionOrderByPositionAsc(Section section);
	public List<BrandSection> findBySection(Section section);
}
