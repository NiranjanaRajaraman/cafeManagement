package com.inn.cafe.serviceimpl;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.JWTFilter;
import com.inn.cafe.JWT.JWTUtil;
import com.inn.cafe.data.BillRepository;
import com.inn.cafe.domain.Bill;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.cafeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	JWTFilter jwtFitlter;

	@Autowired
	BillRepository billRepository;

	private static Logger LOGGER = LoggerFactory.getLogger(BillServiceImpl.class);

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			String fileName;

			if (validateRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate") && !(requestMap.get("isGenerate") == "true")) {
					fileName = (String) requestMap.get("uuid");
				} else {
					fileName = cafeUtils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}
				String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: "
						+ requestMap.get("contactNumber") + "\n" + "Email: " + requestMap.get("email") + "\n"
						+ "Payment method: " + requestMap.get("paymentMethod") + "\n" + "Date: " + new Date();

				Document document = new Document();
				PdfWriter.getInstance(document,
						new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
				document.open();
				setRectangleInPdf(document);
				Paragraph para = new Paragraph("Cafe Management System", getFont("Header"));
				para.setAlignment(Element.ALIGN_CENTER);
				document.add(para);

				Paragraph chunk = new Paragraph(data + "\n \n", getFont("Data"));
				document.add(chunk);

				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);

				JSONArray jsonArray = cafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
				for (int i = 0; i < jsonArray.length(); i++) {
					addRows(table, cafeUtils.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);
				Paragraph footer = new Paragraph(
						"Total: " + requestMap.get("total") + "\n" + "Thank you for visiting.Please visit again!!",
						getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

			} else {
				return cafeUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRows(PdfPTable table, Map<String, Object> data) {
		LOGGER.info("inside add rows");
		table.addCell((String) data.get("name"));
		table.addCell((String) data.get("category"));
		table.addCell((String) data.get("quantity"));
		table.addCell(Double.toString((Double) data.get("price")));
		table.addCell(Double.toString((Double) data.get("total")));

	}

	private Font getFont(String type) {
		LOGGER.info("inside font");
		switch (type) {
		case "Header":
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;

		case "Data":
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;

		default:
			return new Font();

		}
	}

	private void addTableHeader(PdfPTable table) {
		LOGGER.info("inside addTableHeader");
		Stream.of("Name", "Category", "Quantity", "price", "Sub Total").forEach(title -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(title));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);

		});

	}

	private void setRectangleInPdf(Document document) throws DocumentException {
		LOGGER.info("inside rectangle pdf");
		Rectangle rect = new Rectangle(577, 825, 18, 15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		document.add(rect);

	}

	private Boolean validateRequestMap(Map<String, Object> requestMap) {
		return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
				&& requestMap.containsKey("totalAmount") && requestMap.containsKey("productDetails");

	}

	private void insertBill(Map<String, Object> requestMap) {
		try {

			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotal((String) requestMap.get("totalAmount"));
			bill.setProductDetails((String) requestMap.get("productDetails"));
			bill.setCreatedBy(jwtFitlter.getCurrentUser());
			billRepository.save(bill);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
