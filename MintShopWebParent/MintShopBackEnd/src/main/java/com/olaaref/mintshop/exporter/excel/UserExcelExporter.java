package com.olaaref.mintshop.exporter.excel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class UserExcelExporter extends AbstractExporter{
	
	public void export(List<User> usersList, HttpServletResponse response) {
		
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");
	}
}
