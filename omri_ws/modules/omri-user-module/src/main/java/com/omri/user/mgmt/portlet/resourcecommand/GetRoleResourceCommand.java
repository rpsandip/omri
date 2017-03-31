package com.omri.user.mgmt.portlet.resourcecommand;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.user.mgmt.portlet.util.UserModuleContstant;


@Component(
	    property = {
	    	"javax.portlet.name=" + UserModuleContstant.PORTLET_ID,
	        "mvc.command.name=/getRoles"
	    },
	    service = MVCResourceCommand.class
	)
public class GetRoleResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(SearchUserResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String cmd = ParamUtil.getString(resourceRequest, "adminType");
		if(cmd.equals("lawyer")){
			Role lawyerAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer Admin");
			Role receptionistRole = getRoleWithName(themeDisplay.getCompanyId(),"Receptionist");
			Role caseManagerRole = getRoleWithName(themeDisplay.getCompanyId(),"Case Manager");
			Role attorneyRole = getRoleWithName(themeDisplay.getCompanyId(),"Attorney");
			JSONArray roleJsonArray = JSONFactoryUtil.createJSONArray();
			JSONObject roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", lawyerAdminRole.getRoleId());
			roleJsonObject.put("name", lawyerAdminRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", receptionistRole.getRoleId());
			roleJsonObject.put("name", receptionistRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", caseManagerRole.getRoleId());
			roleJsonObject.put("name", caseManagerRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", attorneyRole.getRoleId());
			roleJsonObject.put("name", attorneyRole.getName());
			roleJsonArray.put(roleJsonObject);
			
			try {
				resourceResponse.getWriter().write(roleJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
		}else if(cmd.equals("doctor")){
			Role doctorAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor Admin");
			Role receptionistRole = getRoleWithName(themeDisplay.getCompanyId(),"Receptionist");
			Role medicalAssistanceRole = getRoleWithName(themeDisplay.getCompanyId(),"Medical Assistants");
			Role nurseRole = getRoleWithName(themeDisplay.getCompanyId(),"Nurse");
			
			JSONArray roleJsonArray = JSONFactoryUtil.createJSONArray();
			JSONObject roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", doctorAdminRole.getRoleId());
			roleJsonObject.put("name", doctorAdminRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", receptionistRole.getRoleId());
			roleJsonObject.put("name", receptionistRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", medicalAssistanceRole.getRoleId());
			roleJsonObject.put("name", medicalAssistanceRole.getName());
			roleJsonArray.put(roleJsonObject);
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", nurseRole.getRoleId());
			roleJsonObject.put("name", nurseRole.getName());
			roleJsonArray.put(roleJsonObject);
			
			try {
				resourceResponse.getWriter().write(roleJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
		}else if(cmd.equals("clinic")){
			Role clinicAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Clinic Admin");
			Role technologistRole = getRoleWithName(themeDisplay.getCompanyId(),"Technologist");
			Role billingPersonRole = getRoleWithName(themeDisplay.getCompanyId(),"Billing Person");
			JSONArray roleJsonArray = JSONFactoryUtil.createJSONArray();
			JSONObject roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", clinicAdminRole.getRoleId());
			roleJsonObject.put("name", clinicAdminRole.getName());
			roleJsonArray.put(roleJsonObject);
			
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", technologistRole.getRoleId());
			roleJsonObject.put("name", technologistRole.getName());
			roleJsonArray.put(roleJsonObject);
			
			roleJsonObject = JSONFactoryUtil.createJSONObject();
			roleJsonObject.put("roleId", billingPersonRole.getRoleId());
			roleJsonObject.put("name", billingPersonRole.getName());
			roleJsonArray.put(roleJsonObject);
			
			try {
				resourceResponse.getWriter().write(roleJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
			
		}
		return true;
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
