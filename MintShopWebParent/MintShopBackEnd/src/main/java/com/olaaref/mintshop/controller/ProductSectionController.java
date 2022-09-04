package com.olaaref.mintshop.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.entity.section.ProductSection;
import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.ProductService;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section/product")
public class ProductSectionController {
	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/new")
	public String newProductSectionForm(Model model) {
		Section section = new Section(true, SectionType.PRODUCT);
		
		model.addAttribute("section", section);
		model.addAttribute("pageHeader", "Product Section");
		model.addAttribute("pageTitle", "Add Product Section");
		return "sections/section-form";
	}
	
	@GetMapping("/edit/{sectionId}")
	public String editProductSectionForm(@PathVariable("sectionId") Integer sectionId, 
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			Section section = sectionService.getById(sectionId);
			List<Product> selectedProducts = getProducts(section);
			section.setProducts(selectedProducts);
			
			List<Product> products = productService.listAll();
			
			model.addAttribute("section", section);
			model.addAttribute("products", products);
			model.addAttribute("pageHeader", "Product Section");
			model.addAttribute("pageTitle", "Edit Product Section");
			return "sections/section-form";
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
	
	private List<Product> getProducts(Section section) {
		List<ProductSection> productsSection = sectionService.getProductsSectionSorted(section);
		
		List<Product> products = new LinkedList<>();
		for (ProductSection art : productsSection) {
			products.add(new Product(art.getProduct().getId()));
		}
		return products;
	}
}
