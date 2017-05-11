package com.omri.portlet.actioncommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.exception.DuplicateFileException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Patient;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.util.AppointmentStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriAppointmentPortletmvcportletPortlet",
	        "mvc.command.name=/submitTechnicalReport"
	    },
	    service = MVCActionCommand.class
	)
public class SubmitAppointmentReportActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(SubmitAppointmentReportActionCommand.class.getName());
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		long appointmentId = ParamUtil.getLong(actionRequest, "appointmentId");
		try {
			Appointment appointment = AppointmentLocalServiceUtil.getAppointment(appointmentId);
			String technicalComment = ParamUtil.getString(actionRequest, "technicalComment");
			appointment.setTechnologistComment(technicalComment);
			appointment.setStatus(AppointmentStatus.TECHNOLOGIST_REPORT_SUBMITTED.getValue());
			AppointmentLocalServiceUtil.updateAppointment(appointment);
			uploadTechnologistDocument(actionRequest, actionResponse, appointment);
			SessionMessages.add(actionRequest, "report-submitte-successfully");
		} catch (PortalException e) {
			_log.error(e.getMessage(),e);
		}
		
	}
	private void uploadTechnologistDocument(ActionRequest actionRequest, ActionResponse actionResponse, Appointment appointment){
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		UploadPortletRequest uploadRequest=PortalUtil.getUploadPortletRequest(actionRequest);
	      Map<String, FileItem[]> files= uploadRequest.getMultipartParameterMap();
	      String title, description,mimeType,extension; 
	      InputStream is;
	      long repositoryId=0l;
	      FileEntry fileEntry=null;
	      DLFolder patientFolder= null;
	      try {
	    	 patientFolder = DLFolderLocalServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), 0l, String.valueOf(appointment.getPatientId()));
	      } catch (PortalException e1) {
	    	  _log.error(e1.getMessage(), e1);
	      }
	      if(Validator.isNotNull(patientFolder)){ 
	      Folder patientAppointmentFolder = getFolder(actionRequest, patientFolder.getFolderId(), appointment.getAppointmentId()+StringPool.UNDERLINE+appointment.getPatientId());
	          for (Entry<String, FileItem[]> file2 : files.entrySet()) {
	    	     FileItem item[] =file2.getValue();
	    	      for (FileItem fileItem : item) {
	    		    title = fileItem.getFileName(); 
					description = title;
					repositoryId = themeDisplay.getCompanyGroupId();
					mimeType = fileItem.getContentType();
					extension = fileItem.getFileNameExtension();
					try {
						is =fileItem.getInputStream();
						ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest);
						try{
							fileEntry = DLAppServiceUtil.addFileEntry(repositoryId, patientAppointmentFolder.getFolderId(), title, mimeType, title, description, "", is,fileItem.getSize(),serviceContext);
						}catch(DuplicateFileException | DuplicateFileEntryException dfe){
							title = title+StringPool.DASH+(new Date().getTime());
							fileEntry = DLAppServiceUtil.addFileEntry(repositoryId, patientAppointmentFolder.getFolderId(), title, mimeType, title, description, "", is, fileItem.getSize(), serviceContext);
						}
					} catch (IOException e) {
						_log.error(e.getMessage(), e);
					}catch(PortalException e){
						_log.error(e.getMessage(), e);
					}
					
	    	  }
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
		boolean defaultFolderExist = isFolderExist(themeDisplay,parentFolderId,folderName); 
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
		}
		return patientFolder;
	}
}
