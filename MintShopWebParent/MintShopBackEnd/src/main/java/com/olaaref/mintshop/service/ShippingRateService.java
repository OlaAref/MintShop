package com.olaaref.mintshop.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.exception.ShippingRateAlreadyExistException;
import com.olaaref.mintshop.common.exception.ShippingRateNotFoundException;
import com.olaaref.mintshop.dao.CountryRepository;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.dao.ShippingRateRepository;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class ShippingRateService {

	public static final int SHIPPING_RATE_PER_PAGE = 10;
	public static final int DIM_DIVISOR = 5000;
	
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private ShippingRateRepository shippingRateRepository;
	@Autowired
	private ProductRepository productRepository;
	
	
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNum, SHIPPING_RATE_PER_PAGE, shippingRateRepository);
	}
	
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate formShipping) throws ShippingRateAlreadyExistException {
		
		ShippingRate dbShipping = shippingRateRepository.findByCountryAndState(formShipping.getCountry().getId(), formShipping.getState());
		
		boolean existRateInAddPage = formShipping.getId() == null && dbShipping != null;
		boolean existRateInEditPage = formShipping.getId() != null && dbShipping != null && formShipping.getId() != dbShipping.getId();
		
		if(existRateInAddPage || existRateInEditPage) {
			throw new ShippingRateAlreadyExistException("There is already rate for the destination " + 
						formShipping.getState() + " - " + formShipping.getCountry().getName());
		}
		
		shippingRateRepository.save(formShipping);
	}
	
	public ShippingRate getById(Integer id) throws ShippingRateNotFoundException {
		try {
			return shippingRateRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateNotFoundException("There is no shipping rate with ID : " + id);
		}
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long countById = shippingRateRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new ShippingRateNotFoundException("There is no shipping rate with ID : " + id);
		}
		shippingRateRepository.deleteById(id);
	}
	
	public void updateCodSupported(Integer id, boolean status) throws ShippingRateNotFoundException {
		Long countById = shippingRateRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new ShippingRateNotFoundException("There is no shipping rate with ID : " + id);
		}
		
		shippingRateRepository.updateCodSupported(id, status);
		
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException, ProductNotFoundException {
		
		ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(countryId, state);
		
		if(shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the given destination."
					+ "You have to enter shipping cost manually");
		}
		
		Product product = productRepository.getById(productId);
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = dimWeight > product.getWeight() ? dimWeight : product.getWeight();
		float shippingCost = shippingRate.getRate() * finalWeight;
		
		return shippingCost;
	}
	
}




















