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
import com.omri.service.common.model.Appointment;
import com.omri.service.common.service.AppointmentLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriAppointmentPortletmvcportletPortlet",
	        "mvc.command.name=/sublitTechnologistReport"
	    },
	    service = MVCRenderCommand.class
)
public class SubmitTechnologistRenderCommand implements MVCRenderCommand{
	private static Log _log = LogFactoryUtil.getLog(SubmitTechnologistRenderCommand.class.getName());
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		long appointmentId = ParamUtil.getLong(renderRequest, "appointmentId");
		try {
			Appointment appointment = AppointmentLocalServiceUtil.getAppointment(appointmentId);
			renderRequest.setAttribute("appointment", appointment);
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return "/sublit_technologist_report.jsp";
	}

}
