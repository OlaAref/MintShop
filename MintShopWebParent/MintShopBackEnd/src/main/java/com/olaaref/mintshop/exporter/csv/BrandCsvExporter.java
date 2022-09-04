package com.olaaref.mintshop.exporter.csv;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.olaaref.mintshop.common.entity.Brand;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class BrandCsvExporter extends AbstractExporter{
	
	public void export(List<Brand> categoriesList, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "brands_");
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Name", "Categories"};
        String[] nameMapping = {"id", "name", "categoriesNames"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (Brand brand : categoriesList) {
            csvWriter.write(brand, nameMapping);
        }
         
        csvWriter.close();
	}
}
