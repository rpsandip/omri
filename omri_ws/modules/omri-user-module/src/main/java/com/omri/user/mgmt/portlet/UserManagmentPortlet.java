package com.omri.user.mgmt.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.UserBean;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.CustomUserLocalServiceUtil;

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
		"com.liferay.portlet.display-category=category.omri",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=omri-user-module Portlet",
		"javax.portlet.init-param.template-path=/",
		"com.liferay.portlet.action-url-redirect=true",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class UserManagmentPortlet extends MVCPortlet {
	private static Log _log = LogFactoryUtil.getLog(UserManagmentPortlet.class);
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		List<CustomUser> childUserList = CustomUserLocalServiceUtil.getChildUsers(themeDisplay.getUserId());
		List<UserBean> childUserBeanList = new ArrayList<UserBean>();
		for(CustomUser customUser:childUserList){
			try {
				User user = UserLocalServiceUtil.getUser(customUser.getLrUserId());
				UserBean userBean = new UserBean(user, customUser);
				childUserBeanList.add(userBean);
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
		}
		renderRequest.setAttribute("childUserBeanList", childUserBeanList);
		include(viewTemplate, renderRequest, renderResponse);
	}
	
}