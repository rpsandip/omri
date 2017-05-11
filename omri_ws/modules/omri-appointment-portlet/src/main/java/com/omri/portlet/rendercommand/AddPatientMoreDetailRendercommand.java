package com.omri.portlet.rendercommand;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.portlet.actioncommand.ScheduleForTechnogolistActionCommand;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.PatientDetail;
import com.omri.service.common.service.PatientDetailLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriAppointmentPortletmvcportletPortlet",
	        "mvc.command.name=/add_patient_more_detail"
	    },
	    service = MVCRenderCommand.class
)
public class AddPatientMoreDetailRendercommand implements MVCRenderCommand {
	private static Log _log = LogFactoryUtil.getLog(AddPatientMoreDetailRendercommand.class);
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		long patientId = ParamUtil.getLong(renderRequest, "patientId");
		try {
			Patient patient = PatientLocalServiceUtil.getPatient(patientId);
			renderRequest.setAttribute("patient", patient);
			try{
				PatientDetail patientDetail = PatientDetailLocalServiceUtil.getPatientDetail(patientId);
				renderRequest.setAttribute("patientDetail", patientDetail);
			}catch(PortalException e){
			}
		} catch (PortalException e) {
			_log.error(e.getMessage(),e);
		}
		return "/add_patient_more_detail.jsp";
	}

}
