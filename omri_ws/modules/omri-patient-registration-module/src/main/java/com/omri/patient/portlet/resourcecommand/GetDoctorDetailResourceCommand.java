package com.omri.patient.portlet.resourcecommand;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.CustomUserLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/getDoctorDetail"
	    },
	    service = MVCResourceCommand.class
	)
public class GetDoctorDetailResourceCommand implements MVCResourceCommand{
	private static Log LOG = LogFactoryUtil.getLog(GetDoctorDetailResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		long userId = ParamUtil.getLong(resourceRequest, "userId");
		JSONObject jsoObj = JSONFactoryUtil.createJSONObject();
		try {
			CustomUser customUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(userId);
			
			jsoObj.put("phone", customUser.getPhone());
			jsoObj.put("fax", customUser.getFax());
		} catch (NoSuchCustomUserException e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			resourceResponse.getWriter().write(jsoObj.toString());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return true;
	}

}
