package com.omri.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.beans.ProcedureBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.PatientDetailLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
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
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=omri-billing-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OmriBillingModulemvcportletPortlet extends MVCPortlet {
	
	private static Log _log = LogFactoryUtil.getLog(OmriBillingModulemvcportletPortlet.class);
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themdeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long clinicOrganizationId = getUserAssociatedLawFirmOrgId(themdeDisplay.getUserId()); 
		long clinicOrganizationgroupId = getUserAssociatedLawFirmGroupId(themdeDisplay.getUserId());
		List<ProcedureBean> procedureBeanList = new ArrayList<ProcedureBean>();
		try {
			Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrganizationId);
			
			boolean isBillingPerson=false;
			try {
				Role billingPersonRole = RoleLocalServiceUtil.getRole(themdeDisplay.getCompanyId(), "Billing Person");
				isBillingPerson = UserGroupRoleLocalServiceUtil.hasUserGroupRole(themdeDisplay.getUserId(), clinicOrganizationgroupId, billingPersonRole.getRoleId());
			} catch (PortalException e1) {
				_log.error(e1.getMessage(), e1);
			}
			if(isBillingPerson){
				procedureBeanList = getBillingPersonProcedureList(clinic.getClinicId());
			}
		} catch (NoSuchClinicException e) {
			_log.error(e.getMessage(), e);
		}
		renderRequest.setAttribute("procedureBeanList", procedureBeanList);
		include(viewTemplate, renderRequest, renderResponse);
	}
	
	private List<ProcedureBean> getBillingPersonProcedureList(long clinicId){
		List<ProcedureBean> procedureBeanList = new ArrayList<ProcedureBean>();
		List<Procedure> procedureList = ProcedureLocalServiceUtil.getClinicProcedureCompleted(clinicId);
		for(Procedure procedure : procedureList){
			ProcedureBean procedureBean = new ProcedureBean(procedure.getProcedureId());
			List<Appointment> procedureAppointmentList = AppointmentLocalServiceUtil.getAppointmentByProcedureId(procedure.getProcedureId());
			List<AppointmentBean> procedureAppointmentBeanList = new ArrayList<AppointmentBean>();
			for(Appointment appointment : procedureAppointmentList){
				try{
				Patient patient = PatientLocalServiceUtil.getPatient(appointment.getPatientId());
				Clinic clinic = ClinicLocalServiceUtil.getClinic(appointment.getClinicId());
				Resource resource = ResourceLocalServiceUtil.getResource(appointment.getResourceId());
				Specification specification = SpecificationLocalServiceUtil.getSpecification(appointment.getSpecificationId());
				AppointmentBean appointmentBean = new AppointmentBean(appointment, patient, clinic, resource, specification);
				procedureAppointmentBeanList.add(appointmentBean);
				}catch(PortalException e){
					_log.error(e.getMessage(), e);
				}
			}
			procedureBean.setAppointmentList(procedureAppointmentBeanList);
			procedureBeanList.add(procedureBean);
		}
		return procedureBeanList;
	}
	
	 public long getUserAssociatedLawFirmOrgId(long userId) {
		    List<Organization> userOrganizationList =
		        OrganizationLocalServiceUtil.getUserOrganizations(userId);
		    if (userOrganizationList.size() > 0) {
		      _log.debug(" UserId ->" + userId + " LawFirmOrgId ->"
		          + userOrganizationList.get(0).getOrganizationId());
		      return userOrganizationList.get(0).getOrganizationId();
		    }
		    _log.debug(" UserId ->" + userId + " LawFirmOrgId ->" + 0l);
		    return 0l;
	 }
	 
	 public long getUserAssociatedLawFirmGroupId(long userId) {
		    List<Organization> userOrganizationList =
		        OrganizationLocalServiceUtil.getUserOrganizations(userId);
		    if (userOrganizationList.size() > 0) {
		      _log.debug(" UserId ->" + userId + " LawFirmOrgId ->"
		          + userOrganizationList.get(0).getGroupId());
		      return userOrganizationList.get(0).getGroupId();
		    }
		    _log.debug(" UserId ->" + userId + " LawFirmOrgId ->" + 0l);
		    return 0l;
	 }
}