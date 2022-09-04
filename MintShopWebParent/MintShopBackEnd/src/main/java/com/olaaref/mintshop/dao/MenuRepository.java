package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.common.entity.MenuType;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	public Page<Menu> findAll(Pageable pageable);

	public Menu findByAlias(String alias);
	
	public List<Menu> findByMenuType(MenuType menuType);
	
	public Menu findByMenuTypeAndPosition(MenuType menuType, int position);
	
	@Query("SELECT m FROM Menu m WHERE m.menuType = ?2 AND m.position > ?1")
	public List<Menu> findMenusAfterPositionForMenuType(int position, MenuType type);

	@Query("UPDATE Menu m SET m.enabled = ?2 WHERE m.alias = ?1")
	@Modifying
	public void updateEnabledStatus(String menuAlias, boolean status);
	
	
	public Menu findFirstByMenuTypeOrderByPositionDesc(MenuType menuType);

	@Query("SELECT COUNT(m.id) FROM Menu m WHERE m.enabled = true")
	public long countEnabledMenus();
	
	@Query("SELECT COUNT(m.id) FROM Menu m WHERE m.enabled = false")
	public long countDisabledMenus();
	
	public long countByMenuType(MenuType menuType);
}
