package com.omri.portlet.rendercommand;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriClinicRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/create-clinic"
	    },
	    service = MVCRenderCommand.class
)
public class CreateClinicRenderCommand implements MVCRenderCommand{

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		return "/clinic/create_clinic.jsp";
	}

}
