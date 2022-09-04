package com.olaaref.mintshop.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.common.entity.setting.SettingCategory;
import com.olaaref.mintshop.dao.SettingRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired
	private SettingRepository settingRepository;
	
	@Test
	public void testGetAllSetting() {
		List<Setting> settings = settingRepository.findAll();
		settings.forEach(setting -> System.out.println(setting));
		
	}
	
	@Test
	public void testCreateGeneralSetting() {
		//Setting setting = new Setting("SITE_NAME", "Mintshop", SettingCategory.GENERAL);
		Setting setting2 = new Setting("SITE_LOGO", "MintShop.png", SettingCategory.GENERAL);
		Setting setting3 = new Setting("COPYRIGHT", "Copyright (C) 2021 mintshop Ltd.", SettingCategory.GENERAL);
		
		
		settingRepository.saveAll(List.of(setting2, setting3));
	}
	
	@Test
	public void testCreateCurrencySetting() {
		Setting setting1 = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting setting2 = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting setting3 = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting setting4 = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting setting5 = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting setting6 = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		
		settingRepository.saveAll(List.of(setting1, setting2, setting3, setting4, setting5, setting6));
	}
}
