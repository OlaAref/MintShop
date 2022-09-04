package com.olaaref.mintshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.AuthenticationType;
import com.olaaref.mintshop.common.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Customer findByEmail(String email);
	
	Customer findByVerificationCode(String verificationCode);
	
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id);
	
	@Query("UPDATE Customer c SET c.authenticationType = ?1 WHERE c.id = ?2")
	@Modifying
	public void updateAuthenticationType(AuthenticationType authenticationType, Integer id);
	
	Customer findByResetPasswordToken(String token);
}
