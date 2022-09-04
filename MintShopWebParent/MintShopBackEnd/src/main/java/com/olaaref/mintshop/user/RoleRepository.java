package com.olaaref.mintshop.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
