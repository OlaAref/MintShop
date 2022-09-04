package com.olaaref.mintshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.exception.CategoryNotFoundException;
import com.olaaref.mintshop.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> listNonChildrenCategories() {
		List<Category> nonChildrenCategories = new ArrayList<Category>();
		List<Category> allEnabledCategories = categoryRepository.findAllEnabled();
		
		allEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if(children == null || children.size() == 0) {
				nonChildrenCategories.add(category);
			}
		});
		
		return nonChildrenCategories;
	}
	
	public Category findCategoryByAlias(String alias) throws CategoryNotFoundException {
		Category category = categoryRepository.findByAliasEnabled(alias);
		
		if(category == null) {
			throw new CategoryNotFoundException("There is no category found with alias : " + alias);
		}
		
		return category;
	}
	
	public List<Category> getCategoryParents(Category category){
		List<Category> parentsList = new ArrayList<Category>();
		
		Category parent = category.getParent();
		
		while(parent != null) {
			parentsList.add(0, parent);
			//assign 'parent' to parent of the parent
			parent = parent.getParent();
		}
		
		//add the last child
		parentsList.add(category);
		
		return parentsList;
	}

}
