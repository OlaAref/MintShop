package com.olaaref.mintshop.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.section.BrandSection;
import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.BrandService;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section/brand")
public class BrandSectionController {
	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/new")
	public String newBrandSectionForm(Model model) {
		Section section = new Section(true, SectionType.BRAND);
		List<Brand> brands = brandService.listAll();
		
		model.addAttribute("section", section);
		model.addAttribute("brands", brands);
		model.addAttribute("pageHeader", "Brand Section");
		model.addAttribute("pageTitle", "Add Brand Section");
		return "sections/section-form";
	}
	
	@GetMapping("/edit/{sectionId}")
	public String editBrandSectionForm(@PathVariable("sectionId") Integer sectionId, 
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			Section section = sectionService.getById(sectionId);
			List<Brand> selectedBrands = getBrands(section);
			section.setBrands(selectedBrands);
			
			List<Brand> brands = brandService.listAll();
			
			model.addAttribute("section", section);
			model.addAttribute("brands", brands);
			model.addAttribute("pageHeader", "Brand Section");
			model.addAttribute("pageTitle", "Edit Brand Section");
			return "sections/section-form";
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
	
	private List<Brand> getBrands(Section section) {
		List<BrandSection> brandsSection = sectionService.getBrandsSectionSorted(section);
		
		List<Brand> articles = new LinkedList<>();
		for (BrandSection bs : brandsSection) {
			articles.add(new Brand(bs.getBrand().getId()));
		}
		return articles;
	}
}
