package com.olaaref.mintshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.Currency;
import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.common.entity.setting.SettingCategory;
import com.olaaref.mintshop.repository.CurrencyRepository;
import com.olaaref.mintshop.repository.SettingRepository;
import com.olaaref.mintshop.setting.CurrencySettingBag;
import com.olaaref.mintshop.setting.EmailSettingBag;
import com.olaaref.mintshop.setting.PaymentSettingBag;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	
	public List<Setting> listAllSettings(){
		return settingRepository.findAll();
	}
	
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	
	public EmailSettingBag getEmailSettings() {
		
	 	List<Setting> mailServerSettings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	 	List<Setting> mailTemplatesSettings = settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	 	List<Setting> mailSettings = new ArrayList<Setting>();
	 	mailSettings.addAll(mailServerSettings);
	 	mailSettings.addAll(mailTemplatesSettings);
	 	
	 	return new EmailSettingBag(mailSettings);
	 	
	}
	
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(currencySettings);
	}
	
	public PaymentSettingBag getPaymentSettings() {
		List<Setting> paymentSettings = settingRepository.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(paymentSettings);
	}
	
	public String getCurrencyCode() {
		Setting setting = settingRepository.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepository.findById(currencyId).get();
		return currency.getCode();
	}
	
}


















