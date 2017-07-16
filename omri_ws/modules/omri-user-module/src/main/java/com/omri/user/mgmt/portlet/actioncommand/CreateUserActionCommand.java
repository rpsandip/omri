package com.omri.user.mgmt.portlet.actioncommand;

import java.util.Date;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.ParamAncestorTag;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.user.mgmt.portlet.rendercommand.CreateUserRenderCommand;
import com.omri.user.mgmt.portlet.util.UserModuleContstant;

@Component(
	    immediate = true,
	    property = {
	        "javax.portlet.name=" + UserModuleContstant.PORTLET_ID,
	        "mvc.command.name=/user/create_user"
	    },
	    service = MVCActionCommand.class
	)
public class CreateUserActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(CreateUserActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse)  {
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		User user = null;
		CustomUser customUser = null;
		try {
			// Create LR User
			user = createLRUser(actionRequest, actionResponse);
			
			
			// Find Organization which associate with User
			long parentUserId = getParentUserId(actionRequest, actionResponse);
			
			long[] organizationIds = new long[1];
			long organizationId = getOrganizationIdForCreatingUser(actionRequest, actionResponse);
			if(organizationId>0){
				 organizationIds[0] = organizationId;
			}
			long organizationGroupId = getOrganizationGroupIdFromOrgId(organizationId);
			
			//Associate Liferay default site to user
			Group omriDefaultSite = GroupLocalServiceUtil.getFriendlyURLGroup(themeDisplay.getCompanyId(),"/omri");
			GroupLocalServiceUtil.addUserGroup(user.getUserId(), omriDefaultSite.getGroupId());
			
			
			// Create Custom User
			customUser = createCustomUser(actionRequest, actionResponse, user, parentUserId, organizationId, organizationGroupId);
			
			// Assing Organization Role to User
			long[] roleIds = ParamUtil.getLongValues(actionRequest, "role");
			if(organizationIds.length>0){
				 UserGroupRoleLocalServiceUtil.addUserGroupRoles(user.getUserId(), organizationGroupId, roleIds);
				 UserGroupRoleLocalServiceUtil.addUserGroupRoles(user.getUserId(), omriDefaultSite.getGroupId(), roleIds);
			}
			
			// Set Clinic admin if selected entity is clinic
			String entity = ParamUtil.getString(actionRequest, "entity");
			if(entity.equals("clinic")){
				Role clinicAdminRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Clinic Admin");
				for(int i=0;i<roleIds.length;i++){
					if(roleIds[i]==clinicAdminRole.getRoleId()){
						// creating clinic admin user so check selected clinci has any clinic admin or not. If not then
						// add this user as Clinic admin for selected clinic
						long clinicId = ParamUtil.getLong(actionRequest, "clinic");
						Clinic clinic = ClinicLocalServiceUtil.getClinic(clinicId);
						if(clinic.getClinicAdminId()==0){
							clinic.setClinicAdminId(user.getUserId());
							ClinicLocalServiceUtil.updateClinic(clinic);
							
							// Give Document uploader role to clinic admin
							Role documentUploaderRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Document Uploader");
							RoleLocalServiceUtil.addUserRole(user.getUserId(), documentUploaderRole.getRoleId());
						}else{
							throw new PortalException("Clinic has already clinic admin.");
						}
						
					}
				}
			}else if(entity.equals("lawyer")){
				// Give Document uploader role  lawyer entity
				Role documentUploaderRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Document Uploader");
				RoleLocalServiceUtil.addUserRole(user.getUserId(), documentUploaderRole.getRoleId());
			}else if(entity.equals("doctor")){
				// Give Document uploader role to doctor entity
				Role documentUploaderRole = RoleLocalServiceUtil.getRole(themeDisplay.getCompanyId(), "Document Uploader");
				RoleLocalServiceUtil.addUserRole(user.getUserId(), documentUploaderRole.getRoleId());
			}
			
			SessionMessages.add(actionRequest, "user.added.successfully");
			_log.info("LR User created");
			
			
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
			if(Validator.isNotNull(user)){
				try {
					UserLocalServiceUtil.deleteUser(user);
				} catch (PortalException e1) {
					_log.error(e.getMessage(), e);
				}
			}
			if(Validator.isNotNull(customUser)){
				CustomUserLocalServiceUtil.deleteCustomUser(customUser);
			}
			SessionErrors.add(actionRequest,"error-user-create");
			actionResponse.setRenderParameter("mvcRenderCommandName", "/create-user");
		}
	}
	
	private User createLRUser(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException{
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		
		
		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setUuid(UUID.randomUUID().toString());
		serviceContext.setCreateDate(new Date());
		serviceContext.setModifiedDate(new Date());
		
		// Creating User

		// Get user organization 
		long[] organizationIds = new long[1];
		long organizationId = getOrganizationIdForCreatingUser(actionRequest, actionResponse);
		if(organizationId>0){
			 organizationIds[0] = organizationId;
		}
		
		User user = UserLocalServiceUtil.addUser(themeDisplay.getUserId(), themeDisplay.getCompanyId(), true, null, null,
				 true, null, emailAddress, 0l, StringPool.BLANK, 
				themeDisplay.getLocale(), firstName,StringPool.BLANK, lastName, 1l, 
				1l, true, 1, 1, 1970,
				StringPool.BLANK, null, organizationIds, null, null,
				true, serviceContext);
		
		return user;
	}
	
	private CustomUser createCustomUser(ActionRequest actionRequest, ActionResponse actionResponse, User user, long parentUserId, 
			long organizationId, long organizationGroupId){
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String phone = ParamUtil.getString(actionRequest, "phoneNumber");
		String addressLine1 = ParamUtil.getString(actionRequest, "addressLine1");
		String addressLine2 = ParamUtil.getString(actionRequest, "addressLine1");
		String city = ParamUtil.getString(actionRequest, "city");
		String state = ParamUtil.getString(actionRequest, "state");
		String zipcode = ParamUtil.getString(actionRequest, "zipcode");
		String fax = ParamUtil.getString(actionRequest, "fax");
		
		CustomUser customUser = CustomUserLocalServiceUtil.createCustomUser(CounterLocalServiceUtil.increment());
		customUser.setLrUserId(user.getUserId());
		customUser.setParentUserId(parentUserId);
		customUser.setOrganizationId(organizationId);
		customUser.setOrganizationGroupId(organizationGroupId);
		customUser.setAddressLine1(addressLine1);
		customUser.setAddressLine2(addressLine2);
		customUser.setCity(city);
		customUser.setState(state);
		customUser.setZipcode(zipcode);
		customUser.setPhone(phone);
		customUser.setFax(fax);
		customUser.setCreateDate(new Date());
		customUser.setModifiedDate(new Date());
		customUser.setModifiedBy(themeDisplay.getUserId());
		customUser.setCreatedBy(themeDisplay.getUserId());
		customUser = CustomUserLocalServiceUtil.addCustomUser(customUser);
		return customUser;
	}
 	
	private long getOrganizationIdForCreatingUser(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException{
		long organizationId=0l;
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean isLawyerAdmin = ParamUtil.getBoolean(actionRequest, "isLawyerAdmin");
		boolean isDoctorAdmin = ParamUtil.getBoolean(actionRequest, "isDoctorAdmin");
		boolean isAdmin = ParamUtil.getBoolean(actionRequest, "isAdmin");
		boolean isClinicAdmin = ParamUtil.getBoolean(actionRequest, "isClinicAdmin");
		boolean isSystemAdmin = ParamUtil.getBoolean(actionRequest, "isSystemAdmin");
		String entity = ParamUtil.getString(actionRequest, "entity");
		//if(isLawyerAdmin || (isAdmin && entity.equals("lawyer"))){
		  if(entity.equals("lawyer")){
			try {
				Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Lawyer");
				organizationId = lawyerOrg.getOrganizationId();
				return organizationId;
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
		}
		//if(isDoctorAdmin || (isAdmin && entity.equals("doctor"))){
		  if(entity.equals("doctor")){
			try {
				Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(), "Doctor");
				organizationId = doctorOrg.getOrganizationId();
				return organizationId;
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			
		}
		//if(isClinicAdmin || (isAdmin && entity.equals("clinic"))){
		  if(entity.equals("clinic")){
				long clinicId = ParamUtil.getLong(actionRequest, "clinic");
				Clinic clinic = ClinicLocalServiceUtil.getClinic(clinicId);
				return clinic.getClinicorganizationId();
		}
		return organizationId;
	}
	
	private long getParentUserId(ActionRequest actionRequest, ActionResponse actionResponse){
		long parentUserId=0l;
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean isLawyerAdmin = ParamUtil.getBoolean(actionRequest, "isLawyerAdmin");
		boolean isDoctorAdmin = ParamUtil.getBoolean(actionRequest, "isDoctorAdmin");
		boolean isClinicAdmin = ParamUtil.getBoolean(actionRequest, "isClinicAdmin");
		boolean isAdmin = ParamUtil.getBoolean(actionRequest, "isAdmin");
		
		if(isLawyerAdmin || isDoctorAdmin || isClinicAdmin){
			return themeDisplay.getUserId();
		}
		if(isAdmin){
			long respectiveParentUserId = ParamUtil.getLong(actionRequest, "respectiveParentUserId");
			return respectiveParentUserId;
		}
		return parentUserId;
	}
	
	private long getOrganizationGroupIdFromOrgId(long orgId){
		long orgGroupId=-1;
		try {
			orgGroupId = OrganizationLocalServiceUtil.getOrganization(orgId).getGroupId();
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return orgGroupId;
	}

}
