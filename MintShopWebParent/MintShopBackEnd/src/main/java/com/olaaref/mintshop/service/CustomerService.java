package com.olaaref.mintshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.dao.CountryRepository;
import com.olaaref.mintshop.dao.CustomerRepository;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;

@Service
@Transactional
public class CustomerService {
	
	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	

	
	public void listAllCustomersByPage(int pageNum, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepository);
	}
	
	public Customer getById(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (Exception e) {
			throw new CustomerNotFoundException("There is no customer with ID : " + id);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		customerRepository.updateEnabledStatus(id, status);
	}
	
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(Integer id, String email) {
		
		Customer customerByEmail = customerRepository.findByEmail(email);
		
		if(customerByEmail != null && customerByEmail.getId() != id) {
			return false;
		}
		
		return true;
	}

	public void save(Customer formCustomer) {
		
		Customer dbCustomer = customerRepository.getById(formCustomer.getId());
		
		if (!formCustomer.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(formCustomer.getPassword());
			formCustomer.setPassword(encodedPassword);
		}
		else {
			formCustomer.setPassword(dbCustomer.getPassword());
		}
		
		formCustomer.setEnabled(dbCustomer.isEnabled());
		formCustomer.setCreatedTime(dbCustomer.getCreatedTime());
		formCustomer.setVerificationCode(dbCustomer.getVerificationCode());
		formCustomer.setAuthenticationType(dbCustomer.getAuthenticationType());
		formCustomer.setResetPasswordToken(dbCustomer.getResetPasswordToken());
		
		customerRepository.save(formCustomer);
	}
	
	public void deleteCustomer(Integer id) throws CustomerNotFoundException {
		Long countByID = customerRepository.countById(id);
		if(countByID == null || countByID == 0) {
			throw new CustomerNotFoundException("There is no customer with ID : " + id);
		}
		else {
			customerRepository.deleteById(id);
		}
	}
}
