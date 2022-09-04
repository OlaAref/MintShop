package com.olaaref.mintshop.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.exception.CustomerNotFoundException;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.service.CustomerService;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.setting.EmailSettingBag;

@Controller
@RequestMapping("/password")
public class ForgotPasswordController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/requestPassword")
	public String showRequestForm() {
		return "customer/forget-password-form";
	}
	
	@PostMapping("/forgotPassword")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteUrl(request) + "/password/resetPassword?token=" + token;
			sendEmail(link, email);
			model.addAttribute("message", "Reset password email have sent to your email, please check.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Could not send the email");
		} 
		return "customer/forget-password-form";
	}
	
	@GetMapping("/resetPassword")
	public String showResetForm(@RequestParam("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if(customer != null) {
			model.addAttribute("token", token);
		}
		else {
			model.addAttribute("messageTitle", "Fail");
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		return "customer/reset-password-form";
	}
	
	@PostMapping("/resetPassword")
	public String processResetForm(HttpServletRequest request, Model model) {
		String password = request.getParameter("password");
		String token = request.getParameter("token");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("messageTitle", "Success");
			model.addAttribute("message", "Password Successfully Changed.");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("messageTitle", "Fail");
			model.addAttribute("message", e.getMessage());
			return "message";
		}
		
	}
	
	public void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSender mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Reset Password";
		String content = """
			<div style="text-align: center;"><span style="font-size: 24px; font-weight: var(--bs-body-font-weight); text-align: var(--bs-body-text-align);">&nbsp;Hello,&nbsp;</span></div>
			<div style="text-align: center;"><span style="font-size: 18px;"><br></span></div>
			<span style="font-size: 18px;">
			    <div style="text-align: center;">
					<span style="font-weight: var(--bs-body-font-weight); text-align: var(--bs-body-text-align);">You have requested to reset your password.</span>
					<span style="font-weight: var(--bs-body-font-weight); text-align: var(--bs-body-text-align);">Click the link below to change your password.</span>
				</div>
			</span>
			<div>
			   <div style="text-align: center;"><span style="font-size: 18px;"><br></span></div>
			   <h2 style="text-align: center;">
			      <a href="%s" target="_self">Change my password</a>
			   </h2>
			   <div style="text-align: center;"><br></div>
			   <div style="text-align: center;">Ignore this email if you do remember the password, or you have not made this request.</div>
			   <div style="text-align: center;"><br></div>
			   <div style="text-align: center;">Cheers,</div>
			   <div style="text-align: center;">Mintshop Team</div>
			</div>
				""".formatted(link);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
	}

}
