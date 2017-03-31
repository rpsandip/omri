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

import java.util.Date;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.base.ClinicLocalServiceBaseImpl;

/**
 * The implementation of the clinic local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.ClinicLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ClinicLocalServiceBaseImpl
 * @see com.omri.service.common.service.ClinicLocalServiceUtil
 */
@ProviderType
public class ClinicLocalServiceImpl extends ClinicLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.ClinicLocalServiceUtil} to access the clinic local service.
	 */
	
	public Clinic addClinic(String clinicName, String addressLine1, String addressLine2, String city, String state,
			String zip, String phone, String fax, long createdBy, long modifiedBy){
		Clinic clinic = ClinicLocalServiceUtil.createClinic(CounterLocalServiceUtil.increment());
		clinic.setClinicName(clinicName);
		clinic.setAddressLine1(addressLine1);
		clinic.setAddressLine2(addressLine2);
		clinic.setCity(city);
		clinic.setState(state);
		clinic.setZipcode(zip);
		clinic.setPhoneNo(phone);
		clinic.setFaxNo(fax);
		clinic.setClinicorganizationId(0l);
		clinic.setClinicorganizationGroupId(0l);
		clinic.setCreatedBy(createdBy);
		clinic.setCreateDate(new Date());
		clinic.setModifiedDate(new Date());
		clinic.setModifiedBy(modifiedBy);
		clinic = ClinicLocalServiceUtil.addClinic(clinic);
		return clinic;
	}
}