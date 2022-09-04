package com.olaaref.mintshop.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.dao.BrandRepository;
import com.olaaref.mintshop.dao.CategoryRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testCreateBrand() {
		Brand brand = new Brand("Test");
		Category category1 = categoryRepository.getById(8);
		//Category category2 = categoryRepository.getById(22);
		Set<Category> categories = new HashSet<Category>();
		categories.add(category1);
		//categories.add(category2);
		brand.setCategories(categories);
		
		Brand savedBrand = brandRepository.save(brand);
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllBrands() {
		
		Iterable<Brand> brands = brandRepository.findAll();
		brands.forEach(brand -> System.out.println(brand));
	}
	
	@Test
	public void testListAllBrandsOrderByName() {
		
		Iterable<Brand> brands = brandRepository.findAll();
		brands.forEach(brand -> System.out.println(brand));
	}
	
	@Test
	public void testListAllBrandsPageable() {
		Sort sort = Sort.by("id").ascending();
		
		Pageable pageable = PageRequest.of(1, 10, sort);
		
		Iterable<Brand> brands = brandRepository.findAll(pageable);
		brands.forEach(brand -> System.out.println(brand));
	}
	
	@Test
	public void testGetBrandById() {
		
		Brand brands = brandRepository.findById(2).get();
		System.out.println(brands);
		assertThat(brands).isNotNull();
	}
	
	
	@Test
	public void testFindByName() {
		Brand brand = brandRepository.findByName("Acer");
		System.out.println(brand);
		assertThat(brand).isNotNull();
		
	}
	
	@Test
	public void testUpdateBrand() {
		
		Brand brand = brandRepository.findById(3).get();
		System.out.println(brand);
		brand.setName("Samsung Elecronics");
		brandRepository.save(brand);
		System.out.println(brand);
	}
	
	@Test
	public void testDeleteBrand() {
		
		Brand brand = brandRepository.findById(4).get();
		System.out.println(brand);
		
		brandRepository.deleteById(4);
	}
	

}














