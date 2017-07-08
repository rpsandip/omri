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

import java.util.List;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.base.OMRICommonLocalServiceBaseImpl;

/**
 * The implementation of the o m r i common local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.OMRICommonLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OMRICommonLocalServiceBaseImpl
 * @see com.omri.service.common.service.OMRICommonLocalServiceUtil
 */
@ProviderType
public class OMRICommonLocalServiceImpl extends OMRICommonLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.OMRICommonLocalServiceUtil} to access the o m r i common local service.
	
	 */
	
	private static Log _log = LogFactoryUtil.getLog(OMRICommonLocalServiceImpl.class.getName());
	
	public long getUserAssociatedOrgId(long userId) {
		long organizationId = 0l;
		try {
			CustomUser customUser =  CustomUserLocalServiceUtil.getCustomUserByLRUserId(userId);
			return customUser.getOrganizationId();
		} catch (NoSuchCustomUserException e) {
			_log.error(e.getMessage(), e);
		}
		return organizationId;
	  }
	 
	 public long getOrganizationGroupId(long orgId){
		 long orgGroupId=0l;
		 try {
			Organization org = OrganizationLocalServiceUtil.getOrganization(orgId);
			orgGroupId = org.getGroupId();
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		 return orgGroupId; 
	 }
	 
	 public String getDLFileAbsPath(FileEntry fileEntry) 
			 throws PortalException, SystemException {
			   return PropsUtil.get("dl.hook.file.system.root.dir") + "/"
			     + fileEntry.getCompanyId() + "/"
			     + fileEntry.getFolderId() + "/"
			     + ((DLFileEntry) fileEntry.getModel()).getName() + "/"
			     + fileEntry.getVersion();
	 }
	 
	 public String getDLFileURL(DLFileEntry file) {
		     return "/documents/" + file.getGroupId() + StringPool.SLASH + file.getFolderId() + StringPool.SLASH
		             + file.getTitle() + StringPool.SLASH + file.getUuid();
	 }	 
	 
}