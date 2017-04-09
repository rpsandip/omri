package com.omri.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.StringUtil;
import com.omri.service.common.beans.ClientResourceBean;
import com.omri.service.common.beans.ClinicBean;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

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
		"com.liferay.portlet.display-category=category.omri",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=omri-clinic-registration-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriClinicRegistrationModulemvcportletPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(OmriClinicRegistrationModulemvcportletPortlet.class);
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		List<Clinic> clinicList = ClinicLocalServiceUtil.getClinics(-1, -1);
		List<ClinicBean> clinicBeanList = new ArrayList<ClinicBean>();
		for(Clinic clinic : clinicList){
			List<Clinic_Resource> clinicResourceList = Clinic_ResourceLocalServiceUtil.getClinicResources(clinic.getClinicId());
			List<ClientResourceBean> clinicResourceBeanList = new ArrayList<ClientResourceBean>();
			for(Clinic_Resource clinicResource : clinicResourceList){
				try {
					Resource resource = ResourceLocalServiceUtil.getResource(clinicResource.getResourceId());
					Specification specification = SpecificationLocalServiceUtil.getSpecification(clinicResource.getSpecificationId());
					ClientResourceBean clinicResourceBean = new ClientResourceBean(clinic,resource,specification, clinicResource);
					clinicResourceBeanList.add(clinicResourceBean);
					
				} catch (PortalException e) {
					_log.error(e.getMessage(), e);
				}
			}
			
			ClinicBean cliniBean = new ClinicBean(clinic, clinicResourceBeanList);
			clinicBeanList.add(cliniBean);
		}
		renderRequest.setAttribute("clinicBeanList", clinicBeanList);
 		include(viewTemplate, renderRequest, renderResponse);
	}
	
}