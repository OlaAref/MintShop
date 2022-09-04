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

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class BrandExcelExporter extends AbstractExporter{
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	
	public BrandExcelExporter() {
		workbook = new XSSFWorkbook();
		
	}		
	
	public void export(List<Brand> categoriesList, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "brands_");

		writeHeaderLine();
		writeDataLines(categoriesList);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
	}
	
	private void writeHeaderLine() {
		
		sheet = workbook.createSheet("Categories");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0, "ID", cellStyle);
		createCell(row, 1, "Name", cellStyle);
		createCell(row, 2, "Categories", cellStyle);
		
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
	
	private void writeDataLines(List<Brand> brandsList) {
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		int rowCount = 1;
		
		for (Brand brand : brandsList) {
			XSSFRow row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, brand.getId(), cellStyle);
			createCell(row, columnCount++, brand.getName(), cellStyle);
			createCell(row, columnCount++, brand.getCategoriesNames().toString(), cellStyle);
		}
	}
}


















