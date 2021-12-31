package com.olaaref.mintshop.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.olaaref.mintshop.aws.AmazonS3Util;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.entity.product.ProductImage;
import com.olaaref.mintshop.exception.ProductNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.BrandService;
import com.olaaref.mintshop.service.CategoryService;
import com.olaaref.mintshop.service.ProductService;

@Controller
@RequestMapping({"/product"})
public class ProductController {
  @Autowired
  private ProductService productService;
  
  @Autowired
  private BrandService brandService;
  
  @Autowired
  private CategoryService categoryService;
  
  @GetMapping({"/list"})
  public String listAllProducts(Model model) {
    return "redirect:/product/page/1?sortField=id&sortDir=asc&categoryID=0";
  }
  
  @GetMapping({"/page/{pageNum}"})
  public String listAllByPage(@PagingAndSortingParam(listName = "products", moduleUrl = "/product") PagingAndSortingHelper helper, @PathVariable("pageNum") int pageNum, @RequestParam("categoryID") Integer categoryID, Model model) {
    this.productService.listByPage(pageNum, categoryID, helper);
    model.addAttribute("categories", this.categoryService.listFormCategories());
    if (categoryID != null)
      model.addAttribute("categoryID", categoryID); 
    return "products/list-products";
  }
  
  @GetMapping({"/toAdd"})
  public String toAddNewProductPage(Model model) {
    Product product = new Product();
    product.setEnabled(true);
    product.setInStock(true);
    model.addAttribute("product", product);
    model.addAttribute("brands", this.brandService.listAll());
    model.addAttribute("pageTitle", "Add");
    model.addAttribute("noOfExtraImages", Integer.valueOf(0));
    return "products/product-form";
  }
  
  @PostMapping({"/add"})
  public String addNewProduct(@ModelAttribute("product") Product product, @RequestParam(name = "photo", required = false) MultipartFile multipartFile, @RequestParam(name = "extraPhoto", required = false) MultipartFile[] extraMultipartFiles, @RequestParam(name = "detailIDs", required = false) String[] detailIDs, @RequestParam(name = "detailNames", required = false) String[] detailNames, @RequestParam(name = "detailValues", required = false) String[] detailValues, @RequestParam(name = "imageIDs", required = false) String[] imageIDs, @RequestParam(name = "imageNames", required = false) String[] imageNames, @AuthenticationPrincipal MintshopUserDetails loggedUser, RedirectAttributes redirectAttributes) throws IOException, ProductNotFoundException {
    if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor") && 
      loggedUser.hasRole("Sales")) {
      this.productService.saveProductPrice(product);
      redirectAttributes.addFlashAttribute("message", product.getName());
      return "redirect:/product/list";
    } 
    Product dbProduct = null;
    if (product.getId() != null)
      dbProduct = this.productService.getById(product.getId()); 
    if (multipartFile.isEmpty() && dbProduct != null)
      product.setImage(dbProduct.getImage()); 
    if (product.getCreatedTime() == null && dbProduct != null)
      product.setCreatedTime(dbProduct.getCreatedTime()); 
    setMainImage(multipartFile, product);
    setExistingExtraImages(imageIDs, imageNames, product);
    setExtraImages(product, extraMultipartFiles);
    setProductDetails(product, detailNames, detailValues, detailIDs);
    Product savedProduct = this.productService.save(product);
    saveUploadedImages(multipartFile, extraMultipartFiles, savedProduct);
    deleteExtraImagesWereDeletedInForm(product);
    redirectAttributes.addFlashAttribute("message", product.getName());
    return "redirect:/product/list";
  }
  
  private void saveUploadedImages(MultipartFile multipartFile, MultipartFile[] extraMultipartFiles, Product savedProduct) throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      String uploadDir = "product-images/" + savedProduct.getId();
      List<String> objectKeysList = AmazonS3Util.listFolder(uploadDir + "/");
      for (String key : objectKeysList) {
        if (!key.contains("/extras/"))
          AmazonS3Util.deleteFile(key); 
      } 
      AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
    } 
    if (extraMultipartFiles.length > 0) {
      String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
      for (MultipartFile image : extraMultipartFiles) {
        if (!image.isEmpty()) {
          String fileName = StringUtils.cleanPath(image.getOriginalFilename());
          AmazonS3Util.uploadFile(uploadDir, fileName, image.getInputStream());
        } 
      } 
    } 
  }
  
  private void setMainImage(MultipartFile multipartFile, Product product) throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      product.setImage(fileName);
    } 
  }
  
  private void setExistingExtraImages(String[] imageIDs, String[] imageNames, Product product) {
    if (imageIDs == null || imageIDs.length == 0)
      return; 
    Set<ProductImage> images = new HashSet<>();
    for (int i = 0; i < imageIDs.length; i++) {
      Integer id = Integer.valueOf(Integer.parseInt(imageIDs[i]));
      String fileName = imageNames[i];
      images.add(new ProductImage(id, fileName, product));
    } 
    product.setImages(images);
  }
  
  private void setExtraImages(Product product, MultipartFile[] extraMultipartFiles) throws IOException {
    if (extraMultipartFiles != null)
      if (extraMultipartFiles.length > 0)
        for (MultipartFile image : extraMultipartFiles) {
          if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            if (!product.conatinsImage(fileName))
              product.addExtraImage(fileName); 
          } 
        }   
  }
  
  private void setProductDetails(Product product, String[] detailNames, String[] detailValues, String[] detailIDs) {
    if (detailNames == null || detailNames.length == 0)
      return; 
    for (int i = 0; i < detailNames.length; i++) {
      Integer id = Integer.valueOf(Integer.parseInt(detailIDs[i]));
      String name = detailNames[i];
      String value = detailValues[i];
      if (id.intValue() != 0) {
        product.addProductDetail(id, name, value);
      } else if (!name.isEmpty() && !value.isEmpty()) {
        product.addProductDetail(name, value);
      } 
    } 
  }
  
  public void deleteExtraImagesWereDeletedInForm(Product product) {
    String extraImageDir = "product-images/" + product.getId() + "/extras";
    List<String> objectKeysList = AmazonS3Util.listFolder(extraImageDir);
    for (String key : objectKeysList) {
      int lastIndexOfSlash = key.lastIndexOf("/");
      String fileName = key.substring(lastIndexOfSlash + 1, key.length());
      if (!product.conatinsImage(fileName))
        AmazonS3Util.deleteFile(key); 
    } 
  }
  
  @GetMapping({"{id}/enabled/{status}"})
  public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes redirectAttributes) throws ProductNotFoundException {
    this.productService.updateEnabledStatus(id, status);
    String enabled = status ? "enabled" : "disabled";
    String message = "The product " + this.productService.getById(id).getName() + " has been " + enabled + " successfully.";
    redirectAttributes.addFlashAttribute("enabledMessag", message);
    return "redirect:/product/list";
  }
  
  @GetMapping({"/load/{id}"})
  public String loadProduct(@PathVariable("id") Integer id, @AuthenticationPrincipal MintshopUserDetails loggedUser, Model model, RedirectAttributes redirectAttributes) {
    try {
      Product product = this.productService.getById(id);
      Integer noOfExtraImages = Integer.valueOf(product.getImages().size());
      if (noOfExtraImages == null)
        noOfExtraImages = Integer.valueOf(0); 
      boolean isReadOnly = false;
      if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor") && 
        loggedUser.hasRole("Sales"))
        isReadOnly = true; 
      model.addAttribute("product", product);
      model.addAttribute("brands", this.brandService.listAll());
      model.addAttribute("pageTitle", "Edit");
      model.addAttribute("noOfExtraImages", noOfExtraImages);
      model.addAttribute("isReadOnly", Boolean.valueOf(isReadOnly));
      return "products/product-form";
    } catch (ProductNotFoundException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/product/list";
    } 
  }
  
  @GetMapping({"/details/{id}"})
  public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Product product = this.productService.getById(id);
      model.addAttribute("product", product);
      return "products/product-details-modal";
    } catch (ProductNotFoundException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/product/list";
    } 
  }
  
  @GetMapping({"/delete/{id}"})
  public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws ProductNotFoundException {
    String name = this.productService.getById(id).getName();
    try {
      this.productService.deleteProduct(id);
      String uploadDir = "product-images/" + id;
      AmazonS3Util.removeFolder(uploadDir);
      redirectAttributes.addFlashAttribute("deleteMessag", "Product " + name + " has been deleted successfully.");
    } catch (ProductNotFoundException e) {
      redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
    } 
    return "redirect:/product/list";
  }
}

















