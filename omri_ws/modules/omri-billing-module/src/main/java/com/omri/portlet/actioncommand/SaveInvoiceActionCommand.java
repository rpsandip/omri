package com.omri.portlet.actioncommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
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
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriBillingModulemvcportletPortlet",
	        "mvc.command.name=/save-invoice"
	    },
	    service = MVCActionCommand.class
)
public class SaveInvoiceActionCommand extends BaseMVCActionCommand {
	Log _log = LogFactoryUtil.getLog(SaveInvoiceActionCommand.class.getName());
	private static long PARENT_FOLDER_ID = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long procedureId = ParamUtil.getLong(actionRequest, "procedureId");
		Procedure procedure;
		Patient patient = null;
		Clinic clinic=null;
		AppointmentBean appointmentBean =null;
		long repositoryId = themeDisplay.getCompanyGroupId();
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
					_log.error(e.getMessage(), e);
				}
			}
			String fileName = patient.getFirstName()+StringPool.UNDERLINE+patient.getLastName() +".pdf";
			fileName = fileName.replace(StringPool.SPACE, StringPool.UNDERLINE);
			File file = new File(System.getProperty("catalina.home")+"/temp/"+fileName);
			
			if(Validator.isNotNull(clinic) && Validator.isNotNull(patient) && Validator.isNotNull(appointmentBean)){
				 try {
					   
				 PortletCommonUtil.generatePdf(file, null, themeDisplay, clinic, patient, appointmentBean, procedureAppointmentList);
					 
				 // Save file to patient folder
				 Folder patientFolder = getFolder(actionRequest, PARENT_FOLDER_ID,String.valueOf(patient.getPatientId()));
				 Folder folder = getFolder(actionRequest, patientFolder.getFolderId(),"Invoice Documents"); 
				 
				 ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest); 
				 try{
					 DLAppServiceUtil.addFileEntry(repositoryId, folder.getFolderId(),  file.getName(), MimeTypesUtil.getContentType(file),  file.getName(), "Patient Invoice" , "", file, serviceContext);
				 }catch(DuplicateFileEntryException e){
					 fileName = patient.getFirstName()+StringPool.UNDERLINE+patient.getLastName() + StringPool.UNDERLINE + new Date().getTime() + ".pdf";
					 DLAppServiceUtil.addFileEntry(repositoryId, folder.getFolderId(), fileName, MimeTypesUtil.getContentType(file), fileName, "Patient Invoice", "", file, serviceContext);
				 }
				 
				 actionResponse.setRenderParameter("mvcRenderCommandName", "/generateProcedureBill");
				 actionResponse.setRenderParameter("procedureId", String.valueOf(procedureId));
				 SessionMessages.add(actionRequest, "invoice.save.successfully");
				} catch (PortalException e) {
					_log.error(e.getMessage(), e);
					actionResponse.setRenderParameter("mvcRenderCommandName", "/generateProcedureBill");
					actionResponse.setRenderParameter("procedureId", String.valueOf(procedureId));
					SessionErrors.add(actionRequest, "error-save-invoice");
				}
			}
	}

	private boolean isFolderExist(ThemeDisplay themeDisplay,long parentFolderId,String folderName){
		boolean folderExist = false;
		try { 
			DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), parentFolderId, folderName); 
			folderExist = true;
		} catch (Exception e) {	
			//LOG.error(e.getMessage(), e);
		} return folderExist; 
	}
	
	private Folder getFolder(ActionRequest actionRequest, long parentFolderId,String folderName) { 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean defaultFolderExist = isFolderExist(themeDisplay,parentFolderId,String.valueOf(folderName)); 
		Folder patientFolder=null;
		if (!defaultFolderExist) {
			long repositoryId = themeDisplay.getCompanyGroupId();
			try {
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest); 
				patientFolder = DLAppServiceUtil.addFolder(repositoryId,parentFolderId, folderName,"", serviceContext);
			} catch (PortalException e1) { 
				_log.error(e1.getMessage(), e1);
			} catch (SystemException e1) {
				_log.error(e1.getMessage(), e1);
			}	
		}else{
			try {
				patientFolder =	DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), parentFolderId, folderName);
			} catch (PortalException e1) {
				_log.error(e1.getMessage(), e1);
			}
		}
		return patientFolder;
	}
}
