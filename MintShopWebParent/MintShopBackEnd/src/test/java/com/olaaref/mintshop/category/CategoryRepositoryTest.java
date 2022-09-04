package com.olaaref.mintshop.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.dao.CategoryRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics", "Electronics", true);
		Category savedCategory = categoryRepository.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category subCategory1 = new Category("iPhone", "iPhone", true, parent);
		//Category subCategory2 = new Category("Smartphones", "Smartphones", true, parent);
		
		categoryRepository.save(subCategory1);
		//categoryRepository.save(subCategory2);
	}
	
	@Test
	public void testGetSubCategories() {
		Category parent = categoryRepository.findById(1).get();
		System.out.println(parent);
		System.out.println(parent.getChildren());
		
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categoryList = categoryRepository.findAll();
		
		for (Category category : categoryList) {
			
			if(category.getParent() == null) {
				
				System.out.println(category.getName());
				Set<Category> subCategories = category.getChildren();
				
				for (Category child : subCategories) {
					System.out.println("--" + child.getName());
					this.testPrintSubCategories(child, 1);
				}
			}
		}
	}
	
	@Test
	public void testPrintSubCategories(Category parent, int subLevel) {
		
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category category : children) {
			for (int i=0; i < newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(category.getName());
			
			testPrintSubCategories(category, newSubLevel);
		}
	}
	
	@Test
	public void testRootCategories() {
		System.out.println(categoryRepository.findRootCategories());
		
	}
	
	@Test
	public void testFindByName() {
		Category category = categoryRepository.findByName("Computer");
		
		assertThat(category).isNotNull();
		
	}
	
	@Test
	public void testFindByAlias() {
		Category category = categoryRepository.findByName("Computers");
		
		assertThat(category).isNotNull();
		
	}
	
	@Test
	public void testSearchCategory() {
		int pageSize = 4;
		int pageNumber = 0;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Category> page = categoryRepository.findAll("computer", pageable);
		
		List<Category> categoryList = page.getContent();
		
		categoryList.forEach(category -> System.out.println(category));
		
	}

}














