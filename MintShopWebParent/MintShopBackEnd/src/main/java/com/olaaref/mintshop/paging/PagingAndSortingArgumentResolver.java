package com.olaaref.mintshop.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model, 
								 NativeWebRequest request, WebDataBinderFactory arg3) throws Exception {
		
		PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
		
		String sortField = request.getParameter("sortField");
		String sortDir = request.getParameter("sortDir");
		String keyword = request.getParameter("keyword");
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleLink", annotation.moduleUrl());
		
		
		
		return new PagingAndSortingHelper(model, annotation.listName(), sortField, sortDir, keyword);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//support only parameters annotated with PagingAndSortingParam annotation
		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
	}

}
