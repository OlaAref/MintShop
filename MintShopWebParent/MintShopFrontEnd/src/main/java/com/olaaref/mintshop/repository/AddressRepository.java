package com.olaaref.mintshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

	public List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	public Address findByIdAndCustomer(Integer id, Integer customerId);
	
	@Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	@Modifying
	public void deleteByIdAndCustomer(Integer id, Integer customerId);
	
	@Query("UPDATE Address a SET a.defaultForShopping = true WHERE a.id = ?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShopping = false WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);
	
	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND defaultForShopping = true")
	public Address findDefaultAddressByCustomer(Integer customerId);
}
