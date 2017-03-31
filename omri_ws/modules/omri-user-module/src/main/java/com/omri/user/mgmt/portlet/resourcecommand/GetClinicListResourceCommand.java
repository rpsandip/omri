package com.omri.user.mgmt.portlet.resourcecommand;

import java.io.IOException;
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
import com.omri.service.common.model.Clinic;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.user.mgmt.portlet.util.UserModuleContstant;

@Component(
	    property = {
	    	"javax.portlet.name=" + UserModuleContstant.PORTLET_ID,
	        "mvc.command.name=/getClinicList"
	    },
	    service = MVCResourceCommand.class
	)
public class GetClinicListResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(GetClinicListResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		JSONArray clincJsonArray = JSONFactoryUtil.createJSONArray();
		try {
			List<Clinic> clinicList = ClinicLocalServiceUtil.getClinics(-1, -1);
			for(Clinic clinic : clinicList){
				JSONObject clinicJsonObject = JSONFactoryUtil.createJSONObject();
				clinicJsonObject.put("clinicId", clinic.getClinicId());
				clinicJsonObject.put("clinicOrgId", clinic.getClinicorganizationId());
				clinicJsonObject.put("clinicOrgGroupId", clinic.getClinicorganizationGroupId());
				clinicJsonObject.put("clinicName", clinic.getClinicName());
				clincJsonArray.put(clinicJsonObject);
			}
			resourceResponse.getWriter().write(clincJsonArray.toString());
		} catch (IOException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}

}
