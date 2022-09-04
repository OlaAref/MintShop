package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class ProductService {

	public static final int PRODUCT_PER_PAGE = 5; 
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> listAll(){
		return productRepository.findAll();
	}
	
	
	
	public Product getById(Integer id) throws ProductNotFoundException {
		
		try {
			return productRepository.getById(id);
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("There is no product with ID : " + id);
		}
	}
	
	public void listByPage(int pageNum, Integer categoryId, PagingAndSortingHelper helper){
		
		Pageable pageable = helper.createPageable(pageNum, PRODUCT_PER_PAGE);
		String keyword = helper.getKeyword();
		
		Page<Product> productsPage = null;
		
		if(keyword != null && !keyword.isEmpty()) {
			
			if(categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				productsPage = productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			}
			else {
				productsPage = productRepository.findAll(keyword, pageable);
			}
		}
		
		if(categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
			productsPage =  productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		else {
			productsPage = productRepository.findAll(pageable);
		}
		
		helper.updateModelAttributes(pageNum, productsPage);
	}
	
	public void searchProductsByName(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(pageNum, PRODUCT_PER_PAGE);
		String keyword = helper.getKeyword();
		
		Page<Product> page = productRepository.searchProductsByName(keyword, pageable);
		
		helper.updateModelAttributes(pageNum, page);
	}
	
	public Product save(Product product) {
		
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "_").replaceAll("/", "_").replaceAll("^[^a-zA-Z0-9_]+$", "");
			product.setAlias(defaultAlias);
		}
		else {
			product.setAlias(product.getAlias().replace(" ", "_"));
		}
		
		Product savedProduct = productRepository.save(product);
		productRepository.updateReviewCountAndAverageRating(savedProduct.getId());
		
		return savedProduct;
	}
	
	public void saveProductPrice(Product product) {
		
		Product productDB = productRepository.getById(product.getId());
		productDB.setPrice(product.getPrice());
		productDB.setCost(product.getCost());
		productDB.setDiscountPercent(product.getDiscountPercent());
		
		productRepository.save(productDB);
	}

	@SuppressWarnings("unused")
	public String checkUnique(Integer id, String name) {

		boolean isNew = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);
		
		if(isNew) {
			if(productByName != null) {
				return "DuplicateName";
			}
			if(productByName != null && productByName.getId() != id) {
				return "DuplicateName";
			}
		}
		
		return "OK";
	}

	public void updateEnabledStatus(Integer id, boolean status) {
		productRepository.updateEnabledStatus(id, status);
		
	}

	public void deleteProduct(Integer id) throws ProductNotFoundException {
		
		Long countById = productRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new ProductNotFoundException("There is no product with ID : " + id);
		}
		productRepository.deleteById(id);
	}

	
	
	

	
}
