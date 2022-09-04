package com.olaaref.mintshop.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.BrandNotFoundException;
import com.olaaref.mintshop.common.exception.ProductNotFoundException;
import com.olaaref.mintshop.repository.BrandRepository;
import com.olaaref.mintshop.repository.ProductRepository;

@Service
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 5;
	public static final int SEARCH_RESULT_PER_PAGE = 5;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId){
		
		String categoryIdMnatch = "-" + String.valueOf(categoryId) + "-";
		
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		
		return productRepository.listByCategory(categoryId, categoryIdMnatch, pageable);
	}
	
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if(product == null) {
			throw new ProductNotFoundException("There is no product found with alias : " + alias);
		}
		
		return product;
	}

	public Brand getBrandOfProduct(String brandName) throws BrandNotFoundException {
		Brand brand = brandRepository.findByName(brandName);
		if(brand == null) {
			throw new BrandNotFoundException("There is no brand found with name ("+brandName+").");
		}
		return brand;
	}
	
	public Page<Product> getProductsByBrand(String brandName, int pageNum) throws BrandNotFoundException{
		Brand brand = brandRepository.findByName(brandName);
		if(brand == null) {
			throw new BrandNotFoundException("There is no brand found with name ("+brandName+").");
		}
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		Page<Product> products = productRepository.findByBrand(brand, pageable);
		return products;
	}
	
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable = PageRequest.of(pageNum-1, SEARCH_RESULT_PER_PAGE);
		return productRepository.search(keyword, pageable);
	}
	
	public Product getProductById(Integer id) throws ProductNotFoundException {
		try {
			Product product = productRepository.findById(id).get();
			return product;
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("There is no product found with ID : " + id);
		}
		
	}
	
	
}
