package com.olaaref.mintshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.repository.SectionRepository;

@Service
public class SectionService {

	@Autowired
	private SectionRepository sectionRepository;
	
	public List<Section> listAllSections(){
		return sectionRepository.findAllByEnabledOrderByPosition(true);
	}
}
