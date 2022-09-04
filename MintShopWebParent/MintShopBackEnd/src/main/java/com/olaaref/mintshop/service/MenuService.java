package com.olaaref.mintshop.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.common.entity.MenuType;
import com.olaaref.mintshop.common.exception.MenuNotFoundException;
import com.olaaref.mintshop.dao.MenuRepository;

@Service
@Transactional
public class MenuService {
	
	@Autowired
	private MenuRepository menuRepository;

	public List<Menu> getAllMenus() {
		List<Menu> menus = menuRepository.findAll();
		//sort by type then sort by position
		Comparator<Menu> byType = Comparator.comparing(Menu::getMenuType).thenComparingInt(Menu::getPosition);
		menus.sort(byType);
		return menus;
	}
	
	public List<Menu> getByMenuType(MenuType type){
		return menuRepository.findByMenuType(type);
	}
	
	public Menu getById(Integer id){
		return menuRepository.findById(id).get();
	}
	
	public Menu getByAlias(String alias) throws MenuNotFoundException{
		Menu menu = menuRepository.findByAlias(alias);
		if(menu == null) {
			throw new MenuNotFoundException("No Menu found with alias ("+alias+").");
		}
		return menu;
	}

	public String checkUnique(Integer menuId, String alias) {
		boolean isNew = (menuId == null || menuId == 0);
		Menu dbMenu = menuRepository.findByAlias(alias);
		
		if(isNew && dbMenu != null) {
			return "Duplicate";
		}
		else if(dbMenu != null && dbMenu.getId() != menuId) {
			return "Duplicate";
		}
		
		return "OK";
	}

	public void save(Menu menu) {
		
		Menu dbMenu = menuRepository.findByAlias(menu.getAlias());
		boolean isNew = (menu.getId() == null || menu.getId() == 0);
		if(isNew) {
			setPosition(menu);
		}
		else {
			if(!menu.getMenuType().equals(dbMenu.getMenuType())) {
				setPosition(menu);
				rearrangeMenus(dbMenu.getPosition(), dbMenu.getMenuType());
			}
		}
		
		menuRepository.save(menu);
	}

	private void setPosition(Menu menu) {
		if(menu.getMenuType().equals(MenuType.HEADER)) {
			menu.setPosition(getHeaderLastPosition());
		}
		else if(menu.getMenuType().equals(MenuType.FOOTER)) {
			menu.setPosition(getFooterLastPosition());
		}
		
	}

	private int getHeaderLastPosition() {
		Menu menu = menuRepository.findFirstByMenuTypeOrderByPositionDesc(MenuType.HEADER);
		if(menu == null) {
			return 1;
		}
		
		return menu.getPosition() + 1;	
	}
	
	private int getFooterLastPosition() {
		Menu menu = menuRepository.findFirstByMenuTypeOrderByPositionDesc(MenuType.FOOTER);
		if(menu == null) {
			return 1;
		}
		
		return menu.getPosition() + 1;
	}
	
	public String levelUpMenu(String alias) throws MenuNotFoundException {
		
		Menu menu = getByAlias(alias);
		
		if(menu.getPosition() == 1) {
			return "The menu ID ("+menu.getId()+") is already in first position.";
		}
		
		int oldPosition = menu.getPosition();
		int newPosition = oldPosition - 1;
		
		Menu levelDownMenu = menuRepository.findByMenuTypeAndPosition(menu.getMenuType(), newPosition);
		levelDownMenu.setPosition(oldPosition);
		menu.setPosition(newPosition);
		
		return "The menu ID ("+menu.getId()+") has been leveled up by one position.";
	}
	
	public String levelDownMenu(String alias) throws MenuNotFoundException {
		
		Menu menu = getByAlias(alias);
		
		if(menu.getMenuType().equals(MenuType.HEADER)) {
			if(menu.getPosition() == (getHeaderLastPosition() - 1)) {
				return "The menu ID ("+menu.getId()+") is already in last position.";
			}
		}
		else if(menu.getMenuType().equals(MenuType.FOOTER)) {
			if(menu.getPosition() == (getFooterLastPosition() - 1)) {
				return "The menu ID ("+menu.getId()+") is already in last position.";
			}
		}
		
		int oldPosition = menu.getPosition();
		int newPosition = oldPosition + 1;
		
		Menu levelUpMenu = menuRepository.findByMenuTypeAndPosition(menu.getMenuType(), newPosition);
		levelUpMenu.setPosition(oldPosition);
		menu.setPosition(newPosition);
		
		return "The menu ID ("+menu.getId()+") has been leveled down by one position.";
	}
	
	public void deleteMenu(String alias) throws MenuNotFoundException {
		Menu menu = getByAlias(alias);
		
		if(menu == null) {
			throw new MenuNotFoundException("No Menu found with alias ("+alias+").");
		}
		
		int deletedPosition = menu.getPosition();
		rearrangeMenus(deletedPosition, menu.getMenuType());
		menuRepository.delete(menu);
	}

	private void rearrangeMenus(int deletedPosition, MenuType type) {
		List<Menu> menusAfterPosition = menuRepository.findMenusAfterPositionForMenuType(deletedPosition, type);
		
		if(!menusAfterPosition.isEmpty()) {
			for(Menu menu : menusAfterPosition) {
				menu.setPosition(menu.getPosition()-1);
			}
		}
	}

	public void updateEnabledStatus(String menuAlias, boolean status) {
		menuRepository.updateEnabledStatus(menuAlias, status);
		
	}
	
	
}
