package com.olaaref.mintshop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.gcp.GoogleCloudUtility;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.UserService;


@Controller
@RequestMapping({"/account"})
public class AccountController {
  @Autowired
  private UserService userService;
  
  @GetMapping({"/details"})
  public String viewDetails(@AuthenticationPrincipal MintshopUserDetails loggedUser, Model model) {
    String email = loggedUser.getUsername();
    User user = this.userService.getUserByEmail(email);
    model.addAttribute("user", user);
    return "users/account-form";
  }
  
  @PostMapping({"/update"})
  public String updateAccount(@ModelAttribute("user") User theUser, @RequestParam("photo") MultipartFile multipartFile, @AuthenticationPrincipal MintshopUserDetails loggedUser, RedirectAttributes redirectAttributes) throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      theUser.setImage(fileName);
      saveUserImage(multipartFile, fileName, theUser);
    } else if (theUser.getImage().isEmpty()) {
      theUser.setImage(null);
    } 
    this.userService.updateAccount(theUser);
    redirectAttributes.addFlashAttribute("message", "Your account have been updated.");
    loggedUser.setFirstName(theUser.getFirstName());
    loggedUser.setLastName(theUser.getLastName());
    return "redirect:/account/details";
  }
  
  private void saveUserImage(MultipartFile multipartFile, String fileName, User theUser) throws IOException {
    if (!multipartFile.isEmpty()) {
      String uploadDir = "user-photos/" + theUser.getId();
      
      GoogleCloudUtility utility = new GoogleCloudUtility();
      String prefix = uploadDir + "/";
      utility.deleteFolder(prefix);
      utility.upload(multipartFile, prefix);
    } 
  }
}
