package com.omri.service.common.beans;

import com.liferay.portal.kernel.model.User;
import com.omri.service.common.model.CustomUser;

public class UserBean {
	private User user;
	private CustomUser customUser;
	
	public UserBean(User user,CustomUser customUser){
		this.user = user;
		this.customUser = customUser;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public CustomUser getCustomUser() {
		return customUser;
	}
	public void setCustomUser(CustomUser customUser) {
		this.customUser = customUser;
	}
}
