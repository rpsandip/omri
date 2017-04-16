package com.omri.patient.portlet.actioncommand;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.patient.portlet.resourcecommand.GetClinicResourcesResourceCommand;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.util.PatientStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/user/create_patient"
	    },
	    service = MVCActionCommand.class
	)
public class CreatePatientActionCommand extends BaseMVCActionCommand{
	private static Log LOG = LogFactoryUtil.getLog(CreatePatientActionCommand.class);
	private static long PARENT_FOLDER_ID = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {
			Patient patient = addPatient(actionRequest);
			if(Validator.isNotNull(patient)){
				Patient_Clinic patientClinic = addPatientClinic(actionRequest, patient);
				if(Validator.isNotNull(patientClinic)){
					addPatientClinicResource(actionRequest, patientClinic);
				}
			    addPatientDocuments(actionRequest,patient);
				SessionMessages.add(actionRequest, "patient.added.successfully");
			}
		} catch (ParseException e) {
			actionResponse.setRenderParameter("mvcPath", "/user/create_user.jsp");
			LOG.error(e.getMessage(), e);
		}
	}
	
	private Patient addPatient(ActionRequest actionRequest) throws ParseException{
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		String phoneNo = ParamUtil.getString(actionRequest, "phoneNo");
		String patientDOB = ParamUtil.getString(actionRequest, "patientDOB");
		String address1 = ParamUtil.getString(actionRequest, "address1");
		String address2 = ParamUtil.getString(actionRequest, "address2");
		String city = ParamUtil.getString(actionRequest, "city");
		String state = ParamUtil.getString(actionRequest, "state");
		String country = ParamUtil.getString(actionRequest, "country");
		String zip = ParamUtil.getString(actionRequest, "zip");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dob = dateFormat.parse(patientDOB);
		Patient patient = PatientLocalServiceUtil.createPatient(firstName, lastName, dob, phoneNo,address1, address2, city, state, country, zip, themeDisplay.getUserId(), themeDisplay.getUserId());
		return patient;
	}

	private Patient_Clinic addPatientClinic(ActionRequest actionRequest, Patient patient){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long  doctorId = ParamUtil.getLong(actionRequest, "doctor");
		long clinicId = ParamUtil.getLong(actionRequest, "clinic");
		int status = ParamUtil.getInteger(actionRequest, "patient_status");
		Patient_Clinic patientClinic = null;
		try {
			User user = UserLocalServiceUtil.getUser(doctorId);
			String doctorPhonNo = ParamUtil.getString(actionRequest, "doctorPhone");
			patientClinic = Patient_ClinicLocalServiceUtil.addPatient_Clinic(patient.getPatientId(), clinicId, doctorId, user.getFirstName()+StringPool.BLANK+user.getLastName(), doctorPhonNo, status,themeDisplay.getUserId(), themeDisplay.getUserId());
			
		} catch (PortalException e) {
			LOG.error(e.getMessage(), e);
		}
		return patientClinic;
	}
	
	private void addPatientClinicResource(ActionRequest actionRequest,Patient_Clinic patientClinic){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		int resourcesCounts =  ParamUtil.getInteger(actionRequest, "resourceCount");
		resourcesCounts++;
		for (int i=0; i<=resourcesCounts;i++) {
			long resourceId = ParamUtil.getLong(actionRequest, "resource" + i);
			long specificationId = ParamUtil.getLong(actionRequest, "specification" + i);
			int occurance = ParamUtil.getInteger(actionRequest, "occurance"+i);
			if(resourceId!=0 && specificationId!=0){
				try{
				Patient_Clinic_ResourceLocalServiceUtil.addPatientClinicResource(patientClinic.getPatientId(), resourceId, patientClinic.getClinicId(), specificationId, occurance, themeDisplay.getUserId(), themeDisplay.getUserId() );
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private void addPatientDocuments(ActionRequest actionRequest,Patient patient){
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean isFolderCreated = createFolder(actionRequest, patient);
		if(isFolderCreated){
			fileUpload(themeDisplay, actionRequest, patient);
		}
	}
	
	private boolean createFolder(ActionRequest actionRequest,Patient patient) { 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean defaultFolderExist = isFolderExist(themeDisplay,String.valueOf(patient.getPatientId())); 
		boolean isFolderCreated=false;
		if (!defaultFolderExist) {
			long repositoryId = themeDisplay.getCompanyGroupId();
			try {
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest); 
				DLAppServiceUtil.addFolder(repositoryId,PARENT_FOLDER_ID, String.valueOf(patient.getPatientId()),"", serviceContext);
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
	
	private void fileUpload(ThemeDisplay themeDisplay,ActionRequest actionRequest, Patient patient){ 
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		Map<String, FileItem[]> files= uploadPortletRequest.getMultipartParameterMap(); 
		Folder uploadFolder = getFolder(themeDisplay,String.valueOf(patient.getPatientId()));
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
