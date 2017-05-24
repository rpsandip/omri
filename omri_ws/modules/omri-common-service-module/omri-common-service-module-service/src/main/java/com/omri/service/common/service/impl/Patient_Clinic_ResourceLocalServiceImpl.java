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

import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.base.Patient_Clinic_ResourceLocalServiceBaseImpl;
import com.omri.service.common.service.persistence.Patient_Clinic_ResourcePK;

/**
 * The implementation of the patient_ clinic_ resource local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.Patient_Clinic_ResourceLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Patient_Clinic_ResourceLocalServiceBaseImpl
 * @see com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil
 */
@ProviderType
public class Patient_Clinic_ResourceLocalServiceImpl
	extends Patient_Clinic_ResourceLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil} to access the patient_ clinic_ resource local service.
	 */
	
	public Patient_Clinic_Resource addPatientClinicResource(long patientId, long resourceId, long clinicId, long specificationId, long procedureId,int noOfOccurnace, long createdBy, long modifiedBy){
		Patient_Clinic_ResourcePK patientClinicResourcePK = new Patient_Clinic_ResourcePK();
		patientClinicResourcePK.setClinicId(clinicId);
		patientClinicResourcePK.setPatientId(patientId);
		patientClinicResourcePK.setResourceId(resourceId);
		patientClinicResourcePK.setSpecificationId(specificationId);
		Patient_Clinic_Resource patientClinicResource = Patient_Clinic_ResourceLocalServiceUtil.createPatient_Clinic_Resource(patientClinicResourcePK);
		patientClinicResource.setProcedureId(procedureId);
		patientClinicResource.setNoOfOccurance(noOfOccurnace);
		patientClinicResource.setCreatedBy(createdBy);
		patientClinicResource.setModifiedBy(modifiedBy);
		patientClinicResource = Patient_Clinic_ResourceLocalServiceUtil.addPatient_Clinic_Resource(patientClinicResource);
		return patientClinicResource;
	}
	
	public List<Patient_Clinic_Resource> getPatientClinicByPatiendIdAndClinicIdAndCreateUserId(long patientId, long clinicId, long userId){
		return patient_Clinic_ResourcePersistence.findByPatientIdAndCreatedUserIdAndClinicId(patientId, clinicId, userId);
	}
	
	public List<Patient_Clinic_Resource> getPatientClinicByPatiendIdAndClinicId(long patientId, long clinicId){
		return patient_Clinic_ResourcePersistence.findByPatientIdAndClinicId(patientId, clinicId);
	}
	
}