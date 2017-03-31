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
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
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
			boolean isLawyerAdmin = false;
			boolean isDoctorAdmin = false;
			Role administratorRole = getRoleWithName(themeDisplay.getCompanyId(), RoleConstants.ADMINISTRATOR);
			Role lawyerAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer Admin");
			Role doctorAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor Admin");
			if(Validator.isNotNull(administratorRole)){
				isAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),administratorRole.getRoleId());
			}
			if(Validator.isNotNull(isLawyerAdmin)){
				isLawyerAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),lawyerAdminRole.getRoleId());
			}
			if(Validator.isNotNull(doctorAdminRole)){
				isDoctorAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(),doctorAdminRole.getRoleId());
			}

//			if(isAdmin){
//				roles.add(lawyerAdminRole);
//				roles.add(doctorAdminRole);
//				
//			}
//			if(isLawyerAdmin || isAdmin){
//				Role receptionistRole = getRoleWithName(themeDisplay.getCompanyId(),"Receptionist");
//				Role caseManagerRole = getRoleWithName(themeDisplay.getCompanyId(),"Case Manager");
//				Role attorneyRole = getRoleWithName(themeDisplay.getCompanyId(),"Attorney");
//				if(Validator.isNotNull(receptionistRole))
//				roles.add(receptionistRole);
//				if(Validator.isNotNull(caseManagerRole))
//				roles.add(caseManagerRole);
//				if(Validator.isNotNull(attorneyRole));
//				roles.add(attorneyRole);
//				
//			}
//			if(isDoctorAdmin || isAdmin){
//				Role receptionistRole = getRoleWithName(themeDisplay.getCompanyId(),"Receptionist");
//				Role medicalAssistanceRole = getRoleWithName(themeDisplay.getCompanyId(),"Medical Assistants");
//				Role nurseRole = getRoleWithName(themeDisplay.getCompanyId(),"Nurse");
//				
//				if(Validator.isNotNull(receptionistRole))
//				roles.add(receptionistRole);
//				if(Validator.isNotNull(medicalAssistanceRole))
//				roles.add(medicalAssistanceRole);
//				if(Validator.isNotNull(nurseRole))
//				roles.add(nurseRole);
//			}
			renderRequest.setAttribute("isAdmin", isAdmin);
			renderRequest.setAttribute("isLawyerAdmin", isLawyerAdmin);
			renderRequest.setAttribute("isDoctorAdmin", isDoctorAdmin);
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
