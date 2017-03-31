package com.omri.portlet.resourcecommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.Resource_SpecificationLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriClinicRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/getSpecificationList"
	    },
	    service = MVCResourceCommand.class
	)
public class GetSpecificationResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(GetSpecificationResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		List<Specification> specificationList = new ArrayList<Specification>();
		long resourceId = ParamUtil.getLong(resourceRequest, "resourceId");
		_log.info("resourcId ->" + resourceId);
		specificationList = Resource_SpecificationLocalServiceUtil.getSpecificationOfResource(resourceId);
		JSONArray specificationJsonArray = JSONFactoryUtil.createJSONArray();
		for(Specification specification : specificationList){
			JSONObject specificationJsonObject = JSONFactoryUtil.createJSONObject();
			specificationJsonObject.put("specificationId", specification.getSpecificationId());
			specificationJsonObject.put("specificationName", specification.getSpecificationName());
			specificationJsonArray.put(specificationJsonObject);
		}
		try {
			resourceResponse.getWriter().write(specificationJsonArray.toString());
		} catch (IOException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}

}
