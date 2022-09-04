package com.olaaref.mintshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.mintshop.common.entity.AuthenticationType;
import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.repository.CountryRepository;
import com.olaaref.mintshop.repository.CustomerRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}
	
	public void registerCustomer(Customer customer) {
		
		//encode the password
		String encodedPassword = bCryptPasswordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		
		//set enable to false
		customer.setEnabled(false);
		
		//create verification code
		String verificationCode = RandomString.make(64);
		customer.setVerificationCode(verificationCode);
		
		//set Authentication type
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		
		//save the customer
		customerRepository.save(customer);
		
	}
	
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		
		if(customer == null || customer.isEnabled()) {
			return false;
		}
		else {
			customerRepository.updateEnabledStatus(customer.getId());
			return true;
		}
		
	}
	
	public void updateAuthenticationType(Customer customer, AuthenticationType authenticationType) {
		if(!customer.getAuthenticationType().equals(authenticationType)) {
			customerRepository.updateAuthenticationType(authenticationType, customer.getId());
		}
	}
	
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		
		setName(customer, name);
		
		customer.setEnabled(true);
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setAddressLine1("");
		customer.setAddressLine2("");
		customer.setCity("");
		customer.setState("");
		customer.setCountry(countryRepository.findByCode(countryCode));
		
		customerRepository.save(customer);
		
	}

	private void setName(Customer customer, String name) {
		
		String[] nameArray = name.split(" ");
		
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}
		else {
			customer.setFirstName(nameArray[0]);
			customer.setLastName(nameArray[1]);
		}
	}
	
	public void updateCustomer(Customer formCustomer) {
		Customer dbCustomer = customerRepository.getById(formCustomer.getId());
		
		if (dbCustomer.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!formCustomer.getPassword().isEmpty()) {
				String encodedPassword = bCryptPasswordEncoder.encode(formCustomer.getPassword());
				formCustomer.setPassword(encodedPassword);
			} else {
				formCustomer.setPassword(dbCustomer.getPassword());
			} 
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
	
	public Customer getByResetPasswordToken(String token) {
		return customerRepository.findByResetPasswordToken(token);
	}
	
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		if (customer != null) {
			String resetPasswordToken = RandomString.make(30);
			customer.setResetPasswordToken(resetPasswordToken);
			customerRepository.save(customer);
			return resetPasswordToken;
		}
		else {
			throw new CustomerNotFoundException("There is no customer with email "+email);
		}
		
	}
	
	public void updatePassword(String resetPasswordToken, String password) throws CustomerNotFoundException {
		Customer customer = getByResetPasswordToken(resetPasswordToken);
		
		if (customer != null) {
			String encodedPassword = bCryptPasswordEncoder.encode(password);
			customer.setPassword(encodedPassword);
			customerRepository.save(customer);
			customer.setResetPasswordToken(null);
		}
		else {
			throw new CustomerNotFoundException("No Customer found : Invalid Token");
		}
	}
}





