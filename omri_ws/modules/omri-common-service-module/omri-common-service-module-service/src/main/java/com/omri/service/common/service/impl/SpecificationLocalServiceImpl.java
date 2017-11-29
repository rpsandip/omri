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

import com.omri.service.common.exception.NoSuchSpecificationException;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.base.SpecificationLocalServiceBaseImpl;

/**
 * The implementation of the specification local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.SpecificationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SpecificationLocalServiceBaseImpl
 * @see com.omri.service.common.service.SpecificationLocalServiceUtil
 */
@ProviderType
public class SpecificationLocalServiceImpl
	extends SpecificationLocalServiceBaseImpl {
	
	public Specification getSpecificationByName(String specificationName) throws NoSuchSpecificationException{
		return specificationPersistence.findByspecificationName(specificationName);
	}
	
}