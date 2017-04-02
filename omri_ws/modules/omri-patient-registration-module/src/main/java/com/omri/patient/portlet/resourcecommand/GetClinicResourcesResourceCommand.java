package com.omri.patient.portlet.resourcecommand;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/getClinicResources"
	    },
	    service = MVCResourceCommand.class
	)
public class GetClinicResourcesResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(GetClinicResourcesResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		long clinicId  = ParamUtil.getLong(resourceRequest, "clinicId");
		List<Clinic_Resource> clinicResourcesList = Clinic_ResourceLocalServiceUtil.getClinicResources(clinicId);
		JSONArray clinicResourcesJsonArray = JSONFactoryUtil.createJSONArray();
		for(Clinic_Resource clinicResource : clinicResourcesList){
			JSONObject clinicResourceJsonObject = JSONFactoryUtil.createJSONObject();
			String resourceName= getResourceName(clinicResource.getResourceId());
			if(Validator.isNotNull(resourceName)){
				clinicResourceJsonObject.put("resourceId", clinicResource.getResourceId());
				clinicResourceJsonObject.put("resourceName", resourceName);
				clinicResourcesJsonArray.put(clinicResourceJsonObject);
			}
		}
		try {
			resourceResponse.getWriter().write(clinicResourcesJsonArray.toString());
		} catch (IOException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}

	private String getResourceName(long resourceId){
		try {
			Resource resource = com.omri.service.common.service.ResourceLocalServiceUtil.getResource(resourceId);
			return resource.getResourceName();
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return StringPool.BLANK;
	}
}
