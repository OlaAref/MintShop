package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.paging.EntityPagingRepository;

@Repository
public interface BrandRepository extends EntityPagingRepository<Brand, Integer> {

	Brand findByName(@Param("name") String name);
	
	Long countById(Integer id);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	List<Brand> findAll();

}
