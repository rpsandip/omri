package com.omri.portlet.rendercommand;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.DocumentBean;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.exception.NoSuchPatient_ClinicException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.util.AppointmentStatus;



@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet",
	        "mvc.command.name=/monthwise-patients"
	    },
	    service = MVCRenderCommand.class
)
public class MonthWisePatientListRenderCommand implements MVCRenderCommand{
	Log _log = LogFactoryUtil.getLog(MonthWisePatientListRenderCommand.class.getName());
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		int month = ParamUtil.getInteger(renderRequest, "month");
		int year = ParamUtil.getInteger(renderRequest, "year");
		Date startDate=null;
		Date endDate=null;
		
		Calendar c = Calendar.getInstance();
	    if(month!=-1){
		int day = 1;
	    c.set(year, month, day,0,0,0);
	    int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	    startDate = c.getTime();
	    c.set(year, month, numOfDaysInMonth, 23, 59,59);
	    endDate = c.getTime();
	    }else{
	    	c.set(Calendar.YEAR, year);
	    	c.set(Calendar.DAY_OF_YEAR, 1);    
	    	startDate = c.getTime();
	    	
	    	c.set(Calendar.YEAR, year);
	    	c.set(Calendar.MONTH, 11); 
	    	c.set(Calendar.DAY_OF_MONTH, 31);
	    	c.set(Calendar.HOUR, 23);
	    	c.set(Calendar.MINUTE,59);
	    	c.set(Calendar.SECOND,59);
	    	endDate = c.getTime();
	    }
	    
	    List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
		renderRequest.setAttribute("year", year);
		renderRequest.setAttribute("month", getMonthName(month));
		
		long userAssociatedOrgId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themdeDisplay.getUserId());
		long userAssociatedOrgGroupId = OMRICommonLocalServiceUtil.getOrganizationGroupId(userAssociatedOrgId);
		
		boolean isClinicAdmin = false;
		boolean isDoctorAdmin = false;
		boolean isLawyerAdmin = false;
		
		try{
		Role clinicAdminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Clinic Admin");
		Role doctorAdminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Doctor Admin");
		Role lawyerAdminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Lawyer Admin");
		
		isClinicAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), userAssociatedOrgGroupId, clinicAdminRole.getRoleId());
		isDoctorAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), userAssociatedOrgGroupId, doctorAdminRole.getRoleId());
		isLawyerAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), userAssociatedOrgGroupId, lawyerAdminRole.getRoleId());
	
		}catch(PortalException e){
			_log.error(e.getMessage(), e);
		}
		
		if(isClinicAdmin){
			// For Clinic Admin
			patientBeanList =  getClinicAdminPatientList(renderRequest, renderResponse, startDate, endDate);
		}
		
		// For Lawyer or Doctor admin
		if(isDoctorAdmin | isLawyerAdmin){
			patientBeanList = getLawyerOrDoctorPatientList(renderRequest, renderResponse);
		}
		
		// For Admin
		try {
			Role adminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), RoleConstants.ADMINISTRATOR);
			boolean hasAdminRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), adminRole.getRoleId());
			if(hasAdminRole){
				patientBeanList = getAdminPatientList(renderRequest, renderResponse);
			}
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		
		// For System Admin
		try {
			Role systemAdminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "System Admin");
			boolean hasSystemAdminRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), systemAdminRole.getRoleId());
			if(hasSystemAdminRole){
				patientBeanList = getAdminPatientList(renderRequest, renderResponse);
			}
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		
		
		_log.info("patientBeanList size -> " + patientBeanList.size());
		renderRequest.setAttribute("patientBeanList", patientBeanList);
		return "/monthwise_patients.jsp";
	}
	
	private List<PatientBean> getClinicAdminPatientList(RenderRequest renderRequest, RenderResponse renderResponse, Date startDate, Date endDate){
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
		long clinicOrganizationId =  OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themdeDisplay.getUserId()); 
		if(clinicOrganizationId!=0){
			try {
				Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
				List<Patient_Clinic> clinicPatientList = Patient_ClinicLocalServiceUtil.getPatientsOfClinic(clinic.getClinicId(), startDate, endDate);
				for(Patient_Clinic patientClinic : clinicPatientList){
					List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
					try {
						Patient patient = PatientLocalServiceUtil.getPatient(patientClinic.getPatientId());
						if(Validator.isNotNull(patientClinic)){
							List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patientClinic.getPatientId(), patientClinic
									.getClinicId());
							for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
								try {
									Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
									Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
									PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
									patientResourceBeanList.add(patientResourceBean);
								} catch (PortalException e) {
									_log.error(e.getMessage(), e);
								}
							}
							
							PatientBean patientBean= new PatientBean(patient, patientClinic,patientResourceBeanList);
							setPatientDocuments(themdeDisplay.getCompanyGroupId(), patientBean);
							patientBeanList.add(patientBean);
						}
						
					} catch (NoSuchPatient_ClinicException e) {
						_log.error(e.getMessage(), e);
					} catch(PortalException e){
						
					}
				}
			} catch (NoSuchClinicException e) {
				_log.error(e.getMessage(), e);
			}
		}
		return patientBeanList;
	}
	
	private List<PatientBean> getLawyerOrDoctorPatientList(RenderRequest renderRequest, RenderResponse renderResponse){
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
		List<Patient> patientListCreatedByUser = PatientLocalServiceUtil.getCreatedPatientList(themdeDisplay.getUserId());
		for(Patient patient : patientListCreatedByUser){
			List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			try {
				Patient_Clinic patientClinc = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientIdandCreatorUserId(patient.getPatientId(), themdeDisplay.getUserId());
				if(Validator.isNotNull(patientClinc)){
					List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicIdAndCreateUserId(patient.getPatientId(), patientClinc.getClinicId(), themdeDisplay.getUserId());
					for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
						try {
							Clinic clinic = ClinicLocalServiceUtil.getClinic(patientClinc.getClinicId());
							Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
							Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
							PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
							patientResourceBeanList.add(patientResourceBean);
						} catch (PortalException e) {
							_log.error(e.getMessage(), e);
						}
					}
				}
				PatientBean patientBean= new PatientBean(patient, patientClinc,patientResourceBeanList);
				setPatientDocuments(themdeDisplay.getCompanyGroupId(), patientBean);
				patientBeanList.add(patientBean);
			} catch (NoSuchPatient_ClinicException e) {
				_log.error(e.getMessage(), e);
			}
			
		}
		return patientBeanList;
	}
	
	private List<PatientBean> getAdminPatientList(RenderRequest renderRequest, RenderResponse renderResponse){
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
		List<Patient> allPatientList = PatientLocalServiceUtil.getPatients(-1, -1);
		List<Patient> modifiablePatientList = new ArrayList<Patient>(allPatientList);
		Comparator<Patient> comp = (Patient o1, Patient o2) -> {
		    return o2.getModifiedDate().compareTo(o1.getModifiedDate());
		};
		Collections.sort(modifiablePatientList, comp);
		for(Patient patient : modifiablePatientList){
			List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			try {
				Patient_Clinic patientClinc = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientId(patient.getPatientId());
				if(Validator.isNotNull(patientClinc)){
					List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patient.getPatientId(), patientClinc.getClinicId());
					for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
						try {
							Clinic clinic = ClinicLocalServiceUtil.getClinic(patientClinc.getClinicId());
							Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
							Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
							PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
							patientResourceBeanList.add(patientResourceBean);
						} catch (PortalException e) {
							_log.error(e.getMessage(), e);
						}
					}
				}
				PatientBean patientBean= new PatientBean(patient, patientClinc,patientResourceBeanList);
				setPatientDocuments(themdeDisplay.getCompanyGroupId(), patientBean);
				patientBeanList.add(patientBean);
			} catch (NoSuchPatient_ClinicException e) {
				_log.error(e.getMessage(), e);
			}
		}
		return patientBeanList;
	}
	
	private void setPatientDocuments(long companyGroupId, PatientBean patientBean){
		List<DocumentBean> documentBeanList = new ArrayList<DocumentBean>();
		
		Folder patientFolder=null;
		try {
			patientFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, 0l, String.valueOf(patientBean.getPatientId()));
		} catch (PortalException e1) {
			_log.error(e1.getMessage(), e1);
		}
		
		if(Validator.isNotNull(patientFolder)){
		try {
			
			// Set LOP Requests
			Folder lopRequestFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "LOP Requests");
			List<DLFileEntry> lopRequestDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, lopRequestFolder.getFolderId());
			for(DLFileEntry fileEntry : lopRequestDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setLopRequestDocuments(documentBeanList);
			
			// Set LOP Documents
			Folder LOPFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "LOP");
			List<DLFileEntry> lopDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, LOPFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : lopDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setLopDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			// Set Invoice documents
			Folder invoiceFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "Invoice");
			List<DLFileEntry> invoiceDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, invoiceFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : invoiceDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setInvoiceDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			
			// Set Order documents
			Folder orderFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "Order");
			List<DLFileEntry> orderDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, orderFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : orderDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setOrderDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			// Procedure documents
			List<Appointment> patientCompleteAppoinmtnet = AppointmentLocalServiceUtil.getAppointmentsByStatusAndPatientId(patientBean.getPatientId(), AppointmentStatus.TECHNOLOGIST_REPORT_SUBMITTED.getValue());
			documentBeanList = new ArrayList<DocumentBean>();
			for(Appointment appointment : patientCompleteAppoinmtnet){
				Folder appointmentFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), appointment.getAppointmentId()+StringPool.UNDERLINE+appointment.getPatientId());
				List<DLFileEntry> appointmentDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, appointmentFolder.getFolderId());
				documentBeanList = new ArrayList<DocumentBean>();
				for(DLFileEntry fileEntry : appointmentDocuments){
					DocumentBean documentBean = new DocumentBean();
					documentBean.setTitle(fileEntry.getFileName());
					documentBean.setDownLoadURL(getDLFileURL(fileEntry));
					documentBeanList.add(documentBean);
				}
				
			}
			patientBean.setProcedureDocumnts(documentBeanList);
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}	
	  }
	}
	public static String getDLFileURL(DLFileEntry file) {
		       return "/documents/" + file.getGroupId() + StringPool.FORWARD_SLASH + file.getFolderId() + StringPool.FORWARD_SLASH
		            + file.getTitle() + StringPool.FORWARD_SLASH + file.getUuid();
	}
	
	private String getMonthName(int month){
		switch(month) {
		case 0 : return "January";
		case 1 : return "February";
		case 2 : return "March";
		case 3 : return "April";
		case 4 : return "May";
		case 5 : return "June";
		case 6 : return "July";
		case 7 : return "August";
		case 8 : return "September";
		case 9 : return "October";
		case 10 : return "November";
		case 11 : return "December";
		default : return "";
		}
	}

}
