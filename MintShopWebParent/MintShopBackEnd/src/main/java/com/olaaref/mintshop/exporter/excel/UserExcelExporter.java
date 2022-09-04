package com.olaaref.mintshop.exporter.excel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class UserExcelExporter extends AbstractExporter{
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
		
	}		
	
	public void export(List<User> usersList, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "users_");

		writeHeaderLine();
		writeDataLines(usersList);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
	}
	
	private void writeHeaderLine() {
		
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0, "ID", cellStyle);
		createCell(row, 1, "Email", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enable", cellStyle);
		
	}
	
	private void  createCell(XSSFRow row, int columnIndex, Object value, XSSFCellStyle style) {
		
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}
		else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}
		else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
		
	}
	
	private void writeDataLines(List<User> usersList) {
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		int rowCount = 1;
		
		for (User user : usersList) {
			XSSFRow row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, user.getId(), cellStyle);
			createCell(row, columnCount++, user.getEmail(), cellStyle);
			createCell(row, columnCount++, user.getFirstName(), cellStyle);
			createCell(row, columnCount++, user.getLastName(), cellStyle);
			createCell(row, columnCount++, user.getRoles().toString(), cellStyle);
			createCell(row, columnCount++, user.isEnabled(), cellStyle);
		}
	}
}


















