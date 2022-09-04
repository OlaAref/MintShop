package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/checkUnique")
	public String checkUnique(@RequestParam(name ="id", required = false) Integer id,
							  @RequestParam("name") String name,
							  @RequestParam("alias") String alias) {
		
		return categoryService.checkUnique(id, name, alias);
	}

}
