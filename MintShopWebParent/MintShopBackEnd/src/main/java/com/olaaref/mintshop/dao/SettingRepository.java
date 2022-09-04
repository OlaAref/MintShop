package com.olaaref.mintshop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.common.entity.setting.SettingCategory;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String>{

	List<Setting> findByCategory(SettingCategory category);
	
	public Setting findByKey(String key);
}
