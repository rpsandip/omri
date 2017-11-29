package com.omri.service.common.beans;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.StringPool;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.util.OMRIConstants;

public class UserBean {
	private User user;
	private CustomUser customUser;
	private String roleName;
	private String entity;
	
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public UserBean(User user,CustomUser customUser){
		if(user.getLastName().equals(OMRIConstants.DEFAULT_USER_LASTNAME)){
			user.setLastName(StringPool.BLANK);
		}
		this.user = user;
		this.customUser = customUser;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		if(user.getLastName().equals(OMRIConstants.DEFAULT_USER_LASTNAME)){
			user.setLastName(StringPool.BLANK);
		}
		this.user = user;
	}
	public CustomUser getCustomUser() {
		return customUser;
	}
	public void setCustomUser(CustomUser customUser) {
		this.customUser = customUser;
	}
}
