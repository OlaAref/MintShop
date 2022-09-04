package com.olaaref.mintshop.service;

import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.repository.MenuRepository;

@Service
@Transactional
public class MenuService {

	@Autowired
	private MenuRepository menuRepository;
	
	public List<Menu> getAllMenus(){
		List<Menu> menus = menuRepository.findAll();
		Comparator<Menu> byPosition = Comparator.comparingInt(Menu::getPosition);
		menus.sort(byPosition);
		return menus;
	}
}
