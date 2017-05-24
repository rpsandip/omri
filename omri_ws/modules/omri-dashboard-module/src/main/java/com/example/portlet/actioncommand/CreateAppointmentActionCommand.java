package com.example.portlet.actioncommand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.exception.NoSuchClinic_ResourceException;
import com.omri.service.common.exception.NoSuchProcedureException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
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
			long appointmentClinicId = ParamUtil.getLong(actionRequest, "appointmentClinic");
			String resourceSpeficiation = ParamUtil.getString(actionRequest, "resource");
			String appointmentDateStr = ParamUtil.getString(actionRequest,"appointmentDate");
			String appointmentTime = ParamUtil.getString(actionRequest,"appointmentTime");
			SimpleDateFormat date12Format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		    SimpleDateFormat date24Format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
			int appointmentProcessTime=30;
			Patient_Clinic newClinicPatient = null;
			Patient_Clinic patientClinic = null;
			try {
				Date appointmentDate = date12Format.parse(appointmentDateStr+StringPool.SPACE+appointmentTime);
				String[]resourceSpeficiationArray = resourceSpeficiation.split(StringPool.COMMA);
				Resource resource = ResourceLocalServiceUtil.getResource(Long.parseLong(resourceSpeficiationArray[0]));
				Specification specification = SpecificationLocalServiceUtil.getSpecification(Long.parseLong(resourceSpeficiationArray[1]));
				if(clinicId!=appointmentClinicId){
					// check patient has relation with appoinentmentclinicId
					
					try{
						Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(appointmentClinicId, patientId);
						newClinicPatient = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
					}catch(PortalException e){
						
					}
					if(Validator.isNull(newClinicPatient)){
						Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(clinicId, patientId);
						Patient_Clinic existingPatientClinic = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
						newClinicPatient = Patient_ClinicLocalServiceUtil.addPatient_Clinic(patientId, appointmentClinicId,
								existingPatientClinic.getDoctorId(), existingPatientClinic.getDoctorName(), existingPatientClinic.getDoctorPhoneNo(), existingPatientClinic.getPatient_status(), 
								themeDisplay.getUserId(), themeDisplay.getUserId());
					}
				}
				
				
				if(Validator.isNotNull(newClinicPatient)){
					patientClinic = newClinicPatient;
					clinicId = newClinicPatient.getClinicId();
				}else{
					Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(clinicId, patientId);
					patientClinic = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
				}
				
				Procedure procedure = ProcedureLocalServiceUtil.getProcedureByPatientIdAndClinicId(patientClinic.getPatientId(), patientClinic.getClinicId());
				//Patient_Clinic_ResourcePK patientClinicResourcePK = new Patient_Clinic_ResourcePK(clinicId, patientId, resource.getResourceId(), specification.getSpecificationId());
				//Patient_Clinic_Resource patientClinicResource = Patient_Clinic_ResourceLocalServiceUtil.getPatient_Clinic_Resource(patientClinicResourcePK);
				Clinic_ResourcePK clinicResourcePK = new Clinic_ResourcePK(clinicId, specification.getSpecificationId(), resource.getResourceId());
				Clinic_Resource clinicResource = Clinic_ResourceLocalServiceUtil.getClinic_Resource(clinicResourcePK);
				//appointmentProcessTime = patientClinicResource.getNoOfOccurance()*clinicResource.getOperationTime();
				appointmentProcessTime = clinicResource.getOperationTime();
				Appointment appointment = AppointmentLocalServiceUtil.createAppointment(patientId, clinicId,
						resource.getResourceId(), specification.getSpecificationId(), procedure.getProcedureId(), clinicResource.getPrice(),patientClinic.getDoctorId(),
						appointmentDate, appointmentProcessTime, Integer.parseInt(resourceSpeficiationArray[2]), themeDisplay.getUserId(), themeDisplay.getUserId(),
						new Date(), new Date());
				checkProcedureComplete(appointment);
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientClinic.getPatientId()));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(patientClinic.getClinicId()));
				SessionMessages.add(actionRequest, "appointment-created-successfully");
			} catch (ParseException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientClinic.getPatientId()));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(patientClinic.getClinicId()));
				SessionErrors.add(actionRequest, "error-create-appointment");
				_log.error(e.getMessage(), e);
			}catch (NumberFormatException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientClinic.getPatientId()));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(patientClinic.getClinicId()));
				SessionErrors.add(actionRequest, "error-create-appointment");
				_log.error(e.getMessage(), e);
			} catch (PortalException e) {
				actionResponse.setRenderParameter("mvcRenderCommandName", "/schedule_patient");
				actionResponse.setRenderParameter("patientId", String.valueOf(patientClinic.getPatientId()));
				actionResponse.setRenderParameter("clinicId",  String.valueOf(patientClinic.getClinicId()));
				if(e instanceof NoSuchClinic_ResourceException){
					SessionErrors.add(actionRequest, "clinic-resource-error");
				}else{
					SessionErrors.add(actionRequest, "error-create-appointment");	
				}
				_log.error(e.getMessage(), e);
			}
	}
	
	private boolean checkProcedureComplete(Appointment appointment){
		boolean isProcedureComplete=false;
		int totalResourceCount=0;
		List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(appointment.getPatientId(), appointment.getClinicId());
		for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
			totalResourceCount = totalResourceCount + patientClinicResource.getNoOfOccurance();
		}
		try {
			Procedure procedure = ProcedureLocalServiceUtil.getProcedureByPatientIdAndClinicId(appointment.getPatientId(), appointment.getClinicId());
			List<Appointment> procedureAppointment = AppointmentLocalServiceUtil.getAppointmentByProcedureId(procedure.getProcedureId());
			int remainingAppointmentToCreate = totalResourceCount=procedureAppointment.size();
			if(remainingAppointmentToCreate==0){
				procedure.setIsComplete(true);
				ProcedureLocalServiceUtil.updateProcedure(procedure);
			}
		} catch (NoSuchProcedureException e) {
			_log.error(e.getMessage(), e);
		}
		return isProcedureComplete;
	}

}
