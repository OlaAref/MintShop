package com.olaaref.mintshop.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.dao.CategoryRepository;
import com.olaaref.mintshop.service.CategoryService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
	
	@MockBean
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	@Test
	public void testCheckUniqueName() {
		Integer id = 0;
		String name = "Computers";
		String alias = "abc";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		
		
		String unique = categoryService.checkUnique(id, name, alias);
		
		assertThat(unique).isEqualTo("DuplicateName");
	} 
	
	@Test
	public void testCheckUniqueAlias() {
		Integer id = 0;
		String name = "abc";
		String alias = "Computers";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		
		String unique = categoryService.checkUnique(id, name, alias);
		
		assertThat(unique).isEqualTo("DuplicateAlias");
	} 
	
	@Test
	public void testCheckUniqueReturnOk() {
		Integer id = 0;
		String name = "Computers";
		String alias = "Computers";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		
		
		String unique = categoryService.checkUnique(id, name, alias);
		
		assertThat(unique).isEqualTo("DuplicateAlias");
	} 
	
}
