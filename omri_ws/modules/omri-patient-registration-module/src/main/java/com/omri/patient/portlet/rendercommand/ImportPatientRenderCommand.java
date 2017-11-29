package com.omri.patient.portlet.rendercommand;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.model.Patient;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/import-patient"
	    },
	    service = MVCRenderCommand.class
)
public class ImportPatientRenderCommand implements MVCRenderCommand{

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		boolean isImported = ParamUtil.getBoolean(renderRequest, "isImported");
		if(isImported){
			PortalCache portalCache =   MultiVMPoolUtil.getCache(Patient.class.getName());
			List<String> unsuccessfullPatientList = (List<String>)portalCache.get("unsuccessfullPatientList");
			renderRequest.setAttribute("unsuccessfullPatientList", unsuccessfullPatientList);
			portalCache.remove("unsuccessfullPatientList");
			
			renderRequest.setAttribute("totalPatientCount", ParamUtil.getString(renderRequest, "totalPatientCount"));
			renderRequest.setAttribute("successImportedPatientCount", ParamUtil.getString(renderRequest, "successImportedPatientCount"));
			renderRequest.setAttribute("unsuccessImportedPatientCount", ParamUtil.getString(renderRequest, "UnsuccessImportedPatientCount"));
			renderRequest.setAttribute("isImported", isImported);
		}
		return "/patient/import_patients.jsp";
	}

}
