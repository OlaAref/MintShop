package com.olaaref.mintshop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.olaaref.mintshop.common.Constants;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.setting.GeneralSettingBag;

@Component
public class SettingFilter implements Filter {
	@Autowired
	private SettingService settingService;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString();
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".map") || url.endsWith(".woff2")
				|| url.endsWith(".ico") || url.endsWith(".png") || url.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}
		
		GeneralSettingBag generalSettings = this.settingService.getGeneralSettings();
		String siteLogo = generalSettings.get("SITE_LOGO").getValue();
		String siteName = generalSettings.get("SITE_NAME").getValue();
		String copyright = generalSettings.get("COPYRIGHT").getValue();
		
		request.setAttribute("SITE_LOGO", siteLogo);
		request.setAttribute("SITE_NAME", siteName);
		request.setAttribute("COPYRIGHT", copyright);
		request.setAttribute("GCP_BASE_URI", Constants.GCP_Base_URI);
		chain.doFilter(request, response);
	}
}
