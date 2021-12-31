package com.olaaref.mintshop.exporter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.olaaref.mintshop.common.entity.User;

public class AbstractExporter {

	public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {

		response.setContentType(contentType);

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormatter.format(LocalDateTime.now());
		String fileName = "users_" + timestamp + extension;

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}

}
