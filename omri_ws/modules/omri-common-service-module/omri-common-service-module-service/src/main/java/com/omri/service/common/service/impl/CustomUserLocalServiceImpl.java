/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.omri.service.common.service.impl;

import aQute.bnd.annotation.ProviderType;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.base.CustomUserLocalServiceBaseImpl;

/**
 * The implementation of the custom user local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.CustomUserLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CustomUserLocalServiceBaseImpl
 * @see com.omri.service.common.service.CustomUserLocalServiceUtil
 */
@ProviderType
public class CustomUserLocalServiceImpl extends CustomUserLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.CustomUserLocalServiceUtil} to access the custom user local service.
	 */
	private static Log LOG = LogFactoryUtil.getLog(CustomUserLocalServiceImpl.class);
	
	public List<CustomUser> getChildUsers(long parentUserId){
		
		List<CustomUser> customUserList = new ArrayList<CustomUser>();
		DynamicQuery dynamicQuery = CustomUserLocalServiceUtil.dynamicQuery();
		dynamicQuery.add(PropertyFactoryUtil.forName("parentUserId").eq(parentUserId));
		customUserList = CustomUserLocalServiceUtil.dynamicQuery(dynamicQuery);
		return customUserList;
	}
	
	public CustomUser getCustomUserByLRUserId(long lrUserId) throws NoSuchCustomUserException{
		return customUserPersistence.findBylrUserId(lrUserId);
	}
	
}