package com.omri.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.StringUtil;
import com.omri.service.common.model.Resource;
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
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		renderRequest.setAttribute("resourceList", getResourceList());
		include(viewTemplate, renderRequest, renderResponse);
	}
	
	private List<Resource> getResourceList(){
		List<Resource> resourceList = new ArrayList<Resource>();
		resourceList = ResourceLocalServiceUtil.getResources(-1,-1);
		return resourceList;
	}
}