package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.section.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

	public List<Section> findAllByEnabledOrderByPosition(boolean enabled);
}
