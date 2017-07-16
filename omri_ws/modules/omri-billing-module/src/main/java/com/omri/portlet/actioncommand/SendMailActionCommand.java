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
import com.omri.portlet.util.PortletCommonUtil;
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
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long procedureId = ParamUtil.getLong(actionRequest, "procedureId");
		Procedure procedure;
		Patient patient = null;
		Clinic clinic=null;
		AppointmentBean appointmentBean =null;
		try {
			procedure = ProcedureLocalServiceUtil.getProcedure(procedureId);
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
			File file = new File(System.getProperty("catalina.home")+"/temp/"+fileName);
			
			if(Validator.isNotNull(clinic) && Validator.isNotNull(patient) && Validator.isNotNull(appointmentBean)){
				
				 PortletCommonUtil.generatePdf(file, null, themeDisplay, clinic, patient, appointmentBean, procedureAppointmentList);
				
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
