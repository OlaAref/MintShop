package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.ProductService;

@Controller
@RequestMapping({ "/search" })
public class ProductSearchController {
	@Autowired
	private ProductService productService;

	@GetMapping({ "/product" })
	public String showSearchProductPage() {
		return "redirect:/search/product/page/1?sortField=name&sortDir=asc";
	}

	@GetMapping({ "/product/page/{pageNum}" })
	public String searchProductsByPage(
			@PagingAndSortingParam(listName = "products", moduleUrl = "/search/product") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum, Model model) {
		this.productService.searchProductsByName(pageNum, helper);
		return "orders/search-product";
	}

	@PostMapping({ "/product" })
	public String searchProducts(@RequestParam("keyword") String keyword) {
		return "redirect:/search/product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	
	//section mapping
	@GetMapping("/section/product")
	public String showSearchProductPageForSection() {
		return "redirect:/search/section/product/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/section/product/page/{pageNum}")
	public String searchProductsByPageForSection(@PagingAndSortingParam(listName = "products", moduleUrl = "/search/product") PagingAndSortingHelper helper,
												@PathVariable("pageNum") int pageNum, Model model) {
		this.productService.searchProductsByName(pageNum, helper);
		return "sections/search-product";
	}
	
	@PostMapping("/section/product")
	public String searchProductsForSection(@RequestParam("keyword") String keyword) {
		return "redirect:/search/section/product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
}
