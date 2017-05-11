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
			
			boolean hasLawyerRole = false;
			boolean hasDoctorRole = false;
			boolean hasClinicRole = false;
			
			long clinicGroupId = getUserAssociatedLawFirmGroupId(themeDisplay.getUserId());
			
			Role administratorRole = getRoleWithName(themeDisplay.getCompanyId(), RoleConstants.ADMINISTRATOR);
			Role lawyerRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer");
			Role doctorRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor");
			Role clinicRole = getRoleWithName(themeDisplay.getCompanyId(), "Clinic");
			
			
			try {
				Role lawyerAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Lawyer Admin");
				Role doctorAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Doctor Admin");
				Role clinicAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Clinic Admin");
				Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Doctor");
				Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
				isLawyerAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), lawyerOrg.getGroupId(), lawyerAdminRole.getRoleId());
				isDoctorAdmin =UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), doctorOrg.getGroupId(),doctorAdminRole.getRoleId());
				if(clinicGroupId>0){
					isClinicAdmin =UserGroupRoleLocalServiceUtil.hasUserGroupRole(themeDisplay.getUserId(), clinicGroupId,clinicAdminRole.getRoleId());
				}
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
			if(Validator.isNotNull(administratorRole)){
				isAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),administratorRole.getRoleId());
			}
			if(Validator.isNotNull(lawyerRole)){
				hasLawyerRole = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),lawyerRole.getRoleId());
			}
			if(Validator.isNotNull(doctorRole)){
				hasDoctorRole = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),doctorRole.getRoleId());
			}
			if(Validator.isNotNull(clinicRole)){
				hasClinicRole = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),clinicRole.getRoleId());
			}
			renderRequest.setAttribute("isAdmin", isAdmin);
			renderRequest.setAttribute("hasLawyerRole", hasLawyerRole);
			renderRequest.setAttribute("hasDoctorRole", hasDoctorRole);
			renderRequest.setAttribute("hasClinicRole", hasClinicRole);
			
			renderRequest.setAttribute("isLawyerAdmin", isLawyerAdmin);
			renderRequest.setAttribute("isDoctorAdmin", isDoctorAdmin);
			renderRequest.setAttribute("isClinicAdmin", isClinicAdmin);
		return roles;
	}
	
	 public long getUserAssociatedLawFirmGroupId(long userId) {
		    List<Organization> userOrganizationList =
		        OrganizationLocalServiceUtil.getUserOrganizations(userId);
		    if (userOrganizationList.size() > 0) {
		      _log.debug(" UserId ->" + userId + " LawFirmOrgId ->"
		          + userOrganizationList.get(0).getGroupId());
		      return userOrganizationList.get(0).getGroupId();
		    }
		    _log.debug(" UserId ->" + userId + " LawFirmOrgId ->" + 0l);
		    return 0l;
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
