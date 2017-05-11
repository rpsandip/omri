package com.omri.portlet.actioncommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.util.AppointmentStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriAppointmentPortletmvcportletPortlet",
	        "mvc.command.name=/scheduleForTechnologist"
	    },
	    service = MVCActionCommand.class
	)
public class ScheduleForTechnogolistActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(ScheduleForTechnogolistActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		long appointmentId = ParamUtil.getLong(actionRequest,"appointmentId" );
		try {
			Appointment appointment = AppointmentLocalServiceUtil.getAppointment(appointmentId);
			appointment.setStatus(AppointmentStatus.IN_RESOURCE_PROCESS.getValue());
			AppointmentLocalServiceUtil.updateAppointment(appointment);
			SessionMessages.add(actionRequest, "patient-scheduled-techonologist-succesfully");
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		
	}

}
