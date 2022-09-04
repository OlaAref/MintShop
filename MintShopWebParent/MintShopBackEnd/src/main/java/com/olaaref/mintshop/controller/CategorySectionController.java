package com.olaaref.mintshop.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.section.CategorySection;
import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section/category")
public class CategorySectionController {

	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/all/new")
	public String newAllCategoriesSectionForm(Model model) {
		Section section = new Section(true, SectionType.ALL_CATEGORIES);
		model.addAttribute("section", section);
		model.addAttribute("pageHeader", "All Categories Section");
		model.addAttribute("pageTitle", "Add All Categories Section");
		return "sections/section-form";
	}
	
	@GetMapping("/new")
	public String newCategorySectionForm(Model model) {
		Section section = new Section(true, SectionType.CATEGORY);
		List<Category> categories = categoryService.listFormCategories();
		
		model.addAttribute("section", section);
		model.addAttribute("categories", categories);
		model.addAttribute("pageHeader", "Category Section");
		model.addAttribute("pageTitle", "Add Category Section");
		return "sections/section-form";
	}
	
	@GetMapping("/edit/{sectionId}")
	public String newCategorySectionForm(@PathVariable("sectionId") Integer sectionId, 
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			Section section = sectionService.getById(sectionId);
			List<Category> selectedCat = getCategories(section);
			section.setCategories(selectedCat);
			
			List<Category> categories = categoryService.listFormCategories();
			
			model.addAttribute("section", section);
			model.addAttribute("categories", categories);
			model.addAttribute("pageHeader", "Category Section");
			model.addAttribute("pageTitle", "Edit Category Section");
			return "sections/section-form";
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}

	private List<Category> getCategories(Section section) {
		Set<CategorySection> categoriesSection = sectionService.getCategoriesSectionSorted(section);
		
		List<Category> categories = new LinkedList<Category>();
		for (CategorySection cat : categoriesSection) {
			categories.add(new Category(cat.getCategory().getId()));
		}
		return categories;
	}
	
	
}
