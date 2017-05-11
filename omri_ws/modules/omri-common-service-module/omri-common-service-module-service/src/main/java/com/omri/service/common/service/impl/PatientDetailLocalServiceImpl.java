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

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.omri.service.common.model.PatientDetail;
import com.omri.service.common.service.PatientDetailLocalServiceUtil;
import com.omri.service.common.service.base.PatientDetailLocalServiceBaseImpl;

/**
 * The implementation of the patient detail local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.PatientDetailLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PatientDetailLocalServiceBaseImpl
 * @see com.omri.service.common.service.PatientDetailLocalServiceUtil
 */
@ProviderType
public class PatientDetailLocalServiceImpl
	extends PatientDetailLocalServiceBaseImpl {
	
	public PatientDetail addPatientMoreDetail(long patientId, boolean mriBefore, boolean claustrophobic,boolean lbs300,boolean prevPatient,  boolean pacemaker,boolean metalInBody, boolean priorSurgery,boolean chanceOfPregent,
			boolean overAgeof60,boolean labsDone, boolean allergic, boolean diabetic, boolean hypertension, boolean cancer,
			boolean allergicToIdodine, boolean bloodthinners, String claustrophobicDetail, String pacemakerDetail, String metalInBodyDetail,
			String priorSurgeryDetail,String alergicDetail, String diabeticDetail, String allergicToIdodineDetail, String bloodthinnersDetail,
			String detailWithTimeStamp){
		PatientDetail patientDetail = PatientDetailLocalServiceUtil.createPatientDetail(CounterLocalServiceUtil.increment());
		patientDetail.setPatientId(patientId);
		patientDetail.setMRIBefore(mriBefore);
		patientDetail.setClaustrophobic(claustrophobic);
		patientDetail.setUnder300lbs(lbs300);
		patientDetail.setPreviousPatient(prevPatient);
		patientDetail.setMetalInBody(metalInBody);
		patientDetail.setPriorSurgery(priorSurgery);
		patientDetail.setChanceOfPregnent(chanceOfPregent);
		patientDetail.setOverAge60(overAgeof60);
		patientDetail.setLabsDone(labsDone);
		patientDetail.setAllergic(allergic);
		patientDetail.setDiabetic(diabetic);
		patientDetail.setHyperTension(hypertension);
		patientDetail.setCancer(cancer);
		patientDetail.setAllergicToIdodine(allergicToIdodine);
		patientDetail.setBloodthinners(bloodthinners);
		
		patientDetail.setClaustrophobicDetail(claustrophobicDetail);
		patientDetail.setPacemakerDetail(pacemakerDetail);
		patientDetail.setMetalInBodyDetail(metalInBodyDetail);
		patientDetail.setPriorSurgeryDetail(priorSurgeryDetail);
		patientDetail.setAlergicDetail(alergicDetail);
		patientDetail.setDiabeticDetail(diabeticDetail);
		patientDetail.setAllergicToIdodineDetail(allergicToIdodineDetail);
		patientDetail.setBloodthinnersDetail(bloodthinnersDetail);
		
		patientDetail.setDetailWithTimeStamp(detailWithTimeStamp);
		
		return PatientDetailLocalServiceUtil.addPatientDetail(patientDetail);
	}
}