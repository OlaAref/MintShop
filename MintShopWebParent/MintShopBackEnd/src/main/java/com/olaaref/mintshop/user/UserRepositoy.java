package com.olaaref.mintshop.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface UserRepositoy extends EntityPagingRepository<User, Integer> {

	User findByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	@Query("update User u set u.enabled = ?2 where u.id = ?1")
	@Modifying//for update DML
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.firstName, ' ', u.lastName, ' ', u.email) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT COUNT(u.id) FROM User u")
	public long countUsers();
	
	@Query("SELECT COUNT(u.id) FROM User u WHERE u.enabled = true")
	public long countEnabledUsers();
	
	@Query("SELECT COUNT(u.id) FROM User u WHERE u.enabled = false")
	public long countDisabledUsers();
}
