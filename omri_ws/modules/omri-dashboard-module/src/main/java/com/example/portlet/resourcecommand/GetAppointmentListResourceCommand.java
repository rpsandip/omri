package com.example.portlet.resourcecommand;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.example.portlet.rendercommand.SchedulePatientRenderCommand;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Resource;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_example_portlet_OmriDashboardModulemvcportletPortlet",
	        "mvc.command.name=/getAppointmentList"
	    },
	    service = MVCResourceCommand.class
	)
public class GetAppointmentListResourceCommand  implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(GetAppointmentListResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		long patientId = ParamUtil.getLong(resourceRequest,"patientId");
		long clinicId = ParamUtil.getLong(resourceRequest, "clinicId");
		long resourceId = ParamUtil.getLong(resourceRequest, "resourceId");
		String filterDate = ParamUtil.getString(resourceRequest, "filterDate");
		JSONArray appointmentJsonArray = JSONFactoryUtil.createJSONArray();
		List<Appointment> appointmentList = AppointmentLocalServiceUtil.getPatientAppointmentsByFiterData(clinicId, resourceId, filterDate);
		for(Appointment appointment : appointmentList){
			JSONObject appointmentJsonObj = JSONFactoryUtil.createJSONObject();
			
			try{
			Patient patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
			Resource resource = com.omri.service.common.service.ResourceLocalServiceUtil.getResource(appointment.getResourceId());
			appointmentJsonObj.put("title", patient.getFirstName()+StringPool.SPACE+patient.getLastName()+ StringPool.DASH+ resource.getResourceName());
			appointmentJsonObj.put("start",appointment.getAppointmetDate());
			appointmentJsonObj.put("end",appointment.getAppointmetDate().getTime()+ 1 * (3600*1000));
			appointmentJsonObj.put("allDay", false);
			appointmentJsonArray.put(appointmentJsonObj);
			}catch(PortalException e){
				_log.error(e.getMessage(), e);
			}
		}
		try {
			resourceResponse.getWriter().write(appointmentJsonArray.toString());
		} catch (IOException e) {
			_log.error(e.getMessage(), e);
		}
		return true;
	}

}
