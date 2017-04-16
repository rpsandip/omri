package com.example.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.exception.NoSuchPatient_ClinicException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=omri-dashboard-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriDashboardModulemvcportletPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(OmriDashboardModulemvcportletPortlet.class);
		@Override
		public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {
			ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
			
			// For Clinic
			try {
				Role clinicRegularRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Clinic");
				boolean hasClinicRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), clinicRegularRole.getRoleId());
				if(hasClinicRole){
					patientBeanList = getClinicPatientList(renderRequest, renderResponse);
				}
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}	
			// For Lawyer
			try {
				Role LawyerRegularRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Lawyer");
				boolean hasLawyerRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), LawyerRegularRole.getRoleId());
				if(hasLawyerRole){
					patientBeanList = getLawyerOrDoctorPatientList(renderRequest, renderResponse);
				}
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
			// For Doctor
			try {
				Role doctorRegularRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Doctor");
				boolean hasDoctorRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), doctorRegularRole.getRoleId());
				if(hasDoctorRole){
					patientBeanList = getLawyerOrDoctorPatientList(renderRequest, renderResponse);
				}
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
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
			
			
			renderRequest.setAttribute("patientBeanList", patientBeanList);
			include(viewTemplate, renderRequest, renderResponse);
		}
		
		private List<PatientBean> getClinicPatientList(RenderRequest renderRequest, RenderResponse renderResponse){
			ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
			long clinicOrganizationId = getUserAssociatedLawFirmOrgId(themdeDisplay.getUserId()); 
			if(clinicOrganizationId!=0){
				try {
					Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
					List<Patient_Clinic> clinicPatientList = Patient_ClinicLocalServiceUtil.getPatientsOfClinic(clinic.getClinicId());
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
							}
							PatientBean patientBean= new PatientBean(patient, patientClinic,patientResourceBeanList);
							patientBeanList.add(patientBean);
						} catch (NoSuchPatient_ClinicException e) {
							_log.error(e.getMessage(), e);
						} catch(PortalException e){
							_log.error(e.getMessage(), e);
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
					patientBeanList.add(patientBean);
				} catch (NoSuchPatient_ClinicException e) {
					_log.error(e.getMessage(), e);
				}
			}
			return patientBeanList;
		}
		
		 public long getUserAssociatedLawFirmOrgId(long userId) {
		    List<Organization> userOrganizationList =
		        OrganizationLocalServiceUtil.getUserOrganizations(userId);
		    if (userOrganizationList.size() > 0) {
		      _log.debug(" UserId ->" + userId + " LawFirmOrgId ->"
		          + userOrganizationList.get(0).getOrganizationId());
		      return userOrganizationList.get(0).getOrganizationId();
		    }
		    _log.debug(" UserId ->" + userId + " LawFirmOrgId ->" + 0l);
		    return 0l;
		  }
}