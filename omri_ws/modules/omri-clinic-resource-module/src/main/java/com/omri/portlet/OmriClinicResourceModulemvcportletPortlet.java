package com.omri.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
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
		"javax.portlet.display-name=omri-clinic-resource-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriClinicResourceModulemvcportletPortlet extends MVCPortlet {
	   private static Log _log = LogFactoryUtil.getLog(OmriClinicResourceModulemvcportletPortlet.class);
		@Override
		public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {
			ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			List<Resource> resourceList = new ArrayList<Resource>();
			try {
				Role clinicRegularRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Clinic");
				boolean hasClinicRole = RoleLocalServiceUtil.hasUserRole(themdeDisplay.getUserId(), clinicRegularRole.getRoleId());
				if(hasClinicRole){
					long clinicOrganizationId = getUserAssociatedLawFirmOrgId(themdeDisplay.getUserId()); 
					Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
					List<Clinic_Resource> clinicResourceList= Clinic_ResourceLocalServiceUtil.getClinicResources(clinic.getClinicId());
					for(Clinic_Resource clinicResource : clinicResourceList){
						try{
							Resource resource = ResourceLocalServiceUtil.getResource(clinicResource.getResourceId());
							resourceList.add(resource);
						}catch(PortalException e){
							_log.error(e.getMessage(), e);
						}
					}
				}
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			renderRequest.setAttribute("resourceList", resourceList);
			include(viewTemplate, renderRequest, renderResponse);
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