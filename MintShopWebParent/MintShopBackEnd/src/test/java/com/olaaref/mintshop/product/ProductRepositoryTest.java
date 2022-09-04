package com.olaaref.mintshop.product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.gcp.GoogleCloudUtility;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager entityManager;

	
	@Test
	public void testCreateProduct() {
		
		Brand brand = entityManager.find(Brand.class, 3);
		Category category = entityManager.find(Category.class, 7);
		
		Product product = new Product();
		product.setName("Samsung Galaxy Note21");
		product.setAlias("samsung_galaxy_note21");
		product.setShortDescription("Samsung Galaxy Note21 short description");
		product.setFullDescription("Samsung Galaxy Note21 full description");
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(680);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		
		//product.setCreatedTime(LocalDateTime.now());
		//product.setUpdatedTime(LocalDateTime.now());
		
		Product savedProduct = productRepository.save(product);
		
		System.out.println(savedProduct);
	}
	
	@Test 
	public void testListAllProducts() {
		Iterable<Product> products = productRepository.findAll();
		products.forEach(System.out::println);
	}
	
	@Test 
	public void testGetProduct() {
		Product product = productRepository.getById(2);
		System.out.println(product);
	}
	
	@Test 
	public void testUpdateProduct() {
		Product product = productRepository.getById(4);
		product.setPrice(700);
		productRepository.save(product);
		testGetProduct();
	}
	
	@Test 
	public void testDeleteProduct() {
		productRepository.deleteById(3);
	}
	
	@Test 
	public void listAllFilesTest() throws FileNotFoundException, IOException {
		String uploadDir = "product-images/1/";
		GoogleCloudUtility utility = new GoogleCloudUtility();
	    List<String> listFolder = utility.listFolder(uploadDir);
	    for(String file : listFolder) {
	    	if(!file.contains("/extras/")) {
	    		System.out.println(file);
	    	}
	    }
	}
	
	@Test
	public void updateReviewCountAndAverageRatingTest() {
		Integer productID = 31;
		productRepository.updateReviewCountAndAverageRating(productID);
	}
	
	
}
