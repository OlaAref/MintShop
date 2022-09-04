package com.olaaref.mintshop.exporter.csv;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class UserCsvExporter extends AbstractExporter{
	
	public void export(List<User> usersList, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "users_");
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] nameMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (User user : usersList) {
            csvWriter.write(user, nameMapping);
        }
         
        csvWriter.close();
	}
}
