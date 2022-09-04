package com.olaaref.mintshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.dao.CategoryRepository;
import com.olaaref.mintshop.exception.CategoryNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class CategoryService {
	
	public static final int CATEGORIES_PER_PAGE = 10;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> listAll(){
		List<Category> categories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		return listHieraricalCategories(categories);
	}
	
	
	public List<Category> listFormCategories(){
		
		List<Category> formCategories = new ArrayList<Category>();
		Iterable<Category> dbCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		
		for (Category category : dbCategories) {
			
			if(category.getParent() == null) {
				formCategories.add(Category.copyIdAndName(category));
				
				Set<Category> children = category.getChildren();
				
				for (Category child : children) {
					String name = "--" + child.getName();
					formCategories.add(Category.copyIdAndName(child.getId(), name));
					
					listChildren(formCategories, child, 1);
				}
			}
		}
		
		return formCategories;
	}
	
	private void listChildren(List<Category> formCategories, Category parent, int subLevel) {
		
		int newSubLevel = subLevel+1;
		
		Set<Category> children = parent.getChildren();
		for (Category category : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += category.getName();
			formCategories.add(Category.copyIdAndName(category.getId(), name));
			listChildren(formCategories, category, newSubLevel);
		}
		
	}

	public void listCategoriesByPage(int pageNumber, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNumber, CATEGORIES_PER_PAGE, categoryRepository);
	}
	
	public Category saveCategory(Category category) {
		
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		
		Category savedCategory = categoryRepository.save(category);
		return savedCategory;
	}
	
	
	public Category getById(int id) throws CategoryNotFoundException {
		try {
			return categoryRepository.getById(id);
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("No category found with ID : "+id);
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Category categoryByName = categoryRepository.findByName(name);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicateName";
			}
			else {
				Category categoryByAlias = categoryRepository.findByAlias(alias);
				if(categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		}
		else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		
		return "OK";
	}
	
	public void updateEnabledStatus(Integer id, boolean enabledStatus) {
		categoryRepository.updateEnabledStatus(id, enabledStatus);
	}
	
	public void deleteCategory(Integer id) throws CategoryNotFoundException {
		Long countById = categoryRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new CategoryNotFoundException("No category found with ID : " + id);
		}
		categoryRepository.deleteById(id);
	}
	
	
	

	private List<Category> listHieraricalCategories(List<Category> rootCategories){
		List<Category> hieraricalCategories = new ArrayList<Category>();
		
		for (Category rootCategory : rootCategories) {
			//copy the categories to avoid making changes to the database when add '--' to the name
			hieraricalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			for (Category child : children) {
				String name = "--" + child.getName();
				hieraricalCategories.add(Category.copyFull(child, name));
				
				listHieraricalChildren(hieraricalCategories, child, 1);
			}
		}
		
		return hieraricalCategories;
	}
	
	private void listHieraricalChildren(List<Category> hieraricalCategories, Category parent, int subLevel) {
		
		int newSubLevel = subLevel+1;
		Set<Category> children = parent.getChildren();
		
		for (Category category : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += category.getName();
			hieraricalCategories.add(Category.copyFull(category, name));
			listHieraricalChildren(hieraricalCategories, category, newSubLevel);
		}
		
	}
	
}
