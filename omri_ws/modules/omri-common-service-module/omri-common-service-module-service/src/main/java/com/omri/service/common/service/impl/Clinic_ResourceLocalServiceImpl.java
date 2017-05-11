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

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.base.Clinic_ResourceLocalServiceBaseImpl;
import com.omri.service.common.service.persistence.Clinic_ResourcePK;

/**
 * The implementation of the clinic_ resource local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.Clinic_ResourceLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Clinic_ResourceLocalServiceBaseImpl
 * @see com.omri.service.common.service.Clinic_ResourceLocalServiceUtil
 */
@ProviderType
public class Clinic_ResourceLocalServiceImpl
	extends Clinic_ResourceLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.Clinic_ResourceLocalServiceUtil} to access the clinic_ resource local service.
	 */
	
	public Clinic_Resource addClinic_Resource(long clinicId, long resourceId, long specificationId, int operationTime, int price){
		Clinic_ResourcePK clinicResourcePK = new Clinic_ResourcePK();
		clinicResourcePK.setClinicId(clinicId);
		clinicResourcePK.setResourceId(resourceId);
		clinicResourcePK.setSpecificationId(specificationId);
		Clinic_Resource clinicResource = Clinic_ResourceLocalServiceUtil.createClinic_Resource(clinicResourcePK);
		clinicResource.setOperationTime(operationTime);
		clinicResource.setPrice(price);
		clinicResource = Clinic_ResourceLocalServiceUtil.addClinic_Resource(clinicResource);
		return clinicResource;
	}
	
	public List<Clinic_Resource> getClinicResources(long clinicId){
		List<Clinic_Resource> clinicResources = new ArrayList<Clinic_Resource>();
		clinicResources = clinic_ResourcePersistence.findByClinicId(clinicId);
		return clinicResources;
	}
	
	public List<Clinic_Resource> getClinicResources(long clinicId, long resourceId){
		List<Clinic_Resource> clinicResources = new ArrayList<Clinic_Resource>();
		clinicResources = clinic_ResourcePersistence.findByClinicIdAndResourceId(clinicId, resourceId);
		return clinicResources;
	}
}