package com.omri.user.mgmt.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.UserBean;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.omri",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=omri-user-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"com.liferay.portlet.action-url-redirect=true",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class UserManagmentPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(UserManagmentPortlet.class);
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<UserBean> childUserBeanList = new ArrayList<UserBean>();
		
		try{
			Role clinicAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Clinic Admin");
			Role doctorAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Doctor Admin");
			Role lawyerAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Lawyer Admin");
			Role adminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), RoleConstants.ADMINISTRATOR);
			Role systemAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "System Admin");
			Role technologistRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Technologist");
			Role billingRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Billing Person");
			boolean isSystemAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(), systemAdminRole.getRoleId());
			boolean isSuperAdmin = RoleLocalServiceUtil.hasUserRole(themeDisplay.getUserId(), adminRole.getRoleId());
			
			Map<String,Role> roleMap = new HashMap<String,Role>();
			roleMap.put("clinic", clinicAdminRole);
			roleMap.put("doctor", doctorAdminRole);
			roleMap.put("lawyer", lawyerAdminRole);
			roleMap.put("admin", adminRole);
			roleMap.put("systemadmin", systemAdminRole);
			roleMap.put("technologist", technologistRole);
			roleMap.put("billing", billingRole);
			
			if(isSuperAdmin || isSystemAdmin){
				List<User> userList = UserLocalServiceUtil.getUsers(-1, -1);
				for(User user : userList){
					try{
					CustomUser customUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(user.getUserId());
					UserBean userBean = new UserBean(user, customUser);
					setEntityAndRole(roleMap, userBean, renderRequest);
					childUserBeanList.add(userBean);
					}catch(PortalException e){
						_log.error(e.getMessage());
					}
				}
			}else{
				List<CustomUser> childUserList = CustomUserLocalServiceUtil.getChildUsers(themeDisplay.getUserId());
				for(CustomUser customUser:childUserList){
					try {
						User user = UserLocalServiceUtil.getUser(customUser.getLrUserId());
						UserBean userBean = new UserBean(user, customUser);
						setEntityAndRole(roleMap, userBean, renderRequest);
						childUserBeanList.add(userBean);
					} catch (PortalException e) {
						_log.error(e.getMessage(), e);
					}
				}
			}
		}catch(PortalException e){
			_log.error(e.getMessage(), e);
		}
		renderRequest.setAttribute("childUserBeanList", childUserBeanList);
		include(viewTemplate, renderRequest, renderResponse);
	}
	
	private void setEntityAndRole(Map<String,Role> roleMap ,UserBean userBean, RenderRequest renderRequest){
		
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		long userAssociatedOrgId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(userBean.getUser().getUserId());
		long userAssociatedOrgGroupId = OMRICommonLocalServiceUtil.getOrganizationGroupId(userAssociatedOrgId);
		
		boolean isClinicAdmin = false;
		boolean isDoctorAdmin = false;
		boolean isLawyerAdmin = false;
		boolean isTechnologist = false;
		boolean isBillingPerson = false;
		
		isClinicAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userBean.getUser().getUserId(), userAssociatedOrgGroupId, roleMap.get("clinic").getRoleId());
		isDoctorAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userBean.getUser().getUserId(), userAssociatedOrgGroupId, roleMap.get("doctor").getRoleId());
		isLawyerAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userBean.getUser().getUserId(), userAssociatedOrgGroupId, roleMap.get("lawyer").getRoleId());
		isTechnologist = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userBean.getUser().getUserId(), userAssociatedOrgGroupId, roleMap.get("technologist").getRoleId());
		isBillingPerson = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userBean.getUser().getUserId(), userAssociatedOrgGroupId, roleMap.get("billing").getRoleId());
		
		boolean hasAdminRole = RoleLocalServiceUtil.hasUserRole(userBean.getUser().getUserId(), roleMap.get("admin").getRoleId());
		boolean hasSystemAdminRole = RoleLocalServiceUtil.hasUserRole(userBean.getUser().getUserId(), roleMap.get("systemadmin").getRoleId());
		
		if(isClinicAdmin){
			userBean.setRoleName("Clinic Admin");
			userBean.setEntity("Clinic");
		}
		if(isDoctorAdmin){
			userBean.setRoleName("Doctor Admin");
			userBean.setEntity("Doctor");
		}
		if(isLawyerAdmin){
			userBean.setRoleName("Lawyer Admin");
			userBean.setEntity("Lawyer");
		}
		if(hasAdminRole){
			userBean.setRoleName(RoleConstants.ADMINISTRATOR);
			userBean.setEntity("Admin");
		}
		if(hasSystemAdminRole){
			userBean.setRoleName("System Admin");
			userBean.setEntity("Admin");
		}
		
		if(isTechnologist){
			userBean.setRoleName("Technologist");
			userBean.setEntity("Clinic");
		}
		
		if(isBillingPerson){
			userBean.setRoleName("Billing Person");
			userBean.setEntity("Clinic");
		}


	}
}