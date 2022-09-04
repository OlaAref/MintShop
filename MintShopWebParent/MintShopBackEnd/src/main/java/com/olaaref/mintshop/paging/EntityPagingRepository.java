package com.olaaref.mintshop.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityPagingRepository<T, ID> extends JpaRepository<T, ID> {

	public Page<T> findAll(String keyword, Pageable pageable);
}
