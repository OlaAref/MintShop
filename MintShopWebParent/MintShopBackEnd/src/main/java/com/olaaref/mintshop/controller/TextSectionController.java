package com.olaaref.mintshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section/text")
public class TextSectionController {
	@Autowired
	private SectionService sectionService;
	
	@GetMapping("/new")
	public String newBrandSectionForm(Model model) {
		Section section = new Section(true, SectionType.TEXT);
		
		model.addAttribute("section", section);
		model.addAttribute("pageHeader", "Text Section");
		model.addAttribute("pageTitle", "Add Text Section");
		return "sections/section-form";
	}
	
	@GetMapping("/edit/{sectionId}")
	public String editBrandSectionForm(@PathVariable("sectionId") Integer sectionId, 
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			Section section = sectionService.getById(sectionId);
			
			model.addAttribute("section", section);
			model.addAttribute("pageHeader", "Text Section");
			model.addAttribute("pageTitle", "Edit Text Section");
			return "sections/section-form";
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
}
