package com.omri.user.mgmt.portlet.rendercommand;

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
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.user.mgmt.portlet.util.UserModuleContstant;

@Component(
		immediate = true,
		property = { 
				"javax.portlet.name=" + UserModuleContstant.PORTLET_ID,
				"mvc.command.name=/create-user" 
		}, 
		service = MVCRenderCommand.class)
public class CreateUserRenderCommand implements MVCRenderCommand{
	private static Log _log = LogFactoryUtil.getLog(CreateUserRenderCommand.class);
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		List<Role> userRoles = getCreateUserPageRoleList(renderRequest, renderResponse);
		renderRequest.setAttribute("roles", userRoles);
		return "/user/create_user.jsp";
	}
	
	private List<Role> getCreateUserPageRoleList(RenderRequest renderRequest, RenderResponse renderResponse){
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			List<Role> roles = new ArrayList<Role>();

			boolean isAdmin= false;
			boolean isDoctorAdmin=false;
			boolean isLawyerAdmin=false;
			boolean isClinicAdmin=false;
			boolean isSystemAdmin=false;
			
			boolean hasLawyerRole = false;
			boolean hasDoctorRole = false;
			boolean hasClinicRole = false;
			
			long userAssociatedOrgId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themeDisplay.getUserId());
			long userAssociatedOrgGroupId = OMRICommonLocalServiceUtil.getOrganizationGroupId(userAssociatedOrgId);
			
			Role administratorRole = getRoleWithName(themeDisplay.getCompanyId(), RoleConstants.ADMINISTRATOR);
			try {
				Role lawyerAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Lawyer Admin");
				Role doctorAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Doctor Admin");
				Role clinicAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Clinic Admin");
				Role systemAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "System Admin");
				Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Doctor");
				Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
				isLawyerAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), lawyerOrg.getGroupId(), lawyerAdminRole.getRoleId());
				isDoctorAdmin =UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), doctorOrg.getGroupId(),doctorAdminRole.getRoleId());
				if(userAssociatedOrgGroupId>0){
					isClinicAdmin =UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), userAssociatedOrgGroupId,clinicAdminRole.getRoleId());
				}
			
			if(Validator.isNotNull(administratorRole)){
				isAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),administratorRole.getRoleId());
			}
			if(Validator.isNotNull(lawyerAdminRole)){
				hasLawyerRole = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), userAssociatedOrgGroupId, lawyerAdminRole.getRoleId());
			}
			if(Validator.isNotNull(doctorAdminRole)){
				hasDoctorRole = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), userAssociatedOrgGroupId, doctorAdminRole.getRoleId());
			}
			if(Validator.isNotNull(clinicAdminRole)){
				hasClinicRole = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), userAssociatedOrgGroupId, clinicAdminRole.getRoleId());
			}
			if(Validator.isNotNull(systemAdminRole)){
				isSystemAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(), systemAdminRole.getRoleId());
			}
			
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
			renderRequest.setAttribute("isAdmin", isAdmin);
			renderRequest.setAttribute("hasLawyerRole", hasLawyerRole);
			renderRequest.setAttribute("hasDoctorRole", hasDoctorRole);
			renderRequest.setAttribute("hasClinicRole", hasClinicRole);
			
			renderRequest.setAttribute("isLawyerAdmin", isLawyerAdmin);
			renderRequest.setAttribute("isDoctorAdmin", isDoctorAdmin);
			renderRequest.setAttribute("isClinicAdmin", isClinicAdmin);
			renderRequest.setAttribute("isSystemAdmin", isSystemAdmin);
		return roles;
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
}
