package com.olaaref.mintshop.exporter.pdf;

import java.io.IOException;
import java.util.List;
import java.awt.Color;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.olaaref.mintshop.common.entity.User;
import com.olaaref.mintshop.exporter.AbstractExporter;

public class UserPdfExporter extends AbstractExporter {

	public void export(List<User> usersList, HttpServletResponse response) throws DocumentException, IOException {

		super.setResponseHeader(response, "application/pdf", ".pdf", "users_");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLACK);

		Paragraph paragraph = new Paragraph("List of users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10f);
		table.setWidths(new float[] { 1.2f, 3.5f, 3.0f, 3.0f, 2.7f, 2.0f });

		writeTableHeadere(table);
		writeTableData(table, usersList);

		document.add(paragraph);
		document.add(table);
		document.close();

	}

	private void writeTableHeadere(PdfPTable table) {

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new Color(52, 140, 235));
		cell.setPadding(10);
		cell.setBorder(Rectangle.NO_BORDER);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("First Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Last Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table, List<User> usersList) {

		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		for (User user : usersList) {
			cell.setPhrase(new Phrase(String.valueOf(user.getId())));
			table.addCell(cell);
			cell.setPhrase(new Phrase(user.getEmail()));
			table.addCell(cell);
			cell.setPhrase(new Phrase(user.getFirstName()));
			table.addCell(cell);
			cell.setPhrase(new Phrase(user.getLastName()));
			table.addCell(cell);
			cell.setPhrase(new Phrase(user.getRoles().toString()));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(user.isEnabled())));
			table.addCell(cell);
		}

		// make table striped
		boolean b = true;
		int rowCount = 0;
		for (PdfPRow r : table.getRows()) {

			for (PdfPCell c : r.getCells()) {
				if (rowCount == 0) {
					c.setBackgroundColor(new Color(52, 140, 235));
				} else {
					c.setBackgroundColor(b ? new Color(242, 247, 250) : Color.WHITE);
				}

			}
			b = !b;
			rowCount++;
		}

	}
}