package com.olaaref.mintshop.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.section.CategorySection;
import com.olaaref.mintshop.common.entity.section.Section;

@Repository
public interface CategorySectionRepository extends JpaRepository<CategorySection, Integer> {
	
	public CategorySection findFirstByOrderByPositionDesc();
	public CategorySection findFirstBySectionOrderByPositionDesc(Section section);
	public CategorySection findBySectionAndCategory(Section section, Category category);
	public Set<CategorySection> findBySectionOrderByPositionAsc(Section section);
	
	public List<CategorySection> findBySection(Section section);
}
