package com.olaaref.mintshop.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.dto.CategoryDTO;
import com.olaaref.mintshop.exception.BrandNotFoundException;
import com.olaaref.mintshop.exception.BrandNotFoundRestException;
import com.olaaref.mintshop.service.BrandService;

@RestController
@RequestMapping("/brands")
public class BrandRestController {
	
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/checkUnique")
	public String checkUnique(@RequestParam("id") Integer id,
							  @RequestParam("name") String name) {
		
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("{id}/categories")
	public List<CategoryDTO> listCategoriesBybrand(@PathVariable("id") Integer id) throws BrandNotFoundRestException{
		try {
			
			Brand brand = brandService.getById(id);
			Set<Category> categories = brand.getCategories();
			
			List<CategoryDTO> categoriesDto = new ArrayList<CategoryDTO>();
			
			categories.forEach(category -> {categoriesDto.add(new CategoryDTO(category.getId(), category.getName()));});
			
			return categoriesDto;
			
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}

}
