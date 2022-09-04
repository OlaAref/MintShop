package com.olaaref.mintshop.exporter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;


public class AbstractExporter {

	public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String prefix) {

		response.setContentType(contentType);

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormatter.format(LocalDateTime.now());
		String fileName = prefix + timestamp + extension;

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}

}
