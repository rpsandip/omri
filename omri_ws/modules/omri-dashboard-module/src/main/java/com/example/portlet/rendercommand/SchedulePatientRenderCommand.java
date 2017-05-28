package com.example.portlet.rendercommand;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.service.persistence.Patient_ClinicPK;

@Component(
	    property = {
	    	"javax.portlet.name=com_example_portlet_OmriDashboardModulemvcportletPortlet",
	        "mvc.command.name=/schedule_patient"
	    },
	    service = MVCRenderCommand.class
)
public class SchedulePatientRenderCommand implements MVCRenderCommand{

	private static Log _log = LogFactoryUtil.getLog(SchedulePatientRenderCommand.class);
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		long patientId = ParamUtil.getLong(renderRequest, "patientId");
		long clinicId = ParamUtil.getLong(renderRequest, "clinicId");
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long organizationId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themdeDisplay.getUserId());
		long orgGroupId = OMRICommonLocalServiceUtil.getOrganizationGroupId(organizationId);
		try {
			Patient patient = PatientLocalServiceUtil.getPatient(patientId);
			Clinic clinic = ClinicLocalServiceUtil.getClinic(clinicId);
			List<AppointmentBean> patientAppointmentList = getPatientAppointments(patient);
			Patient_ClinicPK patientClinicPK = new Patient_ClinicPK(clinicId, patientId);
			Patient_Clinic patientClinc = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
			List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			if(Validator.isNotNull(patientClinc)){
				List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patientId, patientClinc.getClinicId());
				for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
					try {
						Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
						Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
						PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
						int appointmentCreated = checkAppointmentExist(patientResourceBean);
						int remainingAppointment = patientResourceBean.getRemainingOccurance()-appointmentCreated;
						if(remainingAppointment<0){
							remainingAppointment=0;
						}
						patientResourceBean.setRemainingOccurance(remainingAppointment);
						patientResourceBeanList.add(patientResourceBean);
					} catch (PortalException e) {
						_log.error(e.getMessage(), e);
					}
				}
			}
			PatientBean patientBean= new PatientBean(patient, patientClinc,patientResourceBeanList);
			renderRequest.setAttribute("patientBean", patientBean);
			renderRequest.setAttribute("patientAppointmentList", patientAppointmentList);
			
			// Get clinic list
			List<Clinic> clinicList = new ArrayList<Clinic>();
			
			boolean isClinicAdmin = false;
			try {
				Role clinicAdminRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Clinic Admin");
				isClinicAdmin = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), orgGroupId, clinicAdminRole.getRoleId());
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
			if(isClinicAdmin){
					clinicList.add(clinic);
			}else{
				clinicList = ClinicLocalServiceUtil.getClinics(-1,-1);
			}
			
			renderRequest.setAttribute("clinicList", clinicList);
			
			// Get reosurceList
			List<Resource> resourceList = ResourceLocalServiceUtil.getResources(-1,-1);
			
			renderRequest.setAttribute("resourceList", resourceList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		return "/patient_schedule.jsp";
	}
	
    private int checkAppointmentExist(PatientResourceBean patientResourceBean){
    	List<Appointment> appointmentExistList = AppointmentLocalServiceUtil.getAppointmentByPatientIdResourceIdSpecificationId
    			(patientResourceBean.getPatientId(), patientResourceBean.getResourceId(), patientResourceBean.getSpecificationId());
    	return appointmentExistList.size();
    }
	
	private List<AppointmentBean> getPatientAppointments(Patient patient){
		List<Appointment> patientAppointmentList= new ArrayList<Appointment>();
		List<AppointmentBean> patientAppointmentBeanList= new ArrayList<AppointmentBean>();
		patientAppointmentList = AppointmentLocalServiceUtil.getPatientAppointmentsByPatientId(patient.getPatientId());  //getPatientAppointments(patient.getPatientId(), clinic.getClinicId());
		for(Appointment appointment : patientAppointmentList){
			try {
				Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
				Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
				Clinic clinic = ClinicLocalServiceUtil.getClinic(appointment.getClinicId());
				AppointmentBean appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
				patientAppointmentBeanList.add(appointmentBean);
			} catch (PortalException e) {
				_log.error(e.getMessage());
			}
 		}
		return patientAppointmentBeanList;
	}
}
