package com.olaaref.mintshop.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@GetMapping("/list")
	public String listAll(Model model) {
		
		Page<User> userPage = userService.listAllUsersByPage(1, "id", "asc", "");
		
		getPageData(1, model, userPage, "id", "asc", "");
		
		return "list-users";
	}
	
	@GetMapping("/page/{pageNum}")
	public String listAllByPage(@PathVariable("pageNum") int pageNum,
								@RequestParam("sortField") String sortField,
								@RequestParam("sortDir") String sortDir,
								@RequestParam("keyword") String keyword,
								Model model) {
		
		Page<User> userPage = userService.listAllUsersByPage(pageNum, sortField, sortDir, keyword);
		
		getPageData(pageNum, model, userPage, sortField, sortDir, keyword);
		
		return "list-users";
	}

	private void getPageData(int pageNum, Model model, Page<User> userPage, String sortField, String sortDir, String keyword) {
		
		long start = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long end = start + UserService.USERS_PER_PAGE - 1;
		if(end > userPage.getTotalElements()) end = userPage.getTotalElements();
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("users", userPage.getContent());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", userPage.getTotalElements());
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
	}
	
	@GetMapping("/toAdd")
	public String toAddPage(Model model) {
		List<Role> roles = userService.listAllRoles();
		model.addAttribute("user", new User());
		model.addAttribute("roles", roles);
		model.addAttribute("pageTitle", "Add");
		return "add-user";
	}
	
	@PostMapping("/add")
	public String addUser(@ModelAttribute("user") User theUser, 
						  @RequestParam("photo") MultipartFile multipartFile,
						  RedirectAttributes redirectAttributes) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			theUser.setImage(multipartFile.getBytes());
		}
		else {
			theUser.setImage(null);
		}
		
		userService.saveUser(theUser);
		redirectAttributes.addFlashAttribute("message", theUser.getFirstName());
		
		String firstPArtOfEmail = theUser.getEmail().split("@")[0];
		return "redirect:/user/page/1?sortField=id&sortDir=asc&keyword="+firstPArtOfEmail;
	}
	
	@GetMapping("/load/{id}")
	public String loadUserDetails(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			List<Role> roles = userService.listAllRoles();
			model.addAttribute("roles", roles);
			User user = userService.getById(id);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit");
			return "add-user";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/user/list";
		}
		
	}
	
	@GetMapping("/showChangePassword/{id}")
	public String showChangePassword(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("id", id);
		return "change-password";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("id") Integer id, 
								@RequestParam("currentPassword") String currentPassword,
								@RequestParam("newPassword") String newPassword,
								@RequestParam("confirmNewPassword") String confirmNewPassword,
								Model model,
								RedirectAttributes redirectAttributes) throws UserNotFoundException {
		
		User user = userService.getById(id);
		
		//check if type correct password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean correctPassword = passwordEncoder.matches(currentPassword, user.getPassword());
		if(!correctPassword) {
			model.addAttribute("wrongPassword", "This Password Is Not Correct.");
			model.addAttribute("id", id);
			return "change-password";
		}
		
		//check if new password matched
		if(!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("notMatchedPassword", "Passwords Are Not Matched.");
			model.addAttribute("id", id);
			return "change-password";
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("passwordChanged", "Password Changed Successfully.");
		
		return "redirect:/user/list";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws UserNotFoundException {
		String name = userService.getById(id).getFirstName();
		try {
			userService.deleteUser(id);
			redirectAttributes.addFlashAttribute("deleteMessag", "User "+name+" has been deleted successfully.");
		} 
		catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
		}
		
		return "redirect:/user/list";
	}
	
	@GetMapping("/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, 
										  @PathVariable("status") boolean status,
										  RedirectAttributes redirectAttributes) throws UserNotFoundException {
		
		userService.updateEnabledStatus(id, status);
		String enabled = status ? "enabled" : "disabled";
		String message = "The user "+userService.getById(id).getFirstName()+" has been "+enabled+" successfully.";
		redirectAttributes.addFlashAttribute("enabledMessag", message);
		
		return "redirect:/user/list";
	}
	
	@GetMapping("/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> usersList = userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(usersList, response);
	}
	
	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> usersList = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(usersList, response);
	}
	
	@GetMapping("/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> usersList = userService.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(usersList, response);
	}
	
}









