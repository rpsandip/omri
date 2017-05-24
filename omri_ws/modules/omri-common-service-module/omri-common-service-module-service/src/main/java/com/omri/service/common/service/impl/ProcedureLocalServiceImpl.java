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
import com.omri.service.common.exception.NoSuchProcedureException;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.base.ProcedureLocalServiceBaseImpl;

/**
 * The implementation of the procedure local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.ProcedureLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ProcedureLocalServiceBaseImpl
 * @see com.omri.service.common.service.ProcedureLocalServiceUtil
 */
@ProviderType
public class ProcedureLocalServiceImpl extends ProcedureLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.ProcedureLocalServiceUtil} to access the procedure local service.
	 */
	
	public Procedure addPatientProcedure(long patientId, long clinicId){
		Procedure patientProcedure = ProcedureLocalServiceUtil.createProcedure(CounterLocalServiceUtil.increment());
		patientProcedure.setPatientId(patientId);
		patientProcedure.setClinicId(clinicId);
		patientProcedure.setIsComplete(false);
		patientProcedure = ProcedureLocalServiceUtil.addProcedure(patientProcedure);
		return patientProcedure;
	}
	
	public Procedure getProcedureByPatientIdAndClinicId(long patientId, long clinicId) throws NoSuchProcedureException{
		return procedurePersistence.findByclinicIdAndPatientId(clinicId, patientId);
	}
	
	public List<Procedure> getClinicProcedureCompleted(long clinicId){
		List<Procedure> clinicProcedureList = new ArrayList<Procedure>();
		clinicProcedureList = procedurePersistence.findByClinicIdAndStatus(clinicId,true);
		return clinicProcedureList;
	}
}