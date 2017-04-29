package com.example.portlet.actioncommand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.example.portlet.OmriDashboardModulemvcportletPortlet;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.service.persistence.Clinic_ResourcePK;
import com.omri.service.common.service.persistence.Patient_ClinicPK;
import com.omri.service.common.service.persistence.Patient_Clinic_ResourcePK;


@Component(
	    property = {
	    	"javax.portlet.name=com_example_portlet_OmriDashboardModulemvcportletPortlet",
	        "mvc.command.name=/patient/add_appointment"
	    },
	    service = MVCActionCommand.class
	)
public class CreateAppointmentActionCommand  extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(CreateAppointmentActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
			ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long patientId = ParamUtil.getLong(actionRequest, "patientId");
			long clinicId = ParamUtil.getLong(actionRequest, "clinicId");
			String resourceSpeficiation = ParamUtil.getString(actionRequest, "resource");
			String appointmentDateStr = ParamUtil.getString(actionRequest,"appointmentDate");
			String appointmentTime = ParamUtil.getString(actionRequest,"appointmentTime");
			SimpleDateFormat date12Format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		    SimpleDateFormat date24Format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
			int appointmentProcessTime=30;
			try {
				Date appointmentDate = date24Format.parse(date24Format.format(date12Format.parse(appointmentDateStr+StringPool.SPACE+appointmentTime)));
				String[]resourceSpeficiationArray = resourceSpeficiation.split(StringPool.COMMA);
				Resource resource = ResourceLocalServiceUtil.getResource(Long.parseLong(resourceSpeficiationArray[0]));
				Specification specification = SpecificationLocalServiceUtil.getSpecification(Long.parseLong(resourceSpeficiationArray[1]));
				Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(clinicId, patientId);
				Patient_Clinic patientClinic = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
				Patient_Clinic_ResourcePK patientClinicResourcePK = new Patient_Clinic_ResourcePK(clinicId, patientId, resource.getResourceId(), specification.getSpecificationId());
				Patient_Clinic_Resource patientClinicResource = Patient_Clinic_ResourceLocalServiceUtil.getPatient_Clinic_Resource(patientClinicResourcePK);
				Clinic_ResourcePK clinicResourcePK = new Clinic_ResourcePK(clinicId, specification.getSpecificationId(), resource.getResourceId());
				Clinic_Resource clinicResource = Clinic_ResourceLocalServiceUtil.getClinic_Resource(clinicResourcePK);
				appointmentProcessTime = patientClinicResource.getNoOfOccurance()*clinicResource.getOperationTime();
				Appointment appointment = AppointmentLocalServiceUtil.createAppointment(patientId, clinicId,
						resource.getResourceId(), specification.getSpecificationId(), patientClinic.getDoctorId(),
						appointmentDate, appointmentProcessTime, Integer.parseInt(resourceSpeficiationArray[2]), themeDisplay.getUserId(), themeDisplay.getUserId(),
						new Date(), new Date());
				
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientId));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(clinicId));
				SessionMessages.add(actionRequest, "appointment-created-successfully");
			} catch (ParseException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientId));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(clinicId));
				SessionErrors.add(actionRequest, "error-create-appointment");
				_log.error(e.getMessage(), e);
			}catch (NumberFormatException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientId));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(clinicId));
				SessionErrors.add(actionRequest, "error-create-appointment");
				_log.error(e.getMessage(), e);
			} catch (PortalException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientId));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(clinicId));
				SessionErrors.add(actionRequest, "error-create-appointment");
				_log.error(e.getMessage(), e);
			}
	}

}
