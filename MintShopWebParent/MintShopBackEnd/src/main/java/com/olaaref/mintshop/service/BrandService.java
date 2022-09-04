package com.olaaref.mintshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.dao.BrandRepository;
import com.olaaref.mintshop.exception.BrandNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 10;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public List<Brand> listAll(){
		return brandRepository.findAll();
	}
	
	public void listAllBrandsByPage(int pageNum, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNum, BRANDS_PER_PAGE, brandRepository);
	}
	
	public Brand getById(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (Exception e) {
			throw new BrandNotFoundException("There is no brand with ID : " + id);
		}
	}

	public String checkUnique(Integer id, String name) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Brand brandByName = brandRepository.findByName(name);
		
		if(isCreatingNew && brandByName != null) {
			return "DuplicateName";
		}
		else {
			if(brandByName != null && brandByName.getId() != id) {
				return "DuplicateName";
			}
		}
		
		
		return "OK";
	}

	public Brand save(Brand brand) {
		Brand savedBrand = brandRepository.save(brand);
		return savedBrand;
	}
	
	public void deleteBrand(Integer id) throws BrandNotFoundException {
		Long countByID = brandRepository.countById(id);
		if(countByID == null || countByID == 0) {
			throw new BrandNotFoundException("There is no brand with ID : " + id);
		}
		else {
			brandRepository.deleteById(id);
		}
	}
}
