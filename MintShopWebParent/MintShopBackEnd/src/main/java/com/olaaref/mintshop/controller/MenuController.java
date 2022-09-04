package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.common.exception.MenuNotFoundException;
import com.olaaref.mintshop.service.ArticleService;
import com.olaaref.mintshop.service.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	private final String REDIRECT_URL = "redirect:/menu/list";
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/list")
	public String listAllMenus(Model model) {
		model.addAttribute("menus", menuService.getAllMenus());
		return "menus/list-menus";
	}
	
	@GetMapping("/new")
	public String newMenuForm(Model model) {
		model.addAttribute("menu", new Menu());
		model.addAttribute("articles", articleService.getMenuBoundArticles());
		model.addAttribute("pageTitle", "Add Menu");
		return "menus/menu-form";
	}
	
	@PostMapping("/save")
	public String saveMenu(@ModelAttribute("menu") Menu menu, 
						   Model model, 
						   RedirectAttributes redirectAttributes) {
		
		menuService.save(menu);
		
		redirectAttributes.addFlashAttribute("message", "Menu has been saved successfully.");
		return REDIRECT_URL;
	}
	
	@GetMapping("/edit/{menuAlias}")
	public String editMenuForm(@PathVariable("menuAlias") String menuAlias, 
							   Model model,
							   RedirectAttributes redirectAttributes) {
		Menu menu;
		try {
			menu = menuService.getByAlias(menuAlias);
			model.addAttribute("menu", menu);
			model.addAttribute("articles", articleService.getMenuBoundArticles());
			model.addAttribute("pageTitle", "Edit Menu");
			return "menus/menu-form";
			
		} catch (MenuNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
		
	}
	
	@GetMapping("/delete/{menuAlias}")
	public String deleteMenu(@PathVariable("menuAlias") String menuAlias,
							 RedirectAttributes redirectAttributes) {
		try {
			menuService.deleteMenu(menuAlias);
			redirectAttributes.addFlashAttribute("deleteMessag", "Menu has been deleted successfully.");
			return REDIRECT_URL;
		} catch (MenuNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
	
	@GetMapping("{menuAlias}/enabled/{status}")
	public String updateEnableStatus(@PathVariable("menuAlias") String menuAlias, 
							 @PathVariable("status") boolean status,
							 RedirectAttributes redirectAttributes) {
		
		menuService.updateEnabledStatus(menuAlias, status);
		
		String enabled = status ? "enabled" : "disabled";
		redirectAttributes.addFlashAttribute("enabledMessag", "Menu has been "+enabled+" successfully.");
		return REDIRECT_URL;
	}
	
	@GetMapping("{menuAlias}/level/{levelType}")
	public String changeMenuPosition(@PathVariable("menuAlias") String menuAlias, 
							 		@PathVariable("levelType") String levelType,
							 		RedirectAttributes redirectAttributes) {
		String message = "Error";
		try {
			if(levelType.equals("up")) {
				message = menuService.levelUpMenu(menuAlias);
			}
			else if(levelType.equals("down")) {
				message = menuService.levelDownMenu(menuAlias);
			}
			
			redirectAttributes.addFlashAttribute("changePositionMessage", message);
			return REDIRECT_URL;
			
		} 
		catch (MenuNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return REDIRECT_URL;
		}
	}
}

















