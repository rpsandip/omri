package com.omri.user.mgmt.portlet.resourcecommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.omri.user.mgmt.portlet.actioncommand.CreateUserActionCommand;
import com.omri.user.mgmt.portlet.util.UserModuleContstant;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.util.comparator.UserFirstNameComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

@Component(
	    property = {
	    	"javax.portlet.name=" + UserModuleContstant.PORTLET_ID,
	        "mvc.command.name=/search_user"
	    },
	    service = MVCResourceCommand.class
	)
public class SearchUserResourceCommand implements MVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(SearchUserResourceCommand.class);
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		JSONArray userJsonArray = JSONFactoryUtil.createJSONArray();
		String cmd = ParamUtil.getString(resourceRequest, "adminType");
		if(cmd.equals("lawyer")){
			List<User> lawyerAdminUserList = getUsesListOfRole(resourceRequest, "Lawyer Admin");
			for(User user : lawyerAdminUserList){
				JSONObject userJsonObject = JSONFactoryUtil.createJSONObject();
				userJsonObject.put("userId",user.getUserId());
				userJsonObject.put("email",user.getEmailAddress());
				userJsonObject.put("firstName",user.getFirstName());
				userJsonArray.put(userJsonObject);
			}
			try {
				resourceResponse.getWriter().write(userJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
		}else if(cmd.equals("doctor")){
			List<User> lawyerAdminUserList = getUsesListOfRole(resourceRequest, "Doctor Admin");
			for(User user : lawyerAdminUserList){
				JSONObject userJsonObject = JSONFactoryUtil.createJSONObject();
				userJsonObject.put("userId",user.getUserId());
				userJsonObject.put("email",user.getEmailAddress());
				userJsonObject.put("firstName",user.getFirstName());
				userJsonArray.put(userJsonObject);
			}
			try {
				resourceResponse.getWriter().write(userJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
		}else if(cmd.equals("clinic")){
			List<User> clinicAdminList = getUsesListOfRole(resourceRequest, "Clinic Admin");
			for(User user : clinicAdminList){
				JSONObject userJsonObject = JSONFactoryUtil.createJSONObject();
				userJsonObject.put("userId",user.getUserId());
				userJsonObject.put("email",user.getEmailAddress());
				userJsonObject.put("firstName",user.getFirstName());
				userJsonArray.put(userJsonObject);
			}
			try {
				resourceResponse.getWriter().write(userJsonArray.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
			}
		}
		return true;
	}
	
	private List<User> getUsesListOfRole(ResourceRequest resourceRequest, String roleName){ 
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<User> userList = new ArrayList<User>();
	   try{
		   Organization lawyerORg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
		   Role laywerAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), roleName);
		   List<UserGroupRole> userGroupRoleList=  UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(lawyerORg.getGroupId(), laywerAdminRole.getRoleId());
		   for(UserGroupRole userGroupRole: userGroupRoleList){
			   User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
			   userList.add(user); 
		   }
		   _log.info("userList ->" + userList.size());
	   } catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
	   return userList;
	}
}
