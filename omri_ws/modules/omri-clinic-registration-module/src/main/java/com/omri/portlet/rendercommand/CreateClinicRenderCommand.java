package com.omri.portlet.rendercommand;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.omri.service.common.model.Resource;
import com.omri.service.common.service.ResourceLocalServiceUtil;

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
		renderRequest.setAttribute("resourceList", getResourceList());
		return "/clinic/create_clinic.jsp";
	}

	private List<Resource> getResourceList(){
		List<Resource> resourceList = new ArrayList<Resource>();
		resourceList = ResourceLocalServiceUtil.getResources(-1,-1);
		return resourceList;
	}
}
