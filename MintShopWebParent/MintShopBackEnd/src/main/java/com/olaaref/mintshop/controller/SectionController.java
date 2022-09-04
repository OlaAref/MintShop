package com.olaaref.mintshop.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section")
public class SectionController {
	
	@Autowired
	private SectionService sectionService;
	
	@GetMapping("/list")
	public String listAllSections(Model model) {
		List<Section> sections = sectionService.getAllSections();
		model.addAttribute("sections", sections);
		return "sections/list-sections";
	}

	@PostMapping("/save")
	public String saveSection(@ModelAttribute("section") Section section, 
							  Model model,
							  RedirectAttributes redirectAttributes) {
		saveSection(section);
		redirectAttributes.addFlashAttribute("message", "Section has been saved successfully.");
		return "redirect:/section/list";
	}

	private void saveSection(Section section) {
		SectionType type = section.getSectionType();
		
		switch (type) {
		case ALL_CATEGORIES:
			sectionService.saveAllCategoriesSection(section);
			break;
			
		case CATEGORY:
			sectionService.saveCategorySection(section);
			break;
			
		case PRODUCT:
			sectionService.saveProductSection(section);
			break;
			
		case ARTICLE:
			sectionService.saveArticleSection(section);
			break;
			
		case BRAND:
			sectionService.saveBrandSection(section);
			break;
			
		case TEXT:
			sectionService.saveTextSection(section);
			break;

		default:
			break;
		}
		
	}
	
	@GetMapping("/edit/{sectionId}")
	public String redirectToEditForm(@PathVariable("sectionId") Integer sectionId) {
		String type = sectionService.getSectionType(sectionId);
		switch (type) {
		case "ALL_CATEGORIES":
			return "redirect:/section/category/edit/"+sectionId;
			
		case "CATEGORY":
			return "redirect:/section/category/edit/"+sectionId;
			
		case "PRODUCT":
			return "redirect:/section/product/edit/"+sectionId;
			
		case "ARTICLE":
			return "redirect:/section/article/edit/"+sectionId;
			
		case "BRAND":
			return "redirect:/section/brand/edit/"+sectionId;
			
		case "TEXT":
			return "redirect:/section/text/edit/"+sectionId;

		default:
			return "redirect:/section/category/edit/"+sectionId;
		}
		
	}
	
	@GetMapping("/{sectionId}/enabled/{status}")
	public String changeEnabledStatus(@PathVariable("sectionId") Integer sectionId,
									  @PathVariable("status") boolean status,
									  RedirectAttributes redirectAttributes) {
		sectionService.updateEnabledStatus(sectionId, status);
		redirectAttributes.addFlashAttribute("enabledMessag", "Section enabled status changed successfully.");
		return "redirect:/section/list";
	}
	
	@GetMapping("/{sectionId}/level/{status}")
	public String changeSectionPosition(@PathVariable("sectionId") Integer sectionId,
									  @PathVariable("status") String status,
									  RedirectAttributes redirectAttributes) {
		
		try {
			String message = "";
			
			if(status.equals("up")) {
				message = sectionService.levelUpSection(sectionId);
			}
			else if(status.equals("down")) {
				message = sectionService.levelDownSection(sectionId);
			}
			
			redirectAttributes.addFlashAttribute("changePositionMessage", message);
			return "redirect:/section/list";
			
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
	
	@GetMapping("/delete/{sectionId}")
	public String deleteSection(@PathVariable("sectionId") Integer sectionId,
								RedirectAttributes redirectAttributes) {
		try {
			sectionService.deleteSection(sectionId);
			
			redirectAttributes.addFlashAttribute("deleteMessag", "Section with ID (" + sectionId + ") has been deleted successfully.");
			return "redirect:/section/list";
		} 
		catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
}






























