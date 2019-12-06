package com.git.automation.supporttutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

public class ExtractAPIPayload {

	@Test
	@Parameters({ "path" })
	// This method is to read all the values in a column in an excel file an send it to api and get response and write it in the excel as last column
	public static void storedata(String path) throws IOException, InterruptedException {
		DataFormatter df = new DataFormatter();
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row - xssfSheet.getLastRowNum()
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				XSSFCell cell = xssfRow.createCell(xssfRow.getLastCellNum(), CellType.STRING);

				if (xssfRow != null) {
					// Read the cell and format the data
					XSSFCell rawdata = xssfRow.getCell(0);
					String asItLooksInExcel = df.formatCellValue(rawdata);

					try {
						try {
							// Get the API response payload and store it in the cell 
							cell.setCellValue(extractpayload(asItLooksInExcel));
						} catch (IllegalArgumentException e1) {
							// Set the Value of the cell if the payload response is more than its maximum length
							e1.printStackTrace();
							cell.setCellValue("Exceeded Maxiumum length of cell Contents");
						}
					} catch (Exception e2) {
						// Set the cell value if there is an timeout with the API
						e2.printStackTrace();
						cell.setCellValue("Timeout");

					}

					try {
						FileOutputStream fos = new FileOutputStream(path);
						xssfWorkbook.write(fos);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			xssfWorkbook.close();
		}

	}

	public static String extractpayload(String sbstr) throws IOException, InterruptedException {
        // Use RestAssured to hit an API and get the Response

		RestAssured.baseURI = "";
		RestAssuredConfig config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
		        setParam("http.connection.timeout",10000).
		        setParam("http.socket.timeout",10000).
		        setParam("http.connection-manager.timeout",10000));;
		Response extpayload = (Response) RestAssured.given().body(sbstr).when().config(config)
				.post("/");
		
		String extplstr = extpayload.getBody().asString();
		System.out.println(extplstr);
		return extplstr;

	}

}
