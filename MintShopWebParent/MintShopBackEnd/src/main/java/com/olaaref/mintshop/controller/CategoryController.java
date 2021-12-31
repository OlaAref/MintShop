package com.olaaref.mintshop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.exception.CategoryNotFoundException;
import com.olaaref.mintshop.exporter.csv.CategoryCsvExporter;
import com.olaaref.mintshop.exporter.excel.CategoryExcelExporter;
import com.olaaref.mintshop.exporter.pdf.CategoryPdfExporter;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/list")
	public String listAll() {
		return "redirect:/category/page/1?sortField=id&sortDir=asc";
	}
	
	
	
	@GetMapping("/page/{pageNum}")
	public String listAllByPage(@PagingAndSortingParam(listName = "categories", moduleUrl = "/category") PagingAndSortingHelper helper,
								@PathVariable("pageNum") int pageNum) {
		
		categoryService.listCategoriesByPage(pageNum, helper);
		
		return "categories/list-categories";
	}
	
	@GetMapping("/toAdd")
	public String toAddPage(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", categoryService.listFormCategories());
		model.addAttribute("pageTitle", "Add");
		
		return "categories/category-form";
	}
	
	@PostMapping("/add")
	public String addCategory(@ModelAttribute("category") Category category,
								@RequestParam("photo") MultipartFile multipartFile,
								RedirectAttributes redirectAttributes) throws IOException, CategoryNotFoundException {
		
		if(!multipartFile.isEmpty()) {
			category.setImage(multipartFile.getBytes());
		}
		else {
			category.setImage(categoryService.getById(category.getId()).getImage());
		}
		
		
		categoryService.saveCategory(category);
		redirectAttributes.addFlashAttribute("message", category.getName());
		
		return "redirect:/category/page/1?sortField=id&sortDir=asc&keyword="+category.getName();
	}
	
	@GetMapping("/load/{id}")
	public String loadCategoryDeatails(@PathVariable("id") int id,
										RedirectAttributes redirectAttributes,
										Model model) {
		
		try {
			
			Category category = categoryService.getById(id);
			
			model.addAttribute("category", category);
			model.addAttribute("listCategories", categoryService.listFormCategories());
			model.addAttribute("pageTitle", "Edit");
			
			return "categories/category-form";
			
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/category/list";
		}
		
	}
	
	@GetMapping("{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable("id") Integer id,
									  @PathVariable("status") boolean status,
									  RedirectAttributes redirectAttributes) throws CategoryNotFoundException {
		
		categoryService.updateEnabledStatus(id, status);
		
		//get enabled/disabled keyword
		String enabled = status ? "enabled" : "disabled";
		
		String message = "The category "+categoryService.getById(id).getName()+" has been "+enabled+" successfully.";
		
		redirectAttributes.addFlashAttribute("enabledMessag", message);
		
		return "redirect:/category/list";
		
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id,
								RedirectAttributes redirectAttributes) throws CategoryNotFoundException {
		
		String name = categoryService.getById(id).getName();
		
		try {
			
			categoryService.deleteCategory(id);
			redirectAttributes.addFlashAttribute("deleteMessag", "Category "+name+" has been deleted successfully.");
			
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
		}
		
		return "redirect:/category/list";
		
	}
	
	@GetMapping("/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Category> categoriesList = categoryService.listAll();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(categoriesList, response);
	}
	
	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Category> categoriesList = categoryService.listAll();
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(categoriesList, response);
	}
	
	@GetMapping("/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<Category> categoriesList = categoryService.listAll();
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(categoriesList, response);
	}
}








