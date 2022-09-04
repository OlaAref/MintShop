package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.section.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

	public Section findFirstByOrderByPositionDesc();
	
	@Query("SELECT s.sectionType FROM Section s WHERE s.id = ?1")
	public String getSectionType(Integer sectionId);

	@Query("UPDATE Section s SET s.enabled = ?2 WHERE s.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer sectionId, boolean status);

	public Section findByPosition(int newPosition);

	@Query("SELECT s FROM Section s WHERE s.id > ?1")
	public List<Section> findSectionsAfterPosition(int position);
	
	public List<Section> findAllByOrderByPosition();

	@Query("SELECT COUNT(s.id) FROM Section s WHERE s.enabled = true")
	public long countEnabledSections();
	
	@Query("SELECT COUNT(s.id) FROM Section s WHERE s.enabled = false")
	public long countDisabledSections();
}
