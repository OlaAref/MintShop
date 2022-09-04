package com.olaaref.mintshop.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.common.entity.Menu;
import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.service.MenuService;
import com.olaaref.mintshop.service.SettingService;

@Component
@Order(-123)//invoke this filter before spring.security.filter
public class SettingFilter implements Filter {

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private MenuService menuService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString();
		
		if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".map") || url.endsWith(".woff2") 
				|| url.endsWith(".ico") || url.endsWith(".png") || url.endsWith(".jpg")) {
			
			chain.doFilter(request, response);
			return;
		}
		
		
		//set setting
		List<Setting> generalSettings = settingService.getGeneralSettings();
		
		generalSettings.forEach(setting -> {
			request.setAttribute(setting.getKey(), setting.getValue());
		});
		
		request.setAttribute("GCP_BASE_URI", Constants.GCP_Base_URI);
		
		//send menus
		List<Menu> menus = menuService.getAllMenus();
		request.setAttribute("menus", menus);
		
		
		chain.doFilter(request, response);
	}

}
