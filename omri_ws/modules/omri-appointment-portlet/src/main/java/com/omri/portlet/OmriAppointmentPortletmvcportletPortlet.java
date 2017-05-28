package com.omri.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.util.AppointmentStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=omri-appointment-portlet Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"com.liferay.portlet.action-url-redirect=true",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriAppointmentPortletmvcportletPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(OmriAppointmentPortletmvcportletPortlet.class);
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long clinicOrganizationId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themdeDisplay.getUserId());
		long clinicOrganizationgroupId =  OMRICommonLocalServiceUtil.getOrganizationGroupId(clinicOrganizationId);
		PermissionChecker permuissionChecker = themdeDisplay.getPermissionChecker();
		boolean hasPatientAddMoreDetailPermission =  permuissionChecker.hasPermission(clinicOrganizationgroupId, Appointment.class.getName(), Appointment.class.getName(), "ADD_PATIENT_MORE_DETAIL");
		boolean hasSheduleForTechnologistPermission =  permuissionChecker.hasPermission(clinicOrganizationgroupId, Appointment.class.getName(), Appointment.class.getName(), "SCHEDULE_FOR_TECHNOLOGIST");
		boolean hasSubmitTechnologistReportPermission =  permuissionChecker.hasPermission(clinicOrganizationgroupId, Appointment.class.getName(), Appointment.class.getName(), "SUBMIT_TECHNOLOGIST_REPORT");
		
		renderRequest.setAttribute("hasPatientAddMoreDetailPermission", hasPatientAddMoreDetailPermission);
		renderRequest.setAttribute("hasSheduleForTechnologistPermission", hasSheduleForTechnologistPermission);
		renderRequest.setAttribute("hasSubmitTechnologistReportPermission", hasSubmitTechnologistReportPermission);
		
		
		List<AppointmentBean> appointmentBeanList = new ArrayList<AppointmentBean>();
		boolean isTechonlogist=false;
		try {
			Role technologistRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Technologist");
			isTechonlogist = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), clinicOrganizationgroupId, technologistRole.getRoleId());
		} catch (PortalException e1) {
			_log.error(e1.getMessage(), e1);
		}
		
		if(isTechonlogist){
			appointmentBeanList = getTechologistAppointmentList(clinicOrganizationId);
		}else{
		
		if(clinicOrganizationId!=0){
			try {
				Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
				List<Appointment> clinicAppointmentList = AppointmentLocalServiceUtil.getPatientAppointmentsByClinicId(clinic.getClinicId());
				for(Appointment appointment : clinicAppointmentList){
					try {
						Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
						Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
						Patient patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
						AppointmentBean appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
						appointmentBeanList.add(appointmentBean);
					} catch (PortalException e) {
						_log.error(e.getMessage());
					}
				}
			} catch (NoSuchClinicException e) {
				_log.error(e.getMessage(), e);
			}
		}
		Collections.sort(appointmentBeanList, new Comparator<AppointmentBean>() {

	        public int compare(AppointmentBean o1, AppointmentBean o2) {
	            return o1.getModifiedDate().compareTo(o1.getModifiedDate());
	        }
	    });
		}
		renderRequest.setAttribute("appointmentList", appointmentBeanList);
		include(viewTemplate, renderRequest, renderResponse);
	}
	
	private List<AppointmentBean> getTechologistAppointmentList(long clinicOrganizationId){
		List<AppointmentBean> appointmentBeanList = new ArrayList<AppointmentBean>();
		try {
			Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
			List<Appointment> technologistAppointment = AppointmentLocalServiceUtil.getClinicAppointmentListBystatus(clinic.getClinicId(), AppointmentStatus.IN_RESOURCE_PROCESS.getValue());
			for(Appointment appointment : technologistAppointment){
				try {
					Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
					Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
					Patient patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
					AppointmentBean appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
					appointmentBeanList.add(appointmentBean);
				} catch (PortalException e) {
					_log.error(e.getMessage());
				}
			}
		} catch (NoSuchClinicException e) {
			_log.error(e.getMessage(), e);
		}
		return appointmentBeanList;
	}
}