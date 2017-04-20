package com.omri.patient.portlet.actioncommand;

import java.io.File;
import java.io.FileInputStream;
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
		String cptCode = ParamUtil.getString(actionRequest, "cptCode");
		String lopNotes = ParamUtil.getString(actionRequest, "lopNotes");
		String orderNotes = ParamUtil.getString(actionRequest, "orderNotes");
		String invoiceNotes = ParamUtil.getString(actionRequest, "invoiceNotes");
		String otherNotes = ParamUtil.getString(actionRequest, "otherNotes");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dob = dateFormat.parse(patientDOB);
		Patient patient = PatientLocalServiceUtil.createPatient(firstName, lastName, cptCode,dob, phoneNo,address1, address2, city, state, country, zip, lopNotes, orderNotes,invoiceNotes,otherNotes,themeDisplay.getUserId(), themeDisplay.getUserId());
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
		Folder patientFolder = getFolder(actionRequest, patient, PARENT_FOLDER_ID,String.valueOf(patient.getPatientId()));
		if(Validator.isNotNull(patientFolder)){
			fileUpload(actionRequest, patient, patientFolder,"lop");
			fileUpload(actionRequest, patient, patientFolder,"order");
			fileUpload(actionRequest, patient, patientFolder,"invoice");
			fileUpload(actionRequest, patient, patientFolder,"other");
		}
	}
	
	private Folder getFolder(ActionRequest actionRequest,Patient patient, long parentFolderId,String folderName) { 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean defaultFolderExist = isFolderExist(themeDisplay,parentFolderId,String.valueOf(patient.getPatientId())); 
		Folder patientFolder=null;
		if (!defaultFolderExist) {
			long repositoryId = themeDisplay.getCompanyGroupId();
			try {
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest); 
				patientFolder = DLAppServiceUtil.addFolder(repositoryId,parentFolderId, folderName,"", serviceContext);
			} catch (PortalException e1) { 
				LOG.error(e1.getMessage(), e1);
			} catch (SystemException e1) {
				LOG.error(e1.getMessage(), e1);
			}	
		}
		return patientFolder;
	}
	
	private void fileUpload(ActionRequest actionRequest, Patient patient, Folder parentFolder ,String fileType){ 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		String fileName="";
		File file = null; 
		String mimeType = "";
		String title = "";
		String description = ""; 
		long repositoryId = themeDisplay.getCompanyGroupId();
		Folder folder = null;
		try { 
			if(fileType.equals("lop")){
				fileName=uploadPortletRequest.getFileName("lopDocument");
				file = uploadPortletRequest.getFile("lopDocument"); 
				mimeType = uploadPortletRequest.getContentType("lopDocument");
				title = fileName;
				description = "Patient LOP Document";
				folder = getFolder(actionRequest,patient, parentFolder.getFolderId(),"LOP Documents"); 
			}
			if(fileType.equals("order")){
				fileName=uploadPortletRequest.getFileName("orderDocument");
				file = uploadPortletRequest.getFile("orderDocument"); 
				mimeType = uploadPortletRequest.getContentType("orderDocument");
				title = fileName;
				description = "Patient Order Document";
				folder = getFolder(actionRequest,patient, parentFolder.getFolderId(),"Order Documents"); 
			}
			if(fileType.equals("invoice")){
				fileName=uploadPortletRequest.getFileName("invoiceDocument");
				file = uploadPortletRequest.getFile("invoiceDocument"); 
				mimeType = uploadPortletRequest.getContentType("invoiceDocument");
				title = fileName;
				description = "Patient Invoice Document";
				folder = getFolder(actionRequest,patient, parentFolder.getFolderId(),"Invoice Documents"); 
			}
			if(fileType.equals("other")){
				fileName=uploadPortletRequest.getFileName("otherDocument");
				file = uploadPortletRequest.getFile("otherDocument"); 
				mimeType = uploadPortletRequest.getContentType("otherDocument");
				title = fileName;
				description = "Patient Other Document";
				folder = parentFolder; 
			}
			if(Validator.isNotNull(fileName) && Validator.isNotNull(folder)){
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest); 
				InputStream is = new FileInputStream( file );
				DLAppServiceUtil.addFileEntry(repositoryId, folder.getFolderId(), fileName, mimeType, title, description, "", is, file.getTotalSpace(), serviceContext); 
			}
			} catch (Exception e) {
				System.out.println(e.getMessage()); e.printStackTrace();
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
}
