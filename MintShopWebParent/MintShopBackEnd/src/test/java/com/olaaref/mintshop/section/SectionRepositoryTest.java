package com.olaaref.mintshop.section;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.dao.SectionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SectionRepositoryTest {

	@Autowired
	private SectionRepository sectionRepository;
	
	@Test
	public void createTablesTest() {
		
	}
	
	@Test
	public void findLastPositionTest() {
		Section section = sectionRepository.findFirstByOrderByPositionDesc();
		int position;
		if(section == null) {
			position = 1;
		}
		else {
			position = section.getPosition() + 1;
		}
		System.out.println(position);
	}
	
	@Test
	public void getSectionTypeTest() {
		String sectionType = sectionRepository.getSectionType(22);
		System.out.println(sectionType);
	}
	
	@Test
	public void updateEnabledStatusTest() {
		sectionRepository.updateEnabledStatus(1, false);
	}
	
	@Test
	public void findByPositionTest() {
		Section section = sectionRepository.findByPosition(2);
		System.out.println(section);
	}
	
	@Test
	public void findSectionsAfterPositionTest() {
		List<Section> sections = sectionRepository.findSectionsAfterPosition(1);
		sections.forEach(System.out::println);
	}
	
}




















