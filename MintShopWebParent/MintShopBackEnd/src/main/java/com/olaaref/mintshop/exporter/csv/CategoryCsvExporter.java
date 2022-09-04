package com.olaaref.mintshop.exporter.csv;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class CategoryCsvExporter extends AbstractExporter{
	
	public void export(List<Category> categoriesList, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "categories_");
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Name", "Alias", "Enabled"};
        String[] nameMapping = {"id", "name", "alias", "enabled"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (Category category : categoriesList) {
            csvWriter.write(category, nameMapping);
        }
         
        csvWriter.close();
	}
}
