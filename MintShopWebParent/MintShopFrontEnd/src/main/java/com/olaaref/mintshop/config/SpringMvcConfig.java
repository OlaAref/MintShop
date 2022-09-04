package com.olaaref.mintshop.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String pathPattern = "../site-logo";
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath =  pathPattern.replace("../", "")+"/**";
		
		registry.addResourceHandler(logicalPath)
				.addResourceLocations("file:/"+absolutePath+"/");
	}
	
	
	
}
