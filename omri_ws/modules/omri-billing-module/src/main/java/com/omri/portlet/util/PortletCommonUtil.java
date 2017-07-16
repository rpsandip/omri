package com.omri.portlet.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.ResourceResponse;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Resource_Specification;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

public class PortletCommonUtil {
	
	private static Log _log = LogFactoryUtil.getLog(PortletCommonUtil.class.getName());
	
	public static void generatePdf(File file, ByteArrayOutputStream baos, ThemeDisplay themeDisplay, Clinic clinic, Patient patient, AppointmentBean appointmentBean, List<Appointment> procedureAppointmentList) throws FileNotFoundException{
		SimpleDateFormat dateformat2 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		int totalAmount=0;
		Document document = new Document(PageSize.A4);
		Font zapfdingbats = new Font(FontFamily.ZAPFDINGBATS, 8);
        Font font = new Font();
        Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);

		 try {
			    if(null != baos){
			    	PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
			    }else{
			    	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));	
			    }

	            //open
	            document.open();
	            
	            Font headerTitleFont = new Font(Font.FontFamily.HELVETICA, 14,Font.BOLD);
	            Font medioumTitleBoldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	            Font medioumTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
	            Font normalTextFont = new Font(Font.FontFamily.HELVETICA, 10,Font.NORMAL);
	            
	            // Invoice Heading
	            Paragraph invoice = new Paragraph("INVOICE",headerTitleFont);
	            invoice.setAlignment(Element.ALIGN_CENTER);
	            document.add(invoice);
	            
	            // Invoice date
	            Paragraph invoiceDate = new Paragraph("Date:"+ dateformat2.format(new Date()),medioumTitleFont);
	            invoiceDate.setAlignment(Element.ALIGN_RIGHT);
	            document.add(invoiceDate);
	            
	            //Clinic image, Clinic detail, Patient Detail
	            PdfPTable headerTable = new PdfPTable(3);
	            headerTable.setWidths(new float[] { 1, 1, 1 });
	            headerTable.setWidthPercentage(100);
	            headerTable.setHorizontalAlignment(headerTable.ALIGN_LEFT);
	            
	            PdfPCell headerCell = new PdfPCell();
	            headerCell.addElement(new Phrase("", normalTextFont));
	            headerCell.setBorder(PdfPCell.NO_BORDER);
	            headerTable.addCell(headerCell);
	            
	            headerCell = new PdfPCell();
	            headerCell.setBorder(PdfPCell.NO_BORDER);
	            headerCell.addElement(new Phrase("Clinic :", medioumTitleBoldFont));
	            headerCell.setPaddingLeft(20f);
	            headerTable.addCell(headerCell);
	            
	            headerCell = new PdfPCell();
	            headerCell.setBorder(PdfPCell.NO_BORDER);
	            headerCell.addElement(new Phrase("Patient :", medioumTitleBoldFont));
	            headerCell.setPaddingLeft(20f);
	            headerTable.addCell(headerCell);

				headerTable.addCell(createClinicImageCell(themeDisplay));
				
				PdfPCell clinicCell = new PdfPCell();
				clinicCell.addElement(new Phrase(clinic.getClinicName(), normalTextFont));
				clinicCell.addElement(new Phrase(clinic.getAddressLine1(), normalTextFont));
				clinicCell.addElement(new Phrase(clinic.getAddressLine2(), normalTextFont));
				clinicCell.addElement(new Phrase(clinic.getCity()+StringPool.COMMA+clinic.getState(), normalTextFont));
				clinicCell.addElement(new Phrase(clinic.getPhoneNo(), normalTextFont));
				clinicCell.setBorder(PdfPCell.NO_BORDER);
				clinicCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				clinicCell.setPaddingTop(5f);
				clinicCell.setPaddingLeft(20f);
				headerTable.addCell(clinicCell);
				
				PdfPCell patientCell = new PdfPCell();
				patientCell.addElement(new Phrase(patient.getFirstName()+StringPool.SPACE+patient.getLastName(), normalTextFont));
				patientCell.addElement(new Phrase(patient.getAddressLine1(), normalTextFont));
				patientCell.addElement(new Phrase(patient.getAddressLine2(), normalTextFont));
				patientCell.addElement(new Phrase(patient.getCity()+ StringPool.COMMA+ patient.getState(), normalTextFont));
				patientCell.addElement(new Phrase(patient.getPhoneNo(), normalTextFont));
				patientCell.setBorder(PdfPCell.NO_BORDER);
				patientCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				patientCell.setPaddingTop(5f);
				patientCell.setPaddingLeft(20f);
				headerTable.addCell(patientCell);
				
				document.add(headerTable);
	            document.add(Chunk.NEWLINE);
				
				 
	            // Lawyer and physician detail
	        	PdfPTable doctorLawyerTable = new PdfPTable(2);
	        	
	        	doctorLawyerTable.setWidths(new float[] { 1,1 });
	        	doctorLawyerTable.setWidthPercentage(100);
	        	
	        	PdfPCell doctorCell = new PdfPCell(new Phrase("Physician", medioumTitleBoldFont));
	        	doctorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	doctorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        	doctorCell.setPadding(5);
	        	doctorLawyerTable.addCell(doctorCell);
	        	
	        	PdfPCell lawyerCell = new PdfPCell(new Phrase("Lawyer", medioumTitleBoldFont));
	        	lawyerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	lawyerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        	doctorCell.setPadding(5);
	        	doctorLawyerTable.addCell(lawyerCell);
				
	        	doctorCell = new PdfPCell(new Phrase(appointmentBean.getDoctorName(), normalTextFont));
	        	doctorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	doctorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        	doctorCell.setPadding(5);
	        	doctorLawyerTable.addCell(doctorCell);
	        	
	        	lawyerCell = new PdfPCell(new Phrase(appointmentBean.getLawyerName(), normalTextFont));
	        	lawyerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	lawyerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        	doctorCell.setPadding(5);
	        	doctorLawyerTable.addCell(lawyerCell);
				
	        	document.add(doctorLawyerTable);
	        	
	        	document.add(Chunk.NEWLINE);

	        	// Appointmenr Detail

	        	PdfPTable table = new PdfPTable(4);
	            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
	            table.setWidthPercentage(100);
	            
	            PdfPCell dateCell = new PdfPCell(new Phrase("Date of Service", medioumTitleBoldFont));
	            PdfPCell resourceCell = new PdfPCell(new Phrase("Description",medioumTitleBoldFont));
	            PdfPCell cptCodeCell = new PdfPCell(new Phrase("CPT Code",medioumTitleBoldFont));
	            PdfPCell amountCell = new PdfPCell(new Phrase("Total Fee",medioumTitleBoldFont));
	            
	            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            dateCell.setPadding(5);
	            table.addCell(dateCell);
	            
	            resourceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            resourceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            resourceCell.setPadding(5);
	            table.addCell(resourceCell);
	            
	            cptCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cptCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cptCodeCell.setPadding(5);
	            table.addCell(cptCodeCell);
	            
	            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            amountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            amountCell.setPadding(5);
	            table.addCell(amountCell);
	            
	            float[] columnWidths = {1f, 1f, 1f, 1f};

	            table.setWidths(columnWidths);
	            
	            for(Appointment appointment : procedureAppointmentList){
					try{
					 
					  patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
					  clinic = ClinicLocalServiceUtil.getClinic(appointment.getClinicId());
					  Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
					  Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
					  Resource_Specification resourceSpecification =  Resource_SpecificationLocalServiceUtil.getResourceSpecification(resource.getResourceId(), specification.getSpecificationId());
					  totalAmount += appointment.getPrice();
					  
					  dateCell = new PdfPCell(new Phrase(dateformat.format(appointment.getAppointmetDate()),normalTextFont));
			          dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			          dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			          dateCell.setPadding(5); 
			          dateCell.setBorder(PdfPCell.NO_BORDER);
			          dateCell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);

			          resourceCell = new PdfPCell(new Phrase(resource.getResourceName()+"("+ specification.getSpecificationName() +")",normalTextFont));
			          resourceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			          resourceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			          resourceCell.setPadding(5);
			          resourceCell.setBorder(PdfPCell.NO_BORDER);
			          resourceCell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
			          
			          cptCodeCell = new PdfPCell(new Phrase(resourceSpecification.getCptCode(),normalTextFont));
			          cptCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			          cptCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			          cptCodeCell.setPadding(5);  
			          cptCodeCell.setBorder(PdfPCell.NO_BORDER);
			          cptCodeCell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);


			          amountCell = new PdfPCell(new Phrase("$"+String.valueOf(appointment.getPrice()),normalTextFont));
			          amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			          amountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			          amountCell.setPadding(5); 
			          amountCell.setBorder(PdfPCell.NO_BORDER);
			          amountCell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
			          
			          table.addCell(dateCell);
			          table.addCell(resourceCell);
			          table.addCell(cptCodeCell);
			          table.addCell(amountCell);
					
					}catch(PortalException e){
						_log.error(e.getMessage(), e);
					}
				}
	            
	            dateCell = new PdfPCell(new Phrase(""));
	            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            dateCell.setPadding(5);  
	            
	            resourceCell = new PdfPCell(new Phrase(""));
	  	      	resourceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	  	      	resourceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  	      	resourceCell.setPadding(5);  
	  	      	
	  	      	cptCodeCell = new PdfPCell(new Phrase("Total",medioumTitleBoldFont));
	  	      	cptCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	  	      	cptCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  	      	cptCodeCell.setPadding(5);  

	  	      	amountCell = new PdfPCell(new Phrase("$"+String.valueOf(totalAmount), medioumTitleBoldFont));
	  	      	amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	  	      	amountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	  	      	amountCell.setPadding(5);  
		          
		        table.addCell(dateCell);
		        table.addCell(resourceCell);
		        table.addCell(cptCodeCell);
		        table.addCell(amountCell);
	            
		        document.add(table);
		  
		        document.add(Chunk.NEWLINE);
		        
		        
		         // Bottom declaration
		         
		        PdfPTable bottomTable = new PdfPTable(2);
		      	
		      	bottomTable.setWidths(new float[] { 1,3 });
		      	bottomTable.setWidthPercentage(100);
		      	
		      	Paragraph pt = new Paragraph();
		      	pt.add(bullet);
		      	pt.add(new Phrase("  Payment Type :", medioumTitleBoldFont));
		      	PdfPCell contentTypeCell = new PdfPCell(pt);
		      	contentTypeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	contentTypeCell.setBorder(PdfPCell.NO_BORDER);    	
		      	bottomTable.addCell(contentTypeCell);
		      	
		      	PdfPCell contentTypeDetailCell = new PdfPCell(new Phrase("___Check   ___Visa   ___MasterCard  ___Amex  ___DiscoverChed", normalTextFont));
		      	contentTypeDetailCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//contentTypeDetailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//contentTypeDetailCell.setPadding(5);
		      	contentTypeDetailCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(contentTypeDetailCell);
		      	
		      	Paragraph cn = new Paragraph();
		      	cn.add(bullet);
		      	cn.add(new Phrase("  Cardholder Name :", medioumTitleBoldFont));
		      	PdfPCell cardHolderCell = new PdfPCell(cn);
		      	cardHolderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//cardHolderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//cardHolderCell.setPadding(5);
		      	cardHolderCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(cardHolderCell);
					
		      	PdfPCell cardHolderDetailCell = new PdfPCell(new Phrase("__________________________________________________________", medioumTitleFont));
		      	cardHolderDetailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		      	//cardHolderDetailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//cardHolderDetailCell.setPadding(5);
		      	cardHolderDetailCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(cardHolderDetailCell);
		      	
		      	
		      	Paragraph an = new Paragraph();
		      	an.add(bullet);
		      	an.add(new Phrase("  Account Number :", medioumTitleBoldFont));
		      	PdfPCell accountNumberCell = new PdfPCell(an);
		      	accountNumberCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//accountNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//accountNumberCell.setPadding(5);
		      	accountNumberCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(accountNumberCell);
					
		      	PdfPCell accountNumberDetailCell = new PdfPCell(new Phrase("__________________________________________________________", medioumTitleFont));
		      	accountNumberDetailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		      	//cardHolderDetailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//cardHolderDetailCell.setPadding(5);
		      	accountNumberDetailCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(accountNumberDetailCell);
		      	
		      	Paragraph ed = new Paragraph();
		      	ed.add(bullet);
		      	ed.add(new Phrase("  Exp Date :", medioumTitleBoldFont));
		      	PdfPCell expDateCell = new PdfPCell(ed);
		      	expDateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//expDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//expDateCell.setPadding(5);
		      	expDateCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(expDateCell);
					
		      	PdfPCell expDateDetailCell = new PdfPCell(new Phrase("__________________________________________________________", medioumTitleFont));
		      	expDateDetailCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//cardHolderDetailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//cardHolderDetailCell.setPadding(5);
		      	expDateDetailCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(expDateDetailCell);
		      	
		      	
		      	Paragraph cvv = new Paragraph();
		      	cvv.add(bullet);
		      	cvv.add(new Phrase("  CVV2(3 digit number on the back of Visa/MC, 4 digits on front of AMEX)", medioumTitleBoldFont));
		      	PdfPCell ccv2Cell = new PdfPCell(cvv);
		      	ccv2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//ccv2Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//ccv2Cell.setPadding(5);
		      	ccv2Cell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(ccv2Cell);
					
		      	PdfPCell ccv2DetailCell = new PdfPCell(new Phrase("_____________________________       ___/___/____", medioumTitleFont));
		      	ccv2DetailCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		      	//cardHolderDetailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		      	//cardHolderDetailCell.setPadding(5);
		      	ccv2DetailCell.setBorder(PdfPCell.NO_BORDER);
		      	bottomTable.addCell(ccv2DetailCell);
					
		      	document.add(bottomTable);
		      	
		      	
		      	document.add(Chunk.NEWLINE);
			      
		      	Paragraph notes = new Paragraph("Notes:_______________________________________________________________________________________",medioumTitleBoldFont);
		      	notes.add(Chunk.NEWLINE);
		      	notes.add("______________________________________________________________________________________________");
		      	notes.setAlignment(Element.ALIGN_LEFT);
		        document.add(notes);
		         
		        //close
		        document.close();
	        } catch (DocumentException e) {
	            e.printStackTrace();
	        } 
	}

	
	private static PdfPCell createClinicImageCell(ThemeDisplay themeDisplay) {
		try {
		 	Image img = Image.getInstance(getClinicImageURL(themeDisplay));
            img.setAlignment(Image.LEFT);
            img.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(img, true);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setPaddingTop(5f);
			return cell;
		} catch (BadElementException e) {
			_log.error(e.getMessage());
		} catch (MalformedURLException e) {
			_log.error(e.getMessage());
		} catch (IOException e) {
			_log.error(e.getMessage());
		}
		return null;
       
	}
 
	private static byte[] getClinicImageURL(ThemeDisplay themeDisplay){
		String clinicImageURL = StringPool.BLANK;
		try {
			DLFileEntry clinicImage = DLFileEntryLocalServiceUtil.getFileEntry(themeDisplay.getCompanyGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Clinic_Image");
			InputStream is = DLFileEntryLocalServiceUtil.getFileAsStream(clinicImage.getFileEntryId(), clinicImage.getLatestFileVersion(true).getVersion());
			byte[] fileBytes = FileUtil.getBytes(is);
			return fileBytes;
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}catch(IOException e){
			_log.error(e.getMessage(), e);
		}
		return new byte[]{};
	}
}
