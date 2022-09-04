package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.dto.ProductDTO;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/checkUnique")
	public String checkUniqueOfProductName(@RequestParam("id") Integer id,
										   @RequestParam("name") String name) {
		return productService.checkUnique(id, name);
	}
	
	@GetMapping("/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id") Integer id) throws ProductNotFoundException {
		
		Product product = productService.getById(id);

		return new ProductDTO(product.getName(), product.getImagePath(), product.getDiscountedPrice(), product.getCost());
	}
	
}
