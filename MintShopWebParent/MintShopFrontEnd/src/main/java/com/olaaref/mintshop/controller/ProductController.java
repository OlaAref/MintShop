package com.olaaref.mintshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.CategoryNotFoundException;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{category-alias}")
	public String viewCategory(@PathVariable("category-alias") String categoryAlias, Model model) throws CategoryNotFoundException {
		
		return viewCategoryByPage(categoryAlias, 1, model);
	}
	
	@GetMapping("/c/{category-alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category-alias") String categoryAlias, 
									 @PathVariable("pageNum") int pageNum,
							         Model model) throws CategoryNotFoundException {
		
		Category category = categoryService.findCategoryByAlias(categoryAlias);
		if(category == null) {
			return "error/404";
		}
		
		List<Category> categoryParents = categoryService.getCategoryParents(category);
		
		Page<Product> productsPage = productService.listByCategory(pageNum, category.getId());
		
		getPageData(productsPage, pageNum, model, category);
		
		model.addAttribute("categoryParents", categoryParents);
		model.addAttribute("categoryName", category.getName());
		return "product/products-by-category";
	}
	
	private void getPageData(Page<Product> productsPage, int pageNum, Model model, Category category) {
		
		long start = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long end = start + ProductService.PRODUCTS_PER_PAGE - 1;
		if(end > productsPage.getTotalElements()) end = productsPage.getTotalElements();
			
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("category", category);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", productsPage.getTotalElements());
		model.addAttribute("totalPages", productsPage.getTotalPages());
		
	}
	
	@GetMapping("/p/{product-alias}")
	public String viewProductDetail(@PathVariable("product-alias") String productAlias, Model model) {
		
		try {
			Product product = productService.getProductByAlias(productAlias);
			List<Category> categoryParents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("categoryParents", categoryParents);
			model.addAttribute("product", product);
			model.addAttribute("productName", product.getShortName());
			
			return "product/product-details";
			
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		
		return searchByPage(1, keyword, model);
			
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@PathVariable("pageNum") int pageNum,
						 @RequestParam("keyword") String keyword, 
						 Model model) {
		
		Page<Product> productsPage = productService.search(keyword, pageNum);
		
		long start = (pageNum - 1) * ProductService.SEARCH_RESULT_PER_PAGE + 1;
		long end = start + ProductService.SEARCH_RESULT_PER_PAGE - 1;
		if(end > productsPage.getTotalElements()) end = productsPage.getTotalElements();
			
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", productsPage.getTotalElements());
		model.addAttribute("totalPages", productsPage.getTotalPages());
		model.addAttribute("keyword", keyword);
		
		return "product/search-result";
			
	}
}














