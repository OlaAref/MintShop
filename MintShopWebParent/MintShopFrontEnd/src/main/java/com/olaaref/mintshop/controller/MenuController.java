package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.olaaref.mintshop.service.MenuService;

@Controller
public class MenuController {

	@Autowired
	private MenuService menuService;
}
