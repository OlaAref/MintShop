package com.olaaref.mintshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.common.entity.setting.SettingCategory;
import com.olaaref.mintshop.dao.SettingRepository;
import com.olaaref.mintshop.setting.GeneralSettingBag;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;
	
	public List<Setting> listAllSettings(){
		return settingRepository.findAll();
	}
	
	public GeneralSettingBag getGeneralSettings() {
		
		List<Setting> settings = new ArrayList<Setting>();
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	
	public List<Setting> getCurrencySettings(){
		return settingRepository.findByCategory(SettingCategory.CURRENCY);
	}
	
	public List<Setting> getMailServerSettings(){
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getMailTemplateSettings(){
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
	
	public List<Setting> getPaymentSettings(){
		return settingRepository.findByCategory(SettingCategory.PAYMENT);
	}
}
