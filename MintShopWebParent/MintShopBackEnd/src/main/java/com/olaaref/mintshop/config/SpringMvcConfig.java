package com.olaaref.mintshop.config;


import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.olaaref.mintshop.paging.PagingAndSortingArgumentResolver;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		String pathPattern = "../site-logo";
//		Path path = Paths.get(pathPattern);
//		String absolutePath = path.toFile().getAbsolutePath();
//		String logicalPath =  pathPattern.replace("../", "")+"/**";
//		
//		registry.addResourceHandler(logicalPath)
//				.addResourceLocations("file:/"+absolutePath+"/");
//	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
	
	
}
