package com.omri.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;

import java.io.IOException;
import java.util.Calendar;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=omri-month-wise-dashboard Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriMonthWiseDashboardmvcportletPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(OmriMonthWiseDashboardmvcportletPortlet.class);
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		
	  ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		
	   OMRICommonLocalServiceUtil.setDoctorList(renderRequest);
	   OMRICommonLocalServiceUtil.setLayerList(renderRequest);
	   
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
		
		renderRequest.setAttribute("isClinicAdmin", isClinicAdmin);
		renderRequest.setAttribute("isDoctorAdmin", isDoctorAdmin);
		renderRequest.setAttribute("isLawyerAdmin", isLawyerAdmin);
		
		include(viewTemplate, renderRequest, renderResponse);
	}
	
}