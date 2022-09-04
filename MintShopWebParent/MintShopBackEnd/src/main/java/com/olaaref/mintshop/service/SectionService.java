package com.olaaref.mintshop.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Article;
import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.entity.section.ArticleSection;
import com.olaaref.mintshop.common.entity.section.BrandSection;
import com.olaaref.mintshop.common.entity.section.CategorySection;
import com.olaaref.mintshop.common.entity.section.ProductSection;
import com.olaaref.mintshop.common.entity.section.Section;
import com.olaaref.mintshop.common.exception.SectionNotFoundException;
import com.olaaref.mintshop.dao.ArticleSectionRepository;
import com.olaaref.mintshop.dao.BrandSectionRepository;
import com.olaaref.mintshop.dao.CategorySectionRepository;
import com.olaaref.mintshop.dao.ProductSectionRepository;
import com.olaaref.mintshop.dao.SectionRepository;

@Service
@Transactional
public class SectionService {

	@Autowired
	private SectionRepository sectionRepository;
	
	@Autowired
	private ProductSectionRepository productSectionRepository;
	
	@Autowired
	private CategorySectionRepository categorySectionRepository;
	
	@Autowired
	private BrandSectionRepository brandSectionRepository;
	
	@Autowired
	private ArticleSectionRepository articleSectionRepository;
	
	public Section getById(Integer id) throws SectionNotFoundException {
		Section section = sectionRepository.findById(id).get();
		if(section == null) {
			throw new SectionNotFoundException("No section found with ID "+id);
		}
		return section;
	}
	
	public List<Section> getAllSections(){
		return sectionRepository.findAllByOrderByPosition();
	}
	
	public Set<CategorySection> getCategoriesSectionSorted(Section section){
		return (Set<CategorySection>) categorySectionRepository.findBySectionOrderByPositionAsc(section);
	}
	
	public List<ArticleSection> getArticlesSectionSorted(Section section){
		return articleSectionRepository.findBySectionOrderByPositionAsc(section);
	}
	
	public List<BrandSection> getBrandsSectionSorted(Section section) {
		return brandSectionRepository.findBySectionOrderByPositionAsc(section);
	}
	
	public String getSectionType(Integer sectionId) {
		return sectionRepository.getSectionType(sectionId);
	}
	
	public List<ProductSection> getProductsSectionSorted(Section section) {
		return productSectionRepository.findBySectionOrderByPositionAsc(section);
	}
	
	private int getSectionLastPosition() {
		Section section = sectionRepository.findFirstByOrderByPositionDesc();
		if(section == null) {
			return 1;
		}
		return section.getPosition() + 1;
	}
	
	public String levelUpSection(Integer sectionId) throws SectionNotFoundException {
		
		Section section = getById(sectionId);
		
		if(section.getPosition() == 1) {
			return "The section ID ("+section.getId()+") is already in first position.";
		}
		
		int oldPosition = section.getPosition();
		int newPosition = oldPosition - 1;
		
		Section prevSection = sectionRepository.findByPosition(newPosition);
		prevSection.setPosition(oldPosition);
		section.setPosition(newPosition);
		
		return "The section ID ("+section.getId()+") has been leveled up by one position.";
	}
	
	public String levelDownSection(Integer sectionId) throws SectionNotFoundException {
		
		Section section = getById(sectionId);
		
		if(section.getPosition() == getSectionLastPosition() - 1) {
			return "The section ID ("+section.getId()+") is already in last position.";
		}
		
		int oldPosition = section.getPosition();
		int newPosition = oldPosition + 1;
		
		Section nextSection = sectionRepository.findByPosition(newPosition);
		nextSection.setPosition(oldPosition);
		section.setPosition(newPosition);
		
		return "The section ID ("+section.getId()+") has been leveled down by one position.";
	}
	
	public void deleteSection(Integer sectionId) throws SectionNotFoundException {
		
		Section section = getById(sectionId);
		
		if(section == null) {
			throw new SectionNotFoundException("No section found with ID ("+sectionId+").");
		}
		
		int position = section.getPosition();
		sectionRepository.delete(section);
		rearrangeSections(position);
		
	}

	private void rearrangeSections(int position) {
		List<Section> sections = sectionRepository.findSectionsAfterPosition(position);
		if(!sections.isEmpty()) {
			for (Section section : sections) {
				section.setPosition(section.getPosition() - 1);
			}
		}
	}
	
	public void updateEnabledStatus(Integer sectionId, boolean status) {
		sectionRepository.updateEnabledStatus(sectionId, status);
	}

	/*
	 * Category Section business code
	 */
	public void saveAllCategoriesSection(Section section) {
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		sectionRepository.save(section);
		
	}

	public void saveCategorySection(Section section) {
		
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		
		Section savedSection = sectionRepository.save(section);
		
		List<Category> categories = section.getCategories();
		List<CategorySection> categoriesSection = new LinkedList<CategorySection>();
		
		convertCategoriesToCategoriesSection(savedSection, categories, categoriesSection);
		deleteRestOfCategoriesSection(categoriesSection, savedSection);
		
		savedSection.setCategoriesSection(categoriesSection);
		
		categoriesSection.forEach(cat -> cat.setSection(savedSection));
		categorySectionRepository.saveAll(categoriesSection);

	}

	private void convertCategoriesToCategoriesSection(Section section, List<Category> categories, List<CategorySection> categoriesSection) {
		
		for (int i = 0; i < categories.size(); i++) {
			int categoryId = categories.get(i).getId();
			int position = i + 1;
			
			CategorySection categorySection = categorySectionRepository.findBySectionAndCategory(section, new Category(categoryId));
			if(categorySection == null) {
				categorySection = new CategorySection(section, new Category(categoryId), position);
			}
			else {
				categorySection.setPosition(position);
			}
			
			categoriesSection.add(categorySection);
		}
	}

	private void deleteRestOfCategoriesSection(List<CategorySection> categoriesSection, Section section) {
		List<CategorySection> catSections = categorySectionRepository.findBySection(section);
		for (CategorySection cs : catSections) {
			if(!categoriesSection.contains(cs)) {
				categorySectionRepository.delete(cs);
			}
		}
	}

	/*
	 * Article Section business code
	 */
	public void saveArticleSection(Section section) {
		
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		
		Section savedSection = sectionRepository.save(section);
		
		List<Article> articles = section.getArticles();
		List<ArticleSection> articlesSection = new LinkedList<>();
		
		convertArticlesToArticlesSection(savedSection, articles, articlesSection);
		deleteRestOfArticlesSection(articlesSection, savedSection);
		
		savedSection.setArticlesSection(articlesSection);
		
		articlesSection.forEach(cat -> cat.setSection(savedSection));
		articleSectionRepository.saveAll(articlesSection);

	}
	
	private void convertArticlesToArticlesSection(Section section, List<Article> articles, List<ArticleSection> articlesSection) {
		
		for (int i = 0; i < articles.size(); i++) {
			int articleId = articles.get(i).getId();
			int position = i + 1;
			
			ArticleSection articleSection = articleSectionRepository.findBySectionAndArticle(section, new Article(articleId));
			if(articleSection == null) {
				articleSection = new ArticleSection(section, new Article(articleId), position);
			}
			else {
				articleSection.setPosition(position);
			}
			
			articlesSection.add(articleSection);
		}
	}

	private void deleteRestOfArticlesSection(List<ArticleSection> articlesSection, Section section) {
		List<ArticleSection> articleSections = articleSectionRepository.findBySection(section);
		for (ArticleSection as : articleSections) {
			if(!articlesSection.contains(as)) {
				articleSectionRepository.delete(as);
			}
		}
	}
	
	/*
	 * Brand Section business code
	 */
	public void saveBrandSection(Section section) {
		
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		
		Section savedSection = sectionRepository.save(section);
		
		List<Brand> brands = section.getBrands();
		List<BrandSection> brandsSection = new LinkedList<>();
		
		convertBrandsToBrandsSection(savedSection, brands, brandsSection);
		deleteRestOfBrandsSection(brandsSection, savedSection);
		
		savedSection.setBrandsSection(brandsSection);
		
		brandsSection.forEach(cat -> cat.setSection(savedSection));
		brandSectionRepository.saveAll(brandsSection);

	}
	
	private void convertBrandsToBrandsSection(Section section, List<Brand> brands, List<BrandSection> brandsSection) {
		
		for (int i = 0; i < brands.size(); i++) {
			int brandId = brands.get(i).getId();
			int position = i + 1;
			
			BrandSection brandSection = brandSectionRepository.findBySectionAndBrand(section, new Brand(brandId));
			if(brandSection == null) {
				brandSection = new BrandSection(section, new Brand(brandId), position);
			}
			else {
				brandSection.setPosition(position);
			}
			
			brandsSection.add(brandSection);
		}
	}

	private void deleteRestOfBrandsSection(List<BrandSection> brandsSection, Section section) {
		List<BrandSection> brandSections = brandSectionRepository.findBySection(section);
		for (BrandSection as : brandSections) {
			if(!brandsSection.contains(as)) {
				brandSectionRepository.delete(as);
			}
		}
	}

	/*
	 * Text Section business code
	 */
	public void saveTextSection(Section section) {
		
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		
		sectionRepository.save(section);

	}

	
	/*
	 * Product Section business code
	 */
	public void saveProductSection(Section section) {
		
		boolean isNew = (section.getId() == null || section.getId() == 0);
		if(isNew) {
			section.setPosition(getSectionLastPosition());
		}
		
		Section savedSection = sectionRepository.save(section);
		
		List<Product> products = section.getProducts();
		List<ProductSection> productsSection = new LinkedList<>();
		
		convertProductsToProductsSection(savedSection, products, productsSection);
		deleteRestOfProductsSection(productsSection, savedSection);
		
		savedSection.setProductsSection(productsSection);
		
		productsSection.forEach(cat -> cat.setSection(savedSection));
		productSectionRepository.saveAll(productsSection);
		
	}
	
	private void convertProductsToProductsSection(Section section, List<Product> products, List<ProductSection> productsSection) {
		
		for (int i = 0; i < products.size(); i++) {
			int productId = products.get(i).getId();
			int position = i + 1;
			
			ProductSection productSection = productSectionRepository.findBySectionAndProduct(section, new Product(productId));
			if(productSection == null) {
				productSection = new ProductSection(section, new Product(productId), position);
			}
			else {
				productSection.setPosition(position);
			}
			
			productsSection.add(productSection);
		}
	}

	private void deleteRestOfProductsSection(List<ProductSection> productsSection, Section section) {
		List<ProductSection> productSections = productSectionRepository.findBySection(section);
		for (ProductSection as : productSections) {
			if(!productsSection.contains(as)) {
				productSectionRepository.delete(as);
			}
		}
	}

	
	

}

















