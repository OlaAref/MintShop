package com.olaaref.mintshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/checkEmail")
	public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email) {
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
	
	

}
