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

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.omri.service.common.exception.NoSuchPatient_ClinicException;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.base.Patient_ClinicLocalServiceBaseImpl;
import com.omri.service.common.service.persistence.Patient_ClinicPK;
import com.omri.service.common.util.PatientDocumentStatus;

/**
 * The implementation of the patient_ clinic local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.Patient_ClinicLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Patient_ClinicLocalServiceBaseImpl
 * @see com.omri.service.common.service.Patient_ClinicLocalServiceUtil
 */
@ProviderType
public class Patient_ClinicLocalServiceImpl
	extends Patient_ClinicLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.Patient_ClinicLocalServiceUtil} to access the patient_ clinic local service.
	 */
	Log _log = LogFactoryUtil.getLog(Patient_ClinicLocalServiceImpl.class.getName());
	
	public Patient_Clinic addPatient_Clinic(long patientId, long clinicId, long doctorId, String doctorName
			, String doctorPhoneNo, String doctorEmail,long lawyerId, String lawyerName, String lawyerPhoneNo, String lawyerEmail,int patientStatus,long createdBy, long modifiedBy){
		Patient_ClinicPK patientClinicPK = new Patient_ClinicPK();
		patientClinicPK.setClinicId(clinicId);
		patientClinicPK.setPatientId(patientId);
		Patient_Clinic patientClinic = Patient_ClinicLocalServiceUtil.createPatient_Clinic(patientClinicPK);
		patientClinic.setDoctorName(doctorName);
		patientClinic.setDoctorId(doctorId);
		patientClinic.setPatient_status(patientStatus);
		patientClinic.setDocumentStatus(PatientDocumentStatus.PENDING.getValue());
		patientClinic.setDoctorPhoneNo(doctorPhoneNo);
		patientClinic.setDoctorEmail(doctorEmail);
		patientClinic.setLawyerId(lawyerId);
		patientClinic.setLawyerName(lawyerName);
		patientClinic.setLawyerPhoneNo(lawyerPhoneNo);
		patientClinic.setLawyerEmail(lawyerEmail);
		patientClinic.setCreatedBy(createdBy);
		patientClinic.setModifiedBy(modifiedBy);
		patientClinic.setCreateDate(new Date());
		patientClinic.setModifiedDate(new Date());
		
		patientClinic = Patient_ClinicLocalServiceUtil.addPatient_Clinic(patientClinic);
		return patientClinic;
	}
	
	public Patient_Clinic updatePatient_Clinic(long patientId, long clinicId, long doctorId, String doctorName
			, String doctorPhoneNo,  String doctorEmail,long lawyerId, String lawyerName, String lawyerPhoneNo,
			String lawyerEmail,long modifiedBy){
		Patient_Clinic patientClinic = null;
		Patient_ClinicPK patientClinicPK = new Patient_ClinicPK();
		patientClinicPK.setClinicId(clinicId);
		patientClinicPK.setPatientId(patientId);
		try {
			 patientClinic = Patient_ClinicLocalServiceUtil.getPatient_Clinic(patientClinicPK);
			 patientClinic.setDoctorName(doctorName);
			 patientClinic.setDoctorId(doctorId);
			 patientClinic.setDoctorPhoneNo(doctorPhoneNo);
			 patientClinic.setDoctorEmail(doctorEmail);
			 patientClinic.setLawyerId(lawyerId);
			 patientClinic.setLawyerName(lawyerName);
			 patientClinic.setLawyerPhoneNo(lawyerPhoneNo);
			 patientClinic.setLawyerEmail(lawyerEmail);
			 patientClinic.setModifiedDate(new Date());
			 patientClinic.setModifiedBy(modifiedBy);
			 Patient_ClinicLocalServiceUtil.updatePatient_Clinic(patientClinic);
		} catch (PortalException e) {
			_log.error(e);
		}
		return patientClinic;
	}
	
	public Patient_Clinic getPatientClinicByPatientIdandCreatorUserId(long patiendId, long userId) throws NoSuchPatient_ClinicException{
		return patient_ClinicPersistence.findByPatientIdAndCreatedUserId(patiendId, userId);
	}
	
	public List<Patient_Clinic> getPatientsOfClinic(long clinicId){
		return patient_ClinicPersistence.findByClinicId(clinicId);
	}
	
	public Patient_Clinic getPatientClinicByPatientId(long patientId) throws NoSuchPatient_ClinicException{
		return patient_ClinicPersistence.findByPatientId(patientId);
	}
	
	public List<Patient_Clinic> getPatientsOfClinic(long clinicId, Date startDate, Date endDate){
		List<Patient_Clinic> patientLClinicList = new ArrayList<Patient_Clinic>();
		DynamicQuery dynamicQuery = Patient_ClinicLocalServiceUtil.dynamicQuery();
		dynamicQuery.add(RestrictionsFactoryUtil.eq("primaryKey.clinicId", clinicId));
		dynamicQuery.add(RestrictionsFactoryUtil.between("createDate",startDate, endDate));
		
		Order dateOrder = OrderFactoryUtil.desc("createDate");
		dynamicQuery.addOrder(dateOrder);
		
		patientLClinicList = Patient_ClinicLocalServiceUtil.dynamicQuery(dynamicQuery);
		return patientLClinicList;
	}
	
}