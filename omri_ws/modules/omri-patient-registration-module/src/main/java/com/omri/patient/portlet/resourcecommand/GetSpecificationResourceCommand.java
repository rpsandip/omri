package com.omri.patient.portlet.resourcecommand;

import java.io.IOException;
import java.util.ArrayList;
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
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/getSpecificationList"
	    },
	    service = MVCResourceCommand.class
	)
public class GetSpecificationResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(GetSpecificationResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		long resourceId = ParamUtil.getLong(resourceRequest, "resourceId");
		long clinicId = ParamUtil.getLong(resourceRequest, "clinicId");
		_log.info("resourcId ->" + resourceId);
		List<Clinic_Resource> clinicResourceList = Clinic_ResourceLocalServiceUtil.getClinicResources(clinicId, resourceId);
		JSONArray specificationJsonArray = JSONFactoryUtil.createJSONArray();
		for(Clinic_Resource clinicResource : clinicResourceList){
			JSONObject specificationJsonObject = JSONFactoryUtil.createJSONObject();
			Specification specification;
			try {
				specification = SpecificationLocalServiceUtil.getSpecification(clinicResource.getSpecificationId());
				specificationJsonObject.put("specificationId", specification.getSpecificationId());
				specificationJsonObject.put("specificationName", specification.getSpecificationName());
				specificationJsonArray.put(specificationJsonObject);
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
		}
		try {
			resourceResponse.getWriter().write(specificationJsonArray.toString());
		} catch (IOException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}

}
