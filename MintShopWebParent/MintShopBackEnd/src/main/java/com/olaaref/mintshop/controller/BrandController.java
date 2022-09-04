package com.olaaref.mintshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.exception.BrandNotFoundException;
import com.olaaref.mintshop.exporter.csv.BrandCsvExporter;
import com.olaaref.mintshop.exporter.excel.BrandExcelExporter;
import com.olaaref.mintshop.exporter.pdf.BrandPdfExporter;
import com.olaaref.mintshop.gcp.GoogleCloudUtility;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.service.BrandService;
import com.olaaref.mintshop.service.CategoryService;

@Controller
@RequestMapping({"/brand"})
public class BrandController {
  @Autowired
  private BrandService brandService;
  
  @Autowired
  private CategoryService categoryService;
  
  @GetMapping({"/list"})
  public String listAllBrand() {
    return "redirect:/brand/page/1?sortField=id&sortDir=asc";
  }
  
  @GetMapping({"/page/{pageNum}"})
  public String listAllByPage(@PagingAndSortingParam(listName = "brands", moduleUrl = "/brand") PagingAndSortingHelper helper, @PathVariable("pageNum") int pageNum) {
    this.brandService.listAllBrandsByPage(pageNum, helper);
    return "brands/list-brands";
  }
  
  @GetMapping({"/toAdd"})
  public String toAddPage(Model model) {
    List<Category> categories = this.categoryService.listFormCategories();
    model.addAttribute("brand", new Brand());
    model.addAttribute("categories", categories);
    model.addAttribute("pageTitle", "Add");
    return "brands/brand-form";
  }
  
  @PostMapping({"/add"})
  public String addBrand(@ModelAttribute("brand") Brand brand, @RequestParam("photo") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException, BrandNotFoundException {
    String fileName = null;
    if (!multipartFile.isEmpty()) {
      fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      brand.setLogo(fileName);
    } else if (brand.getLogo().isEmpty()) {
      brand.setLogo(null);
    } 
    Brand savedBrand = this.brandService.save(brand);
    uploadBrandLogo(multipartFile, fileName, savedBrand);
    redirectAttributes.addFlashAttribute("message", brand.getName());
    return "redirect:/brand/list?sortField=id&sortDir=asc&keyword=" + brand.getName();
  }
  
  private void uploadBrandLogo(MultipartFile multipartFile, String fileName, Brand brand) throws IOException {
    if (!multipartFile.isEmpty()) {
      String uploadDir = "brand-logos/" + brand.getId();
      
      GoogleCloudUtility utility = new GoogleCloudUtility();
      String prefix = uploadDir + "/";
      utility.deleteFolder(prefix);
      utility.upload(multipartFile, prefix);
    } 
  }
  
  @GetMapping({"/load/{id}"})
  public String loadBrandDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      List<Category> categories = this.categoryService.listFormCategories();
      Brand theBrand = this.brandService.getById(id);
      model.addAttribute("brand", theBrand);
      model.addAttribute("categories", categories);
      model.addAttribute("pageTitle", "Edit");
      return "brands/brand-form";
    } catch (BrandNotFoundException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/brand/list";
    } 
  }
  
  @GetMapping({"/delete/{id}"})
  public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws BrandNotFoundException, FileNotFoundException, IOException {
    try {
      String name = this.brandService.getById(id).getName();
      this.brandService.deleteBrand(id);
      String uploadDir = "brand-logos/" + id;
      GoogleCloudUtility utility = new GoogleCloudUtility();
      String prefix = uploadDir + "/";
      utility.deleteFolder(prefix);
      redirectAttributes.addFlashAttribute("deleteMessag", "Brand " + name + " has been deleted successfully.");
    } catch (BrandNotFoundException e) {
      redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
    } 
    return "redirect:/brand/list";
  }
  
  @GetMapping({"/export/csv"})
  public void exportToCsv(HttpServletResponse response) throws IOException {
    List<Brand> brandsList = this.brandService.listAll();
    BrandCsvExporter exporter = new BrandCsvExporter();
    exporter.export(brandsList, response);
  }
  
  @GetMapping({"/export/excel"})
  public void exportToExcel(HttpServletResponse response) throws IOException {
    List<Brand> brandsList = this.brandService.listAll();
    BrandExcelExporter exporter = new BrandExcelExporter();
    exporter.export(brandsList, response);
  }
  
  @GetMapping({"/export/pdf"})
  public void exportToPdf(HttpServletResponse response) throws IOException {
    List<Brand> brandsList = this.brandService.listAll();
    BrandPdfExporter exporter = new BrandPdfExporter();
    exporter.export(brandsList, response);
  }
}
