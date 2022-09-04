package com.olaaref.mintshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.repository.AddressRepository;
import com.olaaref.mintshop.repository.CountryRepository;

@Service
@Transactional
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	public List<Address> listAddressBook(Customer customer){
		return addressRepository.findByCustomer(customer);
	}
	
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}

	public Address get(Integer addressId, Integer customerId) {
		return addressRepository.findByIdAndCustomer(addressId, customerId);
	}
	
	public void saveAddress(Address address) {
		addressRepository.save(address);
	}
	
	public void deletAddress(Integer addressId, Integer customerId) {
		addressRepository.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			addressRepository.setDefaultAddress(defaultAddressId);
		}
		addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
	}
	
	public Address getDefaultAddress(Customer customer) {
		return addressRepository.findDefaultAddressByCustomer(customer.getId());
	}

}














