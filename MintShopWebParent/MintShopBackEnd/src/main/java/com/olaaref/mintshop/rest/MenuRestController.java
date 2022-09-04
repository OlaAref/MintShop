package com.olaaref.mintshop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.service.MenuService;

@RestController
@RequestMapping("/menus")
public class MenuRestController {
	
	@Autowired
	private MenuService menuService;
	
	@PostMapping("/check-unique")
	public String checkUnique(@RequestParam("menuId") Integer menuId, 
							  @RequestParam("menuAlias") String alias) {
		return menuService.checkUnique(menuId, alias);
	}
	
	
}
