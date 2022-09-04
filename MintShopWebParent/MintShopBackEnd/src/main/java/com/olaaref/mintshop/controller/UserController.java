package com.olaaref.mintshop.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


import com.olaaref.mintshop.common.entity.Role;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exception.UserNotFoundException;
import com.olaaref.mintshop.exporter.csv.UserCsvExporter;
import com.olaaref.mintshop.exporter.excel.UserExcelExporter;
import com.olaaref.mintshop.exporter.pdf.UserPdfExporter;
import com.olaaref.mintshop.gcp.GoogleCloudUtility;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.UserService;

@Controller
@RequestMapping({ "/user" })
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping({ "/list" })
	public String listAll() {
		return "redirect:/user/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping({ "/page/{pageNum}" })
	public String listAllByPage(@PagingAndSortingParam(listName = "users", moduleUrl = "/user") PagingAndSortingHelper helper,
								@PathVariable("pageNum") int pageNum) {
		
		this.userService.listAllUsersByPage(pageNum, helper);
		return "users/list-users";
	}

	@GetMapping({ "/toAdd" })
	public String toAddPage(Model model) {
		
		List<Role> roles = this.userService.listAllRoles();
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", roles);
		model.addAttribute("pageTitle", "Add");
		
		return "users/add-user";
	}

	@PostMapping({ "/add" })
	public String addUser(@ModelAttribute("user") User theUser, 
						  @RequestParam("photo") MultipartFile multipartFile,
						  RedirectAttributes redirectAttributes) throws IOException, UserNotFoundException {
		
		String fileName = null;
		
		if (!multipartFile.isEmpty()) {
			fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			theUser.setImage(fileName);
			
		} else if (theUser.getImage().isEmpty()) {
			theUser.setImage(null);
		}
		
		User savedUser = this.userService.saveUser(theUser);
		saveUserImage(multipartFile, fileName, savedUser);
		
		redirectAttributes.addFlashAttribute("message", theUser.getFirstName());
		
		String firstPArtOfEmail = theUser.getEmail().split("@")[0];
		return "redirect:/user/page/1?sortField=id&sortDir=asc&keyword=" + firstPArtOfEmail;
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

	@GetMapping({ "/load/{id}" })
	public String loadUserDetails(@PathVariable("id") Integer id, 
								RedirectAttributes redirectAttributes, 
								Model model) {
		try {
			List<Role> roles = this.userService.listAllRoles();
			model.addAttribute("roles", roles);
			
			User user = this.userService.getById(id);
			model.addAttribute("user", user);
			
			model.addAttribute("pageTitle", "Edit");
			
			return "users/add-user";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/user/list";
		}
	}

	@GetMapping({ "/showChangePassword/{id}" })
	public String showChangePassword(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("id", id);
		return "change-password";
	}

	@PostMapping({ "/changePassword" })
	public String changePassword(@RequestParam("id") Integer id,
								@RequestParam("currentPassword") String currentPassword, 
								@RequestParam("newPassword") String newPassword,
								@RequestParam("confirmNewPassword") String confirmNewPassword,
								@AuthenticationPrincipal MintshopUserDetails loggedUser, 
								Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
		
		User user = this.userService.getById(id);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		boolean correctPassword = passwordEncoder.matches(currentPassword, user.getPassword());
		
		if (!correctPassword) {
			model.addAttribute("wrongPassword", "This Password Is Not Correct.");
			model.addAttribute("id", id);
			return "change-password";
		}
		if (!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("notMatchedPassword", "Passwords Are Not Matched.");
			model.addAttribute("id", id);
			return "change-password";
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		
		this.userService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("passwordChanged", "Password Changed Successfully.");
		
		if (loggedUser.getUsername().equals(user.getEmail())) return "redirect:/account/details";
		
		return "redirect:/user/list";
	}

	@GetMapping({ "/delete/{id}" })
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws UserNotFoundException, FileNotFoundException, IOException {
		
		String name = this.userService.getById(id).getFirstName();
		
		try {
			this.userService.deleteUser(id);
			String uploadDir = "user-photos/" + id;
			
			GoogleCloudUtility utility = new GoogleCloudUtility();
			String prefix = uploadDir + "/";
			utility.deleteFolder(prefix);
			
			redirectAttributes.addFlashAttribute("deleteMessag", "User " + name + " has been deleted successfully.");
			
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
		}
		return "redirect:/user/list";
	}

	@GetMapping({ "/{id}/enabled/{status}" })
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, 
										 @PathVariable("status") boolean status,
										 RedirectAttributes redirectAttributes) throws UserNotFoundException {
		
		this.userService.updateEnabledStatus(id, status);
		
		String enabled = status ? "enabled" : "disabled";
		String message = "The user " + this.userService.getById(id).getFirstName() + " has been " + enabled
				+ " successfully.";
		
		redirectAttributes.addFlashAttribute("enabledMessag", message);
		return "redirect:/user/list";
	}

	@GetMapping({ "/export/csv" })
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> usersList = this.userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(usersList, response);
	}

	@GetMapping({ "/export/excel" })
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> usersList = this.userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(usersList, response);
	}

	@GetMapping({ "/export/pdf" })
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> usersList = this.userService.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(usersList, response);
	}
}
