package com.omri.patient.portlet.rendercommand;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
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
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.patient.portlet.OmriPatientRegistrationModulemvcportletPortlet;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;

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
}
