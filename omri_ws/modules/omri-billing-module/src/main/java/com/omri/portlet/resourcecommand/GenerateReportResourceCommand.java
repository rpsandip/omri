

package com.omri.portlet.resourcecommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.kernel.StructureNameException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.beans.ProcedureBean;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Resource_Specification;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriBillingModulemvcportletPortlet",
	        "mvc.command.name=/generate-invoice"
	    },
	    service = MVCResourceCommand.class
)
public class GenerateReportResourceCommand implements MVCResourceCommand{

	private static Log  _log = LogFactoryUtil.getLog(GenerateReportResourceCommand.class.getName());
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		 
		long procedureId = ParamUtil.getLong(resourceRequest, "procedureId");
		Procedure procedure;
		Patient patient = null;
		Clinic clinic=null;
		AppointmentBean appointmentBean =null;
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("MM/dd/yyyy");
		int totalAmount=0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			procedure = ProcedureLocalServiceUtil.getProcedure(procedureId);
			ProcedureBean procedureBean = new ProcedureBean(procedure.getProcedureId());
			List<Appointment> procedureAppointmentList = AppointmentLocalServiceUtil.getAppointmentByProcedureId(procedure.getProcedureId());
			List<AppointmentBean> procedureAppointmentBeanList = new ArrayList<AppointmentBean>();
			for(Appointment appointment : procedureAppointmentList){
				try{
				 patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
				 clinic = ClinicLocalServiceUtil.getClinic(appointment.getClinicId());
				Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
				Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
				appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
				procedureAppointmentBeanList.add(appointmentBean);
				}catch(PortalException e){
					_log.error(e.getMessage(), e);
				}
			}
			procedureBean.setAppointmentList(procedureAppointmentBeanList);

		    if(Validator.isNotNull(clinic) && Validator.isNotNull(patient) && Validator.isNotNull(appointmentBean)){
				Document document = new Document();
				 try {
					    PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
		
			            //open
			            document.open();
			            
			            Font headerTitleFont = new Font();
			            headerTitleFont.setStyle(Font.BOLD);
			            headerTitleFont.setSize(16);
			            
			            Font medioumTitleFont = new Font();
			            medioumTitleFont.setStyle(Font.BOLD);
			            medioumTitleFont.setSize(12);
			            
			            Font normalTextFont = new Font();
			            medioumTitleFont.setSize(10);
			            
		
			            Paragraph invoice = new Paragraph("Invoice",headerTitleFont);
			            invoice.setAlignment(Element.ALIGN_CENTER);
		
			            document.add(invoice);
			            
			            // Invoice detail
			            
			            Paragraph invoiceDate = new Paragraph("Date:"+ dateformat2.format(new Date()),medioumTitleFont);
			            invoiceDate.setAlignment(Element.ALIGN_RIGHT);
			            document.add(invoiceDate);
			            
		            	PdfPTable headerTable = new PdfPTable(3);
			            headerTable.setWidths(new float[] { 1, 2, 2 });
						headerTable.addCell(createClinicImageCell(themeDisplay));
						
						PdfPCell clinicCell = new PdfPCell();
						clinicCell.addElement(new Phrase(clinic.getClinicName()+StringPool.COMMA, normalTextFont));
						clinicCell.addElement(new Phrase(clinic.getAddressLine1()+StringPool.COMMA, normalTextFont));
						clinicCell.addElement(new Phrase(clinic.getAddressLine2()+StringPool.COMMA, normalTextFont));
						clinicCell.addElement(new Phrase(clinic.getCity()+StringPool.COMMA+clinic.getState(), normalTextFont));
						clinicCell.setBorder(PdfPCell.NO_BORDER);
						clinicCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						headerTable.addCell(clinicCell);
						
						PdfPCell patientCell = new PdfPCell();
						patientCell.addElement(new Phrase(patient.getFirstName()+StringPool.SPACE+patient.getLastName()+StringPool.COMMA, normalTextFont));
						patientCell.addElement(new Phrase(patient.getAddressLine1()+StringPool.COMMA, normalTextFont));
						patientCell.addElement(new Phrase(patient.getAddressLine2()+StringPool.COMMA, normalTextFont));
						patientCell.addElement(new Phrase(patient.getCity()+StringPool.COMMA+patient.getState(), normalTextFont));
						patientCell.setBorder(PdfPCell.NO_BORDER);
						clinicCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						headerTable.addCell(patientCell);
						
						document.add(headerTable);

			            
						document.add(Chunk.NEWLINE);
			            /*
			            // Clinic Detail
			            Paragraph clinicTitle = new Paragraph("Clinic:",medioumTitleFont);
			            document.add(clinicTitle);
			            
			            document.add(new Chunk(clinic.getClinicName()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getAddressLine1()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getAddressLine2()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getCity()+StringPool.COMMA+clinic.getState()));document.add(Chunk.NEWLINE);
			            */
			            
			            /*
						// Patient Detail
			            
			            Paragraph patientTitle = new Paragraph("Patient :",medioumTitleFont);
			            document.add(patientTitle);
			            
			            document.add(new Chunk(patient.getFirstName()+StringPool.SPACE+patient.getLastName()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(patient.getAddressLine1()));document.add(Chunk.NEWLINE);
			            document.add(	new Chunk(patient.getAddressLine2()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(patient.getCity()+StringPool.COMMA+patient.getState()));document.add(Chunk.NEWLINE);
			            
			            */
						
						 
		            	PdfPTable doctorLawyerTable = new PdfPTable(2);
		            	doctorLawyerTable.setWidths(new float[] { 1,1 });
		            	doctorLawyerTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		            	
		            	PdfPCell doctorCell = new PdfPCell();
		            	doctorCell.addElement(new Phrase("Physician", medioumTitleFont));
		            	doctorLawyerTable.addCell(doctorCell);
		            	
		            	PdfPCell lawyerCell = new PdfPCell();
		            	lawyerCell.addElement(new Phrase("Lawyer", medioumTitleFont));
		            	doctorLawyerTable.addCell(lawyerCell);
						
		            	doctorCell = new PdfPCell();
		            	doctorCell.addElement(new Phrase(appointmentBean.getDoctorName(), medioumTitleFont));
		            	doctorLawyerTable.addCell(doctorCell);
		            	
		            	lawyerCell = new PdfPCell();
		            	lawyerCell.addElement(new Phrase(appointmentBean.getLawyerName(), medioumTitleFont));
		            	doctorLawyerTable.addCell(lawyerCell);
						
		            	document.add(doctorLawyerTable);
		            	document.add(Chunk.NEWLINE);
		            	
		            	/*
			            // Doctor Detail
			            Paragraph doctorDetail = new Paragraph("Physician : ",medioumTitleFont);
			            document.add(doctorDetail);
			            document.add(new Chunk(appointmentBean.getDoctorName()));
			            
						// Lawyer Detail
			            Paragraph lawyerDetail = new Paragraph("Lawyer : ",medioumTitleFont);
			            document.add(lawyerDetail);
			            document.add(new Chunk(appointmentBean.getLawyerName()));
			            */
			           
			            
			            Paragraph appointmentDetail = new Paragraph("Appointments : ",medioumTitleFont);
			            document.add(appointmentDetail);
			            document.add(Chunk.NEWLINE);

			            // Appointment Detail
			            PdfPTable table = new PdfPTable(4);
			            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
			            PdfPCell dateCell = new PdfPCell(new Paragraph("Date and Time"));
			            PdfPCell resourceCell = new PdfPCell(new Paragraph("Resource"));
			            PdfPCell cptCodeCell = new PdfPCell(new Paragraph("CPT Code"));
			            PdfPCell amountCell = new PdfPCell(new Paragraph("Amount"));
			            
			            table.addCell(dateCell);
			            table.addCell(resourceCell);
			            table.addCell(cptCodeCell);
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
							  dateCell = new PdfPCell(new Paragraph(dateformat.format(appointment.getAppointmetDate())));
							  dateCell.setBorderColorBottom(BaseColor.WHITE);
					          resourceCell = new PdfPCell(new Paragraph(resource.getResourceName()+"("+ specification.getSpecificationName() +")"));
					          dateCell.setBorderColorBottom(BaseColor.WHITE);
					          cptCodeCell = new PdfPCell(new Paragraph(resourceSpecification.getCptCode()));
					          dateCell.setBorderColorBottom(BaseColor.WHITE);
					          amountCell = new PdfPCell(new Paragraph("$"+String.valueOf(appointment.getPrice())));
					          dateCell.setBorderColorBottom(BaseColor.WHITE);
					          
					          table.addCell(dateCell);
					            table.addCell(resourceCell);
					            table.addCell(cptCodeCell);
					            table.addCell(amountCell);
							
							}catch(PortalException e){
								_log.error(e.getMessage(), e);
							}
						}
			            
			              dateCell = new PdfPCell(new Paragraph(""));
				          resourceCell = new PdfPCell(new Paragraph(""));
				          cptCodeCell = new PdfPCell(new Paragraph("Total"));
				          amountCell = new PdfPCell(new Paragraph("$"+String.valueOf(totalAmount)));
				          
				          table.addCell(dateCell);
				          table.addCell(resourceCell);
				          table.addCell(cptCodeCell);
				          table.addCell(amountCell);
			            
				          document.add(table);
				          
				          // Bottom declaration
				          
				         
				          //close
				          document.close();
			            
			            resourceResponse.setContentLength(baos.size());
		
			        } catch (DocumentException e) {
			            e.printStackTrace();
			        } 
				 
		}
		 
		 	try {
		 		resourceResponse.setContentType(ContentTypes.APPLICATION_PDF);
		 		resourceResponse.addProperty(
	                    HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename="+ patient.getFirstName()+StringPool.UNDERLINE+patient.getLastName() +".pdf");

	            OutputStream pos = resourceResponse.getPortletOutputStream();
	            try {
	                baos.writeTo(pos);
	                pos.flush();
	                baos.close();
	            } finally {
	                pos.close();
	            }
	        } catch(IOException e){
	        	e.printStackTrace();
	        }
		 	
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}
	
	 public static PdfPCell createClinicImageCell(ThemeDisplay themeDisplay) {
			try {
				Image img = Image.getInstance(getClinicImageURL(themeDisplay));
				 PdfPCell cell = new PdfPCell(img, true);
				 cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				 cell.setBorder(PdfPCell.NO_BORDER);
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