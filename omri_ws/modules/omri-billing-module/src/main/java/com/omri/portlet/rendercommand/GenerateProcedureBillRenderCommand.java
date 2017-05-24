package com.omri.portlet.rendercommand;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.AppointmentBean;
import com.omri.service.common.beans.ProcedureBean;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriBillingModulemvcportletPortlet",
	        "mvc.command.name=/generateProcedureBill"
	    },
	    service = MVCRenderCommand.class
)
public class GenerateProcedureBillRenderCommand implements MVCRenderCommand{
	private static Log _log = LogFactoryUtil.getLog(GenerateProcedureBillRenderCommand.class.getName());
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		long procedureId = ParamUtil.getLong(renderRequest, "procedureId");
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Procedure procedure;
		double totalPrice =0;
		try {
			procedure = ProcedureLocalServiceUtil.getProcedure(procedureId);
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
				totalPrice = totalPrice+appointmentBean.getPrice();
				}catch(PortalException e){
					_log.error(e.getMessage(), e);
				}
			}
			procedureBean.setAppointmentList(procedureAppointmentBeanList);
			renderRequest.setAttribute("procedureBean", procedureBean);
			renderRequest.setAttribute("procedureId", procedureId);
			renderRequest.setAttribute("totalAmount", totalPrice);
			
			// Get doctor admin list
			try{
				Role doctorAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor Admin");
				Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Doctor");
				List<User>doctorAdminList= new ArrayList<User>();
				List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(doctorOrg.getGroupId(), doctorAdminRole.getRoleId());
				for(UserGroupRole userGroupRole : userGroupRoleList){
					User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
					doctorAdminList.add(user);
				}
				renderRequest.setAttribute("doctorAdminList", doctorAdminList);
				}catch(PortalException e){
					_log.error(e.getMessage(), e);
				}
			
			// Get lawyer admin list
				try{
					Role lawyerAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer Admin");
					Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
					List<User>lawyerAdminList= new ArrayList<User>();
					List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(lawyerOrg.getGroupId(), lawyerAdminRole.getRoleId());
					for(UserGroupRole userGroupRole : userGroupRoleList){
						User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
						lawyerAdminList.add(user);
					}
					renderRequest.setAttribute("lawyerAdminList", lawyerAdminList);
					}catch(PortalException e){
						_log.error(e.getMessage(), e);
					}
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		
		return "/genrate_procedure_bill.jsp";
	}
	
	private Role getRoleWithName(long companyId, String roleName){
		Role role = null;
		try {
			role = RoleLocalServiceUtil.getRole(companyId,roleName);
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return role;
	}

}
