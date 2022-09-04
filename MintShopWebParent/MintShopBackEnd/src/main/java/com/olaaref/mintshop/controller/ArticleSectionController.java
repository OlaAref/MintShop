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

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.section.ArticleSection;
import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.entity.section.SectionType;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.service.ArticleService;
import com.olaaref.mintshop.service.SectionService;

@Controller
@RequestMapping("/section/article")
public class ArticleSectionController {

	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/new")
	public String newArticleSectionForm(Model model) {
		Section section = new Section(true, SectionType.ARTICLE);
		List<Article> articles = articleService.listAllArticles();
		
		model.addAttribute("section", section);
		model.addAttribute("articles", articles);
		model.addAttribute("pageHeader", "Article Section");
		model.addAttribute("pageTitle", "Add Article Section");
		return "sections/section-form";
	}
	
	@GetMapping("/edit/{sectionId}")
	public String newArticleSectionForm(@PathVariable("sectionId") Integer sectionId, 
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			Section section = sectionService.getById(sectionId);
			List<Article> selectedArticles = getArticles(section);
			section.setArticles(selectedArticles);
			
			List<Article> articles = articleService.listAllArticles();
			
			model.addAttribute("section", section);
			model.addAttribute("articles", articles);
			model.addAttribute("pageHeader", "Article Section");
			model.addAttribute("pageTitle", "Edit Article Section");
			return "sections/section-form";
		} catch (SectionNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/section/list";
		}
	}
	
	private List<Article> getArticles(Section section) {
		List<ArticleSection> articlesSection = sectionService.getArticlesSectionSorted(section);
		
		List<Article> articles = new LinkedList<>();
		for (ArticleSection art : articlesSection) {
			articles.add(new Article(art.getArticle().getId()));
		}
		return articles;
	}
}
