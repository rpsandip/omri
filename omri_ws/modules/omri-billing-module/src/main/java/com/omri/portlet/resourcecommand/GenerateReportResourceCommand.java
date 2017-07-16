

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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			procedure = ProcedureLocalServiceUtil.getProcedure(procedureId);
			List<Appointment> procedureAppointmentList = AppointmentLocalServiceUtil.getAppointmentByProcedureId(procedure.getProcedureId());
			for(Appointment appointment : procedureAppointmentList){
				try{
					patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
					clinic = ClinicLocalServiceUtil.getClinic(appointment.getClinicId());
					Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
					Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
					appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
				}catch(PortalException e){
					_log.error(e.getMessage(), e);
				}
			}

		    if(Validator.isNotNull(clinic) && Validator.isNotNull(patient) && Validator.isNotNull(appointmentBean)){
		    	 try {
					PortletCommonUtil.generatePdf(null, baos, themeDisplay, clinic, patient, appointmentBean, procedureAppointmentList);
					resourceResponse.setContentLength(baos.size());
		    	 } catch (FileNotFoundException e) {
					_log.error(e);
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