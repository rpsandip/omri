package com.omri.portlet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liferay.portal.kernel.util.StringPool;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.util.PatientStatus;

public class ReportFileUtil {
	public static File generateReport(List<PatientBean> patientBeanList) throws IOException{
		
		int index=0;
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		
		XSSFFont boldFont = wb.createFont();
		boldFont.setFontHeightInPoints((short) 14);
		boldFont.setBold(true);
		boldFont.setFontName("Arial");
		
		XSSFFont normalFont = wb.createFont();
		boldFont.setFontHeightInPoints((short) 12);
		boldFont.setFontName("Arial");
		
		index = createMainHeaderRow(sheet, wb, boldFont, index);
		
		for(PatientBean patientBean : patientBeanList){
			index = patientDataRow(sheet, wb, normalFont, patientBean, index);
		}
		
		// Write the output to a file
 		String fileName = "Patient_Reports.xlsx";
 		File file = new File(System.getProperty("catalina.home")+"/temp/"+fileName);
 		
 		FileOutputStream fileOut = new FileOutputStream(file);
 		wb.write(fileOut);
 		fileOut.close();
 		
 		return file; 
	}
	
	private static int createMainHeaderRow(XSSFSheet sheet, XSSFWorkbook wb, XSSFFont boldFont,int index){
		XSSFRow headerRow = sheet.createRow(index);	
		XSSFCellStyle style = wb.createCellStyle();
		style.setFont(boldFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		XSSFCell cell1=  headerRow.createCell(1);
		cell1.setCellValue("Name");
		cell1.setCellStyle(style);
		
		XSSFCell cell2=  headerRow.createCell(2);
		cell2.setCellValue("Patient Id");
		cell2.setCellStyle(style);
		
		XSSFCell cell3=  headerRow.createCell(3);
		cell3.setCellValue("Dos");
		cell3.setCellStyle(style);
		
		XSSFCell cell4=  headerRow.createCell(4);
		cell4.setCellValue("Payee");
		cell4.setCellStyle(style);
		
		XSSFCell cell5=  headerRow.createCell(5);
		cell5.setCellValue("Clinic");
		cell5.setCellStyle(style);
		
		
		XSSFCell cell6=  headerRow.createCell(6);
		cell6.setCellValue("Resources");
		cell6.setCellStyle(style);
		
		XSSFCell cell7=  headerRow.createCell(7);
		cell7.setCellValue("Status");
		cell7.setCellStyle(style);

		XSSFCell cell8=  headerRow.createCell(8);
		cell8.setCellValue("LOP");
		cell8.setCellStyle(style);
		
		XSSFCell cell9=  headerRow.createCell(9);
		cell9.setCellValue("LOP Request");
		cell9.setCellStyle(style);
		
		XSSFCell cell10=  headerRow.createCell(10);
		cell10.setCellValue("Invoice");
		cell10.setCellStyle(style);
		
		XSSFCell cell11=  headerRow.createCell(11);
		cell11.setCellValue("Order");
		cell11.setCellStyle(style);
		
		XSSFCell cell12=  headerRow.createCell(12);
		cell12.setCellValue("Procedure");
		cell12.setCellStyle(style);
		
		
		index++;
		return index;
	}
	
	private static int patientDataRow(XSSFSheet sheet, XSSFWorkbook wb, XSSFFont normalFont, PatientBean patientBean,int index){
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		XSSFRow patientRow = sheet.createRow(index);
		XSSFCellStyle style = wb.createCellStyle();
		style.setFont(normalFont);
		
		XSSFCell cell1=  patientRow.createCell(1);
		cell1.setCellValue(patientBean.getFirstName()+StringPool.SPACE+patientBean.getLastName());
		cell1.setCellStyle(style);
		
		XSSFCell cell2=  patientRow.createCell(2);
		cell2.setCellValue(patientBean.getCustomPatientId());
		cell2.setCellStyle(style);
		
		
		XSSFCell cell3=  patientRow.createCell(3);
		cell3.setCellValue(df.format(patientBean.getDos()));
		cell3.setCellStyle(style);
		
		
		XSSFCell cell4=  patientRow.createCell(4);
		cell4.setCellValue(patientBean.getPayee());
		cell4.setCellStyle(style);
		
		XSSFCell cell5=  patientRow.createCell(5);
		cell5.setCellValue(patientBean.getClinicName());
		cell5.setCellStyle(style);
		
		List<PatientResourceBean> resourceBeanList = patientBean.getResourceBeanList();
		String resourceDetail = StringPool.BLANK;
		for(PatientResourceBean patientResourceBean : resourceBeanList){
			resourceDetail += patientResourceBean.getResourceName()+StringPool.OPEN_PARENTHESIS+
					patientResourceBean.getSpecificationName()+StringPool.CLOSE_PARENTHESIS+StringPool.COMMA;
		}
		if(resourceDetail.indexOf(StringPool.COMMA)>0){
			resourceDetail = resourceDetail.substring(0, resourceDetail.lastIndexOf(StringPool.COMMA));
		}
		
		XSSFCell cell6=  patientRow.createCell(6);
		cell6.setCellValue(resourceDetail);
		cell6.setCellStyle(style);
		
		
		XSSFCell cell7=  patientRow.createCell(7);
		cell7.setCellValue(PatientStatus.findByValue(patientBean.getPatientClinic().getPatient_status()).getLabel());
		cell7.setCellStyle(style);
		
		
		XSSFCell cell8=  patientRow.createCell(8);
		cell8.setCellValue(patientBean.getLopDocuments().isEmpty()? "FALSE" : "TRUE");
		cell8.setCellStyle(style);
		
		XSSFCell cell9=  patientRow.createCell(9);
		cell9.setCellValue(patientBean.getLopRequestDocuments().isEmpty()? "FALSE" : "TRUE");
		cell9.setCellStyle(style);
		
		XSSFCell cell10=  patientRow.createCell(10);
		cell10.setCellValue(patientBean.getInvoiceDocuments().isEmpty()?"FALSE" : "TRUE");
		cell10.setCellStyle(style);
		
		XSSFCell cell11=  patientRow.createCell(11);
		cell11.setCellValue(patientBean.getOrderDocuments().isEmpty()?"FALSE": "TRUE");
		cell11.setCellStyle(style);
		
		XSSFCell cell12=  patientRow.createCell(12);
		cell12.setCellValue(patientBean.getProcedureDocumnts().isEmpty()?"FALSE":"TRUE");
		cell12.setCellStyle(style);
		
		index++;
		return index;
	}
}
