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
import java.util.Date;
import java.util.List;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.omri.service.common.model.Patient;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.base.PatientLocalServiceBaseImpl;

/**
 * The implementation of the patient local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.PatientLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PatientLocalServiceBaseImpl
 * @see com.omri.service.common.service.PatientLocalServiceUtil
 */
@ProviderType
public class PatientLocalServiceImpl extends PatientLocalServiceBaseImpl {
	Log _log = LogFactoryUtil.getLog(PatientLocalServiceImpl.class.getName());
	
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.PatientLocalServiceUtil} to access the patient local service.
	 */
	
	public Patient createPatient(String firstName,String lastName, String cptCode,Date dob, String phoneNo,String addressLine1, String addressLine2,
			String city, String state, String country, String zip, String lopNotes, String orderNotes, String invoiceNotes,String otherNotes,long createdBy, long modifiedBy){
		Patient patient = PatientLocalServiceUtil.createPatient(CounterLocalServiceUtil.increment());
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setDob(dob);
		patient.setAddressLine1(addressLine1);
		patient.setAddressLine2(addressLine2);
		patient.setPhoneNo(phoneNo);
		patient.setCity(city);
		patient.setState(state);
		patient.setCountry(country);
		patient.setZip(zip);
		patient.setCptCode(cptCode);
		patient.setLopNotes(lopNotes);
		patient.setOrderNotes(orderNotes);
		patient.setInvoiceNotes(invoiceNotes);
		patient.setOtherNotes(otherNotes);
		patient.setCreatedBy(createdBy);
		patient.setModifiedBy(modifiedBy);
		patient.setCreateDate(new Date());
		patient.setModifiedDate(new Date());
		patient = PatientLocalServiceUtil.addPatient(patient);
		return patient;
	}
	
	public Patient updatePatient(long patientId, String firstName,String lastName, String cptCode,Date dob, String phoneNo,String addressLine1, String addressLine2,
			String city, String state, String country, String zip, String lopNotes, String orderNotes, String invoiceNotes,String otherNotes, long modifiedBy){
		Patient patient =null;
		try {
			patient = PatientLocalServiceUtil.getPatient(patientId);
			patient.setFirstName(firstName);
			patient.setLastName(lastName);
			patient.setDob(dob);
			patient.setAddressLine1(addressLine1);
			patient.setAddressLine2(addressLine2);
			patient.setPhoneNo(phoneNo);
			patient.setCity(city);
			patient.setState(state);
			patient.setCountry(country);
			patient.setZip(zip);
			patient.setCptCode(cptCode);
			patient.setLopNotes(lopNotes);
			patient.setOtherNotes(otherNotes);
			patient.setOrderNotes(orderNotes);
			patient.setInvoiceNotes(invoiceNotes);
			patient.setModifiedBy(modifiedBy);
			patient.setModifiedDate(new Date());
			patient = PatientLocalServiceUtil.updatePatient(patient);
		} catch (PortalException e) {
			_log.error(e);
		}
		return patient;
	}
	
	public List<Patient> getCreatedPatientList(long createdByUserId){
		List<Patient> patientList = new ArrayList<Patient>();
		patientList = patientPersistence.findByCreatedBy(createdByUserId);
		return patientList;
	}

	public List<Patient> getCreatedPatientListWithDate(long createdByUserId, Date startDate, Date endDate){
		List<Patient> patientList = new ArrayList<Patient>();
		DynamicQuery dynamicQuery = PatientLocalServiceUtil.dynamicQuery();
		dynamicQuery.add(RestrictionsFactoryUtil.eq("createdBy", createdByUserId));
		dynamicQuery.add(RestrictionsFactoryUtil.between("createDate",startDate, endDate));
		patientList = PatientLocalServiceUtil.dynamicQuery(dynamicQuery);
		return patientList;
	}
	
}