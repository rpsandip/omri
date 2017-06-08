package com.omri.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

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
		renderRequest.setAttribute("startYear", PortletContstant.START_YEAR);
		Calendar c = Calendar.getInstance();
	    int endYear = c.get(Calendar.YEAR);
	    renderRequest.setAttribute("endYear", endYear+PortletContstant.NEXT_YEARS_DELTA);
		include(viewTemplate, renderRequest, renderResponse);
	}
	
}