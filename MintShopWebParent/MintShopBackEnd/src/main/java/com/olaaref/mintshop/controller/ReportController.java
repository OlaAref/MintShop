package com.olaaref.mintshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.service.SettingService;

@Controller
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private SettingService settingService;

	@GetMapping("/")
	public String showSalesReportsHome(HttpServletRequest request) {
		loadCurrencySettings(request);
		return "reports/report-home";
	}

	private void loadCurrencySettings(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}
		
	}
}
