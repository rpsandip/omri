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

import javax.xml.bind.ValidationEvent;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactory;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.exception.NoSuchPatientException;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
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

	public Patient createPatient(String customPatientId, String firstName,String lastName,Date dob, Date dos, Date doi, String payee,String phoneNo,String addressLine1, String addressLine2,
			String city, String state, String country, String zip, String lopNotes, String orderNotes, String invoiceNotes,String otherNotes,long createdBy, long modifiedBy){
		
		Patient patient = PatientLocalServiceUtil.createPatient(CounterLocalServiceUtil.increment());
		patient.setCustomPatientId(customPatientId);
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setDob(dob);
		patient.setDos(dos);
		patient.setDoi(doi);
		patient.setPayee(payee);
		patient.setAddressLine1(addressLine1);
		patient.setAddressLine2(addressLine2);
		patient.setPhoneNo(phoneNo);
		patient.setCity(city);
		patient.setState(state);
		patient.setCountry(country);
		patient.setZip(zip);
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
	
	public Patient updatePatient(long patientId, String customPatientId,String firstName,String lastName,Date dob, Date dos, Date doi, String payee,String phoneNo,String addressLine1, String addressLine2,
			String city, String state, String country, String zip, String lopNotes, String orderNotes, String invoiceNotes,String otherNotes, long modifiedBy){
		Patient patient =null;
		try {
			patient = PatientLocalServiceUtil.getPatient(patientId);
			patient.setCustomPatientId(customPatientId);
			patient.setFirstName(firstName);
			patient.setLastName(lastName);
			patient.setDob(dob);
			patient.setDos(dos);
			patient.setDoi(doi);
			patient.setPayee(payee);
			patient.setAddressLine1(addressLine1);
			patient.setAddressLine2(addressLine2);
			patient.setPhoneNo(phoneNo);
			patient.setCity(city);
			patient.setState(state);
			patient.setCountry(country);
			patient.setZip(zip);
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
	
	public List<Patient> getPatientList(int start, int end, String keyword,long createdByUserId){
		List<Patient> patientList = new ArrayList<Patient>();
		
		DynamicQuery dynamicQuery = PatientLocalServiceUtil.dynamicQuery();
		if(createdByUserId>0){
			dynamicQuery.add(PropertyFactoryUtil.forName("createdBy").eq(createdByUserId));
		}
		if(Validator.isNotNull(keyword)){
			Criterion keywordCriterion =  RestrictionsFactoryUtil.like("firstName", StringPool.PERCENT+keyword+StringPool.PERCENT);
			keywordCriterion = RestrictionsFactoryUtil.or(keywordCriterion, RestrictionsFactoryUtil.like("lastName", StringPool.PERCENT+keyword+StringPool.PERCENT));
			keywordCriterion = RestrictionsFactoryUtil.or(keywordCriterion, RestrictionsFactoryUtil.like("phoneNo", StringPool.PERCENT+keyword+StringPool.PERCENT));
			dynamicQuery.add(keywordCriterion);
		}
		dynamicQuery.setLimit(start, end);
		Order defaultOrder = OrderFactoryUtil.desc("createDate");
	    dynamicQuery.addOrder(defaultOrder);
	    
	    patientList = PatientLocalServiceUtil.dynamicQuery(dynamicQuery);
	    
		return patientList;
	}
	
	public Patient importPatient(String customPatientId, Date dos, String firstName, String lastName,
			Date dob, String phoneNo, String address, String city, String zipcode,long clinicId,long resourceId, long specificationId,
		    String payee, User doctor, User lawyer, long createdBy) throws NoSuchCustomUserException{
		
		// Add Patient master record
		Patient patient = PatientLocalServiceUtil.createPatient(CounterLocalServiceUtil.increment());
		patient.setCustomPatientId(customPatientId);
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setDob(dob);
		patient.setDos(dos);
		patient.setAddressLine1(address);
		patient.setCity(city);
		patient.setZip(zipcode);
		patient.setPhoneNo(phoneNo);
		
		patient.setCreatedBy(createdBy);
		patient.setModifiedBy(createdBy);
		patient.setCreateDate(new Date());
		patient.setModifiedDate(new Date());
		
		patient = PatientLocalServiceUtil.addPatient(patient);
		
		//Add Patient Clinic Data
		long doctorId = 0;
		String doctorName = StringPool.BLANK;
		String doctorPhone = StringPool.BLANK;
		String doctorEmail = StringPool.BLANK;
		long lawyerId=0;
		String lawyerName = StringPool.BLANK;
		String lawyerPhone = StringPool.BLANK;
		String lawyerEmail = StringPool.BLANK;
		
		if(Validator.isNotNull(doctor)){
			doctorId=doctor.getUserId();
		    doctorName = doctor.getFirstName()+StringPool.BLANK+doctor.getLastName();
		    doctorEmail  = doctor.getEmailAddress();
		    	CustomUser customUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(doctor.getUserId());
		    	doctorPhone = customUser.getPhone();
		   
		}
		if(Validator.isNotNull(lawyer)){
			lawyerId = lawyer.getUserId();
			lawyerName = lawyer.getFirstName()+StringPool.SPACE+lawyer.getLastName();
			lawyerEmail = lawyer.getEmailAddress();
		    	CustomUser customUser = CustomUserLocalServiceUtil.getCustomUserByLRUserId(lawyer.getUserId());
		    	lawyerPhone = customUser.getPhone();
		    
		}
		
		
		if(Validator.isNotNull(patient) ){
			
			// Add Patient Clinic
			Patient_ClinicLocalServiceUtil.addPatient_Clinic(patient.getPatientId(), clinicId, doctorId,
					doctorName, doctorPhone, doctorEmail,lawyerId, lawyerName, lawyerPhone, lawyerEmail, 0,
					createdBy, createdBy);
			
			// Add Patient Clinic Resource
			Patient_Clinic_ResourceLocalServiceUtil.addPatientClinicResource(patient.getPatientId(), 
					resourceId, clinicId, specificationId, 0, 1, 
					createdBy, createdBy);
		}
		
		
		return patient;
		
	}
	
	public Patient getPatientByCustomPatientId(String cusomPatientId) throws NoSuchPatientException{
		return patientPersistence.findBycustomPatientId(cusomPatientId);
	}
	
	public int searchPatientCount(long clinicId, Date startDate, Date endDate, String payee, long doctorId, long lawyerId, boolean searchByCreatedDate, long userId){
		List<Patient> patientList = searchPatient(clinicId, startDate, endDate, payee, doctorId, lawyerId, searchByCreatedDate, userId,-1, -1);
		return patientList.size();
	}
	
	public List<Patient> searchPatient(long clinicId, Date startDate, Date endDate, String payee, long doctorId, long lawyerId, boolean searchByCreatedDate, long userId, int start, int end){
		List<Patient> patientList = new ArrayList<Patient>();
		
		
		// Patient Clinic query
		DynamicQuery patientClinicQuery = Patient_ClinicLocalServiceUtil.dynamicQuery();
		patientClinicQuery.setProjection(ProjectionFactoryUtil.property("primaryKey.patientId"));
		
		Criterion patientClinicCriteria = null;
		if(clinicId>0){
			patientClinicCriteria = RestrictionsFactoryUtil.eq("primaryKey.clinicId", clinicId);
		}
		
		if(lawyerId>0){
			if(Validator.isNotNull(patientClinicCriteria)){
				patientClinicCriteria = RestrictionsFactoryUtil.and(patientClinicCriteria,RestrictionsFactoryUtil.eq("lawyerId", lawyerId));
			}else{
				patientClinicCriteria = RestrictionsFactoryUtil.eq("lawyerId", lawyerId);
			}
		}
		
		if(doctorId>0){
			if(Validator.isNotNull(patientClinicCriteria)){
				patientClinicCriteria = RestrictionsFactoryUtil.and(patientClinicCriteria,RestrictionsFactoryUtil.eq("doctorId", doctorId));
			}else{
				patientClinicCriteria = RestrictionsFactoryUtil.eq("doctorId", doctorId);
			}
		}
		
		if(Validator.isNotNull(patientClinicCriteria)){
			patientClinicQuery.setProjection(ProjectionFactoryUtil.property("primaryKey.patientId"));
			patientClinicQuery.add(patientClinicCriteria);
		}
		
		
		// Patient Query
		DynamicQuery dynamicQuery = PatientLocalServiceUtil.dynamicQuery();
		Criterion criteria = null;
		
		if(Validator.isNotNull(patientClinicCriteria)){
			criteria = PropertyFactoryUtil.forName("patientId").in(patientClinicQuery);
		}
		
		if(Validator.isNotNull(startDate) && Validator.isNotNull(endDate)){
			if(Validator.isNotNull(criteria)){
				criteria = RestrictionsFactoryUtil.and(criteria, RestrictionsFactoryUtil.between("dos", startDate, endDate));
			}else{
				criteria =  RestrictionsFactoryUtil.between("dos", startDate, endDate);
			}
		}
		
		if(Validator.isNotNull(payee)){
			if(Validator.isNotNull(criteria)){
				criteria = RestrictionsFactoryUtil.and(criteria, RestrictionsFactoryUtil.eq("payee", payee));
			}else{
				criteria = RestrictionsFactoryUtil.eq("payee", payee);
			}
		}
		
		if(searchByCreatedDate && userId>0){
			if(Validator.isNotNull(criteria)){
				criteria = RestrictionsFactoryUtil.and(criteria, RestrictionsFactoryUtil.eq("createdBy", userId));
			}else{
				criteria = RestrictionsFactoryUtil.eq("createdBy", userId); 
			}
		}
		
		if(Validator.isNotNull(criteria)){
			dynamicQuery.add(criteria);
		}
		
		Order defaultOrder = OrderFactoryUtil.desc("dos");
		dynamicQuery.addOrder(defaultOrder);
		dynamicQuery.setLimit(start, end);
		 
		 patientList = PatientLocalServiceUtil.dynamicQuery(dynamicQuery);
		 
		 
		return patientList;
	}
}