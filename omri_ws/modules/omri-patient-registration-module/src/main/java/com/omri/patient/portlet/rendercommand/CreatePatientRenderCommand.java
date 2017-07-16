package com.omri.patient.portlet.rendercommand;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.patient.portlet.OmriPatientRegistrationModulemvcportletPortlet;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.exception.NoSuchResource_SpecificationException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Resource_Specification;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/create-patient"
	    },
	    service = MVCRenderCommand.class
)
public class CreatePatientRenderCommand implements MVCRenderCommand{
	private static Log _log = LogFactoryUtil.getLog(CreatePatientRenderCommand.class);
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
	ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
	long patientId = ParamUtil.getLong(renderRequest, "patientId");
	List<Clinic> clinicList = new ArrayList<Clinic>();
	long organizationId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themeDisplay.getUserId());
	long orgGroupId = OMRICommonLocalServiceUtil.getOrganizationGroupId(organizationId);
	Role clinicAdminRole;
	boolean isClinicAdmin = false;
	try {
		clinicAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Clinic Admin");
		isClinicAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), orgGroupId, clinicAdminRole.getRoleId());
	} catch (PortalException e) {
		_log.error(e.getMessage(), e);
	}
	
	if(isClinicAdmin){
		try {
			Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(organizationId);
			clinicList.add(clinic);
		} catch (NoSuchClinicException e) {
			_log.error(e.getMessage(), e);
		}
	}else{
		clinicList = ClinicLocalServiceUtil.getClinics(-1,-1);
	}
	renderRequest.setAttribute("clinicList", clinicList);
	
	setDoctorList(renderRequest);
	
	setLayerList(renderRequest);
	
	if(patientId>0){
		setPatientBeanForUpdatePatient(renderRequest);
	}
	renderRequest.setAttribute("patientId", patientId);
	return "/patient/create_patient.jsp";
	}
	
	private Role getRoleWithName(long companyId, String roleName){
		Role role = null;
		try {
			role = RoleLocalServiceUtil.getRole(companyId,roleName);
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return role;
	}
	
	private void setLayerList(RenderRequest renderRequest){
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try{
			Role lawyerAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer Admin");
			Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
			List<User>lawyeAdminList= new ArrayList<User>();
			List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(lawyerOrg.getGroupId(), lawyerAdminRole.getRoleId());
			for(UserGroupRole userGroupRole : userGroupRoleList){
				User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
				lawyeAdminList.add(user);
			}
			renderRequest.setAttribute("lawyerAdminList", lawyeAdminList);
			}catch(PortalException e){
				_log.error(e.getMessage(), e);
			}
	}
	
	private void setDoctorList(RenderRequest renderRequest){
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try{
			Role doctorAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor Admin");
			Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Doctor");
			List<User>doctorAdminList= new ArrayList<User>();
			List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(doctorOrg.getGroupId(), doctorAdminRole.getRoleId());
			for(UserGroupRole userGroupRole : userGroupRoleList){
				User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
				doctorAdminList.add(user);
			}
			renderRequest.setAttribute("doctorAdminList", doctorAdminList);
			}catch(PortalException e){
				_log.error(e.getMessage(), e);
			}
	}
	
	private void setPatientBeanForUpdatePatient(RenderRequest renderRequest){
		long patientId = ParamUtil.getLong(renderRequest, "patientId");
		String resourceIdDetail = StringPool.BLANK;
		try {
			Patient patient = PatientLocalServiceUtil.getPatient(patientId);
			Patient_Clinic patientClinic = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientId(patient.getPatientId());
			List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			if(Validator.isNotNull(patientClinic)){
				List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patientClinic.getPatientId(), patientClinic
						.getClinicId());
				for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
						Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
						Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
						Clinic clinic = ClinicLocalServiceUtil.getClinic(patientClinic.getClinicId());
						PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
						patientResourceBeanList.add(patientResourceBean);
						resourceIdDetail+= resource.getResourceId()+StringPool.COMMA;
				}
			}
			PatientBean patientBean= new PatientBean(patient, patientClinic,patientResourceBeanList);
			setClinicResourcesForEditPatient(patientClinic, renderRequest);
			renderRequest.setAttribute("patientBean", patientBean);
			renderRequest.setAttribute("resourceIdDetail", resourceIdDetail);
			
			CustomUser doctorUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(patientClinic.getDoctorId());
			CustomUser lawyerUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(patientClinic.getLawyerId());
			renderRequest.setAttribute("doctorFax", doctorUser.getFax());
			renderRequest.setAttribute("lawyerFax", lawyerUser.getFax());
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
	}
	
	private void setClinicResourcesForEditPatient(Patient_Clinic patientClinic, RenderRequest renderRequest){
		List<Clinic_Resource> clinicResourcesList = Clinic_ResourceLocalServiceUtil.getClinicResources(patientClinic.getClinicId());
		JSONArray resourceArray = JSONFactoryUtil.createJSONArray();
		for(Clinic_Resource clinicResource : clinicResourcesList){
			JSONObject clinicResourceJsonObj = JSONFactoryUtil.createJSONObject();
			clinicResourceJsonObj.put("resourceId", clinicResource.getResourceId());
			clinicResourceJsonObj.put("resourceName", clinicResource.getResourceName());
			List<Clinic_Resource> resourceSpecificationList = Clinic_ResourceLocalServiceUtil.getClinicResources(patientClinic.getClinicId(), clinicResource.getResourceId());
			JSONArray specificationArray = JSONFactoryUtil.createJSONArray();
			for(Clinic_Resource clinicResourceSpecification : resourceSpecificationList){
				try {
					Resource_Specification resourceSpecification = Resource_SpecificationLocalServiceUtil.getResourceSpecification(clinicResourceSpecification.getResourceId(), clinicResourceSpecification.getSpecificationId());
					JSONObject specJsonObj = JSONFactoryUtil.createJSONObject();
					specJsonObj.put("specificatonId", clinicResourceSpecification.getSpecificationId());
					specJsonObj.put("specificatonName", clinicResourceSpecification.getSpecificationName()+"("+resourceSpecification.getCptCode()+")");
					specificationArray.put(specJsonObj);
				} catch (NoSuchResource_SpecificationException e) {
					_log.error(e.getMessage(), e);
				}
				
			}
			clinicResourceJsonObj.put("specifications",specificationArray);
			resourceArray.put(clinicResourceJsonObj);
		}
		renderRequest.setAttribute("resourceArray", resourceArray);
	}
}
