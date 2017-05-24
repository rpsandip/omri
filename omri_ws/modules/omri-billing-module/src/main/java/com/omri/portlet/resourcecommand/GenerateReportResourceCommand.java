

package com.omri.portlet.resourcecommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.dynamic.data.mapping.kernel.StructureNameException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.beans.ProcedureBean;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
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
			            
			            Font invoiceTitle = new Font();
			            invoiceTitle.setStyle(Font.BOLD);
			            invoiceTitle.setSize(24);
			            
			            Font clinicTitleFont = new Font();
			            clinicTitleFont.setStyle(Font.BOLD);
			            clinicTitleFont.setSize(16);
			            
		
			            Paragraph invoice = new Paragraph("Invoice",invoiceTitle);
			            invoice.setAlignment(Element.ALIGN_CENTER);
		
			            document.add(invoice);
		
			            // Clinic Detail
			            Paragraph clinicTitle = new Paragraph("Clinic:",clinicTitleFont);
			            document.add(clinicTitle);
			            
			            document.add(new Chunk(clinic.getClinicName()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getAddressLine1()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getAddressLine2()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(clinic.getCity()+StringPool.COMMA+clinic.getState()));document.add(Chunk.NEWLINE);
			            
			            // Patient Detail
			            
			            Paragraph patientTitle = new Paragraph("Patient :",clinicTitleFont);
			            document.add(patientTitle);
			            
			            document.add(new Chunk(patient.getFirstName()+StringPool.SPACE+patient.getLastName()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(patient.getAddressLine1()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(patient.getAddressLine2()));document.add(Chunk.NEWLINE);
			            document.add(new Chunk(patient.getCity()+StringPool.COMMA+patient.getState()));document.add(Chunk.NEWLINE);
			            
			            // Doctor Detail
			            
			            
			            Paragraph doctorDetail = new Paragraph("Physician : ",clinicTitleFont);
			            document.add(doctorDetail);
			            document.add(new Chunk(appointmentBean.getDoctorName()));
			            
			            // Invoice detail
			            
			            Paragraph invoiceDate = new Paragraph("Invoice Date",clinicTitleFont);
			            invoiceDate.setAlignment(Element.ALIGN_RIGHT);
			            document.add(invoiceDate);
			            Paragraph dateDetail = new Paragraph(dateformat2.format(new Date()));
			            dateDetail.setAlignment(Element.ALIGN_RIGHT);
			            document.add(dateDetail);
			            
			            
			            Paragraph appointmentDetail = new Paragraph("Appointments : ",clinicTitleFont);
			            document.add(appointmentDetail);
			            document.add(Chunk.NEWLINE);
			            // Appointment Detail
			            PdfPTable table = new PdfPTable(4);
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
                             totalAmount += appointment.getPrice();
							  dateCell = new PdfPCell(new Paragraph(dateformat.format(appointment.getAppointmetDate())));
					          resourceCell = new PdfPCell(new Paragraph(resource.getResourceName()+"("+ specification.getSpecificationName() +")"));
					          cptCodeCell = new PdfPCell(new Paragraph(patient.getCptCode()));
					          amountCell = new PdfPCell(new Paragraph("$"+String.valueOf(appointment.getPrice())));
					          
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

}