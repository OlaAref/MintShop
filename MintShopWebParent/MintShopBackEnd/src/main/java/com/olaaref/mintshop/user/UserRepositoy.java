package com.olaaref.mintshop.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.User;

@Repository
public interface UserRepositoy extends JpaRepository<User, Integer> {

	User findByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	@Query("update User u set u.enabled = ?2 where u.id = ?1")
	@Modifying//for update DDL
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.firstName, ' ', u.lastName, ' ', u.email) LIKE %?1%")
	public Page<User> search(String keyword, Pageable pageable);
}
