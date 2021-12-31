package com.olaaref.mintshop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.aws.AmazonS3Util;
import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.common.entity.Currency;
import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.dao.CurrencyRepository;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.setting.GeneralSettingBag;

@Controller
@RequestMapping({ "/setting" })
public class SettingController {
	@Autowired
	private SettingService settingService;

	@Autowired
	private CurrencyRepository currencyRepository;

	@GetMapping({ "/list" })
	public String listAll(Model model) {
		List<Setting> settings = this.settingService.listAllSettings();
		List<Currency> currencies = this.currencyRepository.findAllByOrderByNameAsc();
		model.addAttribute("currencies", currencies);
		for (Setting setting : settings)
			model.addAttribute(setting.getKey(), setting.getValue());
		String s3Uri = Constants.S3_BASE_URI;
		model.addAttribute("S3_BASE_URI", s3Uri);
		return "settings/list-settings";
	}

	@PostMapping({ "/saveGeneral" })
	public String saveGeneralSettings(@RequestParam("photo") MultipartFile multipartFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {
		GeneralSettingBag generalSettingsBag = this.settingService.getGeneralSettings();
		saveSiteLogo(multipartFile, generalSettingsBag);
		saveCurrencySymbol(request, generalSettingsBag);
		updateSettingsValues(request, generalSettingsBag.list());
		redirectAttributes.addFlashAttribute("message", "General Setting have been saved.");
		return "redirect:/setting/list";
	}

	private void updateSettingsValues(HttpServletRequest request, List<Setting> settingList) {
		for (Setting setting : settingList) {
			String value = request.getParameter(setting.getKey());
			if (value != null)
				setting.setValue(value);
		}
		this.settingService.saveAll(settingList);
	}

	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingsBag) {
		Integer currencyId = Integer.valueOf(Integer.parseInt(request.getParameter("CURRENCY_ID")));
		Optional<Currency> currencyById = this.currencyRepository.findById(currencyId);
		if (currencyById.isPresent()) {
			Currency currency = currencyById.get();
			generalSettingsBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingsBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = "site-logo";
			String value = "/site-logo/" + filename;
			generalSettingsBag.updateSiteLogo(value);
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, filename, multipartFile.getInputStream());
		}
	}

	@PostMapping({ "/saveMailServer" })
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSettings = this.settingService.getMailServerSettings();
		updateSettingsValues(request, mailServerSettings);
		redirectAttributes.addFlashAttribute("message", "Mail Server Settings have been saved.");
		return "redirect:/setting/list";
	}

	@PostMapping({ "/saveMailTemplates" })
	public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplateSettings = this.settingService.getMailTemplateSettings();
		updateSettingsValues(request, mailTemplateSettings);
		redirectAttributes.addFlashAttribute("message", "Mail Template Settings have been saved.");
		return "redirect:/setting/list";
	}

	@PostMapping({ "/savePayment" })
	public String savePaymentSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> paymentSettings = this.settingService.getPaymentSettings();
		updateSettingsValues(request, paymentSettings);
		redirectAttributes.addFlashAttribute("message", "Payment Settings have been saved.");
		return "redirect:/setting/list";
	}
}