package com.omri.portlet.resourcecommand;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.persistence.Patient_ClinicPK;
import com.omri.service.common.util.PatientDocumentStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet",
	        "mvc.command.name=verify-document"
	    },
	    service = MVCResourceCommand.class
	)
public class VerifyDocumentResourceCommand implements MVCResourceCommand{

	Log _log = LogFactoryUtil.getLog(VerifyDocumentResourceCommand.class.getName());
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {

		long patientId = ParamUtil.getLong(resourceRequest, "patientId");
		long clinicId = ParamUtil.getLong(resourceRequest, "clinicId");
		JSONObject responseObj = JSONFactoryUtil.createJSONObject();
		
		Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(clinicId, patientId);
		
		try {
			Patient_Clinic patientClinic = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
			patientClinic.setDocumentStatus(PatientDocumentStatus.APPROVED.getValue());
			Patient_ClinicLocalServiceUtil.updatePatient_Clinic(patientClinic);
			responseObj.put("status", "success");
		} catch (PortalException e) {
			responseObj.put("status", "error");
			_log.error(e);
		}
		
		
		try {
			resourceResponse.getWriter().write(responseObj.toString());
		} catch (IOException e) {
			_log.error(e);
		}
		return true;
	}

}
