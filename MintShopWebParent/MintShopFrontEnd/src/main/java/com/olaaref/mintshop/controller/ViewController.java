package com.olaaref.mintshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.BrandNotFoundException;
import com.olaaref.mintshop.common.exception.CategoryNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;

@Controller
public class ViewController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{category-alias}")
	public String viewCategory(@PathVariable("category-alias") String categoryAlias, Model model) throws CategoryNotFoundException {
		
		return viewCategoryByPage(categoryAlias, 1, "id", "asc", model);
	}
	
	@GetMapping("/c/{category-alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category-alias") String categoryAlias, 
									 @PathVariable("pageNum") int pageNum,
									 @RequestParam("sortField") String sortField,
									 @RequestParam("sortDir") String sortDir,
							         Model model) throws CategoryNotFoundException {
		
		Category category = categoryService.findCategoryByAlias(categoryAlias);
		if(category == null) {
			return "error/404";
		}
		
		List<Category> categoryParents = categoryService.getCategoryParents(category);
		
		Page<Product> productsPage = productService.listByCategory(pageNum, category.getId());
		
		getPageData(productsPage, pageNum, model, category, sortField, sortDir);
		
		model.addAttribute("categoryParents", categoryParents);
		model.addAttribute("categoryName", category.getName());
		return "product/products-by-category";
	}
	
	private void getPageData(Page<Product> productsPage, int pageNum, Model model, Category category, String sortField, String sortDir) {
		
		long start = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long end = start + ProductService.PRODUCTS_PER_PAGE - 1;
		if(end > productsPage.getTotalElements()) end = productsPage.getTotalElements();
			
		model.addAttribute("moduleLink", "/c/"+category.getAlias());
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("category", category);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", productsPage.getTotalElements());
		model.addAttribute("totalPages", productsPage.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
	}
	
	@GetMapping("/b/{brand-name}")
	public String viewBrand(@PathVariable("brand-name") String brandName, Model model) throws BrandNotFoundException {
		
		return viewBrandByPage(brandName, 1, "id", "asc", model);
	}
	
	@GetMapping("/b/{brand-alias}/page/{pageNum}")
	public String viewBrandByPage(@PathVariable("brand-name") String brandName, 
									 @PathVariable("pageNum") int pageNum,
									 @RequestParam("sortField") String sortField,
									 @RequestParam("sortDir") String sortDir,
							         Model model){
		
		try {
			Brand brand = productService.getBrandOfProduct(brandName);
			Page<Product> productsPage = productService.getProductsByBrand(brandName, pageNum);
			
			getPageData(productsPage, pageNum, model, brand, sortField, sortDir);
			
			return "product/products-by-brand";
		} catch (BrandNotFoundException e) {
			return "error/404";
		}
	}
	
	private void getPageData(Page<Product> productsPage, int pageNum, Model model, Brand brand, String sortField, String sortDir) {
		
		long start = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long end = start + ProductService.PRODUCTS_PER_PAGE - 1;
		if(end > productsPage.getTotalElements()) end = productsPage.getTotalElements();
			
		model.addAttribute("moduleLink", "/b/"+brand.getName());
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("brand", brand);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", productsPage.getTotalElements());
		model.addAttribute("totalPages", productsPage.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
	}
}
