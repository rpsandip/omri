package com.omri.portlet.actioncommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

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
import com.liferay.mail.kernel.model.FileAttachment;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.mail.MailEngine;
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
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriBillingModulemvcportletPortlet",
	        "mvc.command.name=/send-mail"
	    },
	    service = MVCActionCommand.class
)
public class SendMailActionCommand extends BaseMVCActionCommand{

	private static String FROM_EMAIL = "no-reply@omri.com";
	private static Log LOG = LogFactoryUtil.getLog(SendMailActionCommand.class.getName());
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		long procedureId = ParamUtil.getLong(actionRequest, "procedureId");
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
					LOG.error(e.getMessage(), e);
				}
			}
			String fileName = patient.getFirstName()+StringPool.UNDERLINE+patient.getLastName() +".pdf";
			fileName = fileName.replace(StringPool.SPACE, StringPool.UNDERLINE);
			File file = File.createTempFile(fileName, ".pdf");
			
			if(Validator.isNotNull(clinic) && Validator.isNotNull(patient) && Validator.isNotNull(appointmentBean)){
				Document document = new Document();
				
				
				
				 try {
					    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
		
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
			            
			            // Lawyer Detail
			            
			            Paragraph lawyerDetail = new Paragraph("Lawyer : ",clinicTitleFont);
			            document.add(lawyerDetail);
			            document.add(new Chunk(appointmentBean.getLawyerName()));
			            
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
							 Resource_Specification resourceSpecification =  Resource_SpecificationLocalServiceUtil.getResourceSpecification(resource.getResourceId(), specification.getSpecificationId());
							 totalAmount += appointment.getPrice();
							  dateCell = new PdfPCell(new Paragraph(dateformat.format(appointment.getAppointmetDate())));
					          resourceCell = new PdfPCell(new Paragraph(resource.getResourceName()+"("+ specification.getSpecificationName() +")"));
					          cptCodeCell = new PdfPCell(new Paragraph(resourceSpecification.getCptCode()));
					          amountCell = new PdfPCell(new Paragraph("$"+String.valueOf(appointment.getPrice())));
					          
					          table.addCell(dateCell);
					            table.addCell(resourceCell);
					            table.addCell(cptCodeCell);
					            table.addCell(amountCell);
							
							}catch(PortalException e){
								LOG.error(e.getMessage(), e);
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
			        } catch (DocumentException e) {
			            e.printStackTrace();
			        } 
				 sendMailNotification(actionRequest,file, patient);
				 //file.delete();
				 
				 actionResponse.setRenderParameter("mvcRenderCommandName", "/generateProcedureBill");
				 actionResponse.setRenderParameter("procedureId", String.valueOf(procedureId));
				 SessionMessages.add(actionRequest, "mail.sent.successfully");
		     }
		} catch (PortalException e) {
			LOG.error(e.getMessage(), e);
			actionResponse.setRenderParameter("mvcRenderCommandName", "/generateProcedureBill");
			actionResponse.setRenderParameter("procedureId", String.valueOf(procedureId));
			SessionErrors.add(actionRequest, "error-sent-mail");
		}
	}
	
	private void sendMailNotification(PortletRequest request, File file, Patient patient){
		
		 ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		 InputStream is = null;
		 UnsyncBufferedReader unsyncBufferedReader = null;
		 ClassLoader classloader = getClass().getClassLoader();
		 String[] doctorEmailIds = ParamUtil.getParameterValues(request, "doctor");
		 String[] lawerEmailIds = ParamUtil.getParameterValues(request, "lawyer");
		 
		 try {
			is = classloader.getResourceAsStream("email-body.tmpl");
			StringBundler sb = new StringBundler();
			unsyncBufferedReader = new UnsyncBufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = unsyncBufferedReader.readLine()) != null) {
				 sb.append(line);
				 sb.append(CharPool.NEW_LINE);
			}
			unsyncBufferedReader.close();
			is.close();
			String body = sb.toString().trim();
			String[] variables = new String[] { "[$PATIENT_NAME]"};
			String[] values = new String[] { patient.getFirstName()+StringPool.SPACE+patient.getLastName()};
			body = StringUtil.replace(body, variables, values);
			String subject = "OMRI Patient Invoice";
			String fromMail = FROM_EMAIL;
			InternetAddress from = new InternetAddress(fromMail);

			InternetAddress[] to =new InternetAddress[doctorEmailIds.length+lawerEmailIds.length];
			for(int i=0; i<doctorEmailIds.length; i++){
				to[i]= new InternetAddress(doctorEmailIds[i]);
			}
			
			for(int i=0; i<lawerEmailIds.length; i++){
				to[doctorEmailIds.length+i]= new InternetAddress(doctorEmailIds[i]);
			}
			
			FileAttachment fileAttachment = new FileAttachment(file, file.getName());
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.add(fileAttachment);
			MailMessage mailMessage = new MailMessage(from, subject, body, true);
			
			mailMessage.addFileAttachment(new File(file.getAbsolutePath()));
			mailMessage.setTo(to);
			MailServiceUtil.sendEmail(mailMessage);
			//file.delete();
		} catch (Exception e) {
			if(null != is && null != unsyncBufferedReader){
			try{
			is.close();
			unsyncBufferedReader.close();
			}catch(IOException e1){
				LOG.error(e1.getMessage(), e1);
			}
			}
			LOG.error(e.getMessage(), e);
		}
	 }

}
