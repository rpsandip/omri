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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.omri.service.common.model.Resource_Specification;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.service.base.Resource_SpecificationLocalServiceBaseImpl;

/**
 * The implementation of the resource_ specification local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.Resource_SpecificationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Resource_SpecificationLocalServiceBaseImpl
 * @see com.omri.service.common.service.Resource_SpecificationLocalServiceUtil
 */
@ProviderType
public class Resource_SpecificationLocalServiceImpl
	extends Resource_SpecificationLocalServiceBaseImpl {
	
	private static Log _log = LogFactoryUtil.getLog(Resource_SpecificationLocalServiceImpl.class);
	
	public List<Specification> getSpecificationOfResource(long resourceId){
		List<Specification> specificationList = new ArrayList<Specification>();
		List<Resource_Specification> resourceSpecificationList = resource_SpecificationPersistence.findByresourceId(resourceId);
		for(Resource_Specification resourceSpecification : resourceSpecificationList){
			try {
				Specification specification = SpecificationLocalServiceUtil.getSpecification(resourceSpecification.getSpecificationId());
				specificationList.add(specification);
			} catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
		}
		return specificationList;
	}
}