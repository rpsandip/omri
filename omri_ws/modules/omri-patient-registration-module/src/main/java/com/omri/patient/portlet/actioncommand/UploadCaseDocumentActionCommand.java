package com.omri.patient.portlet.actioncommand;

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
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
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
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.model.Patient;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/upload-patient-document"
	    },
	    service = MVCActionCommand.class
	)
public class UploadCaseDocumentActionCommand extends BaseMVCActionCommand{

	private static Log LOG = LogFactoryUtil.getLog(CreatePatientActionCommand.class);
	private static long PARENT_FOLDER_ID = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		long patientId = ParamUtil.getLong(actionRequest, "patientId");
		addPatientDocuments(actionRequest,patientId);
	}
	
	private void addPatientDocuments(ActionRequest actionRequest, long patientId){
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean isFolderCreated = createFolder(actionRequest, patientId);
		if(isFolderCreated){
			fileUpload(themeDisplay, actionRequest, patientId);
		}
	}
	
	private boolean createFolder(ActionRequest actionRequest,long patientId) { 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean defaultFolderExist = isFolderExist(themeDisplay,String.valueOf(patientId)); 
		boolean isFolderCreated=false;
		if (!defaultFolderExist) {
			long repositoryId = themeDisplay.getCompanyGroupId();
			try {
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest); 
				DLAppServiceUtil.addFolder(repositoryId,PARENT_FOLDER_ID, String.valueOf(patientId),"", serviceContext);
				isFolderCreated=true;
			} catch (PortalException e1) { 
				LOG.error(e1.getMessage(), e1);
			} catch (SystemException e1) {
				LOG.error(e1.getMessage(), e1);
			}	
		}else{
			isFolderCreated = true;
		}
		return isFolderCreated;
	}
	
	private void fileUpload(ThemeDisplay themeDisplay,ActionRequest actionRequest, long patientId){ 
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		Map<String, FileItem[]> files= uploadPortletRequest.getMultipartParameterMap(); 
		Folder uploadFolder = getFolder(themeDisplay,String.valueOf(patientId));
		InputStream is;
		String title, description,mimeType,extension; 
		long repositoryId=0l; 
		for (Entry<String, FileItem[]> file2 : files.entrySet()) {
			FileItem item[] =file2.getValue(); 
			try { 
				for (FileItem fileItem : item) {
					title = fileItem.getFileName(); 
					description = title;
					repositoryId = themeDisplay.getCompanyGroupId();
					mimeType = fileItem.getContentType();
					extension = fileItem.getFileNameExtension();
					is =fileItem.getInputStream();
					try { 
						ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest);
						FileEntry fileEntry = null;
						try{
							fileEntry = DLAppServiceUtil.addFileEntry(repositoryId, uploadFolder.getFolderId(), title, mimeType, title, description, "", is, fileItem.getSize(), serviceContext);
						}catch(DuplicateFileException | DuplicateFileEntryException dfe){
							title = title+ StringPool.UNDERLINE +new Date().getTime();
							fileEntry = DLAppServiceUtil.addFileEntry(repositoryId, uploadFolder.getFolderId(), title, mimeType, title, description, "", is, fileItem.getSize(), serviceContext);
						}
					} catch (PortalException e) { 
						LOG.error(e.getMessage(), e);
					} catch (SystemException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}	
		}
	  }
	
	private boolean isFolderExist(ThemeDisplay themeDisplay,String folderName){
		boolean folderExist = false;
		try { 
			DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), PARENT_FOLDER_ID, folderName); 
			folderExist = true;
		} catch (Exception e) {	
			//LOG.error(e.getMessage(), e);
		} return folderExist; 
	}
	
	private Folder getFolder(ThemeDisplay themeDisplay, String folderName){
		Folder folder = null; 
		try { 
			folder =DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), PARENT_FOLDER_ID, folderName);
		} catch (Exception e) {
			//LOG.error(e.getMessage(), e);
		} return folder; 
	}
}
