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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.base.AppointmentLocalServiceBaseImpl;
import com.omri.service.common.util.AppointmentStatus;

/**
 * The implementation of the appointment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.AppointmentLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AppointmentLocalServiceBaseImpl
 * @see com.omri.service.common.service.AppointmentLocalServiceUtil
 */
@ProviderType
public class AppointmentLocalServiceImpl extends AppointmentLocalServiceBaseImpl {
	private static Log _log = LogFactoryUtil.getLog(AppointmentLocalServiceImpl.class);
	public Appointment createAppointment(long patientId, long clinicId, long resourceId, long specificationId, long doctorId,Date appointmentDate,int appointmentProcessTime,int noOfOccurance,
			long createdBy, long modifiedBy, Date createdDate, Date modifiedDate){
		Appointment appointment = AppointmentLocalServiceUtil.createAppointment(CounterLocalServiceUtil.increment());
		appointment.setPatientId(patientId);
		appointment.setClinicId(clinicId);
		appointment.setResourceId(resourceId);
		appointment.setSpecificationId(specificationId);
		appointment.setDoctorId(doctorId);
		appointment.setAppointmetDate(appointmentDate);
		appointment.setAppointmetProcessTime(appointmentProcessTime);
		appointment.setCreatedBy(createdBy);
		appointment.setNoOfOccurance(noOfOccurance);
		appointment.setModifiedBy(modifiedBy);
		appointment.setStatus(AppointmentStatus.CREATED.getValue());
		appointment.setCreateDate(createdDate);
		appointment.setModifiedDate(modifiedDate);
		appointment = AppointmentLocalServiceUtil.addAppointment(appointment);
		return appointment;
	}
	
	public List<Appointment> getPatientAppointments(long patientId, long clinicId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByPatientIdAndClinicId(patientId, clinicId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getPatientAppointmentsByClinicId(long clinicId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByClinicId(clinicId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getPatientAppointmentsByResourceId(long resourceId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByResourceId(resourceId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getPatientAppointmentsByPatientId(long patientId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByPatientId(patientId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getPatientAppointmentsByAppintmentDate(Date date){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByappointmentDate(date);
		return patientAppointmentList;
	}
	
	public List<Appointment> getAppointmentByPatientIdClinicIdResourceIdSpecificationId(long patientId, long clinicId, long resourceId,
			long specificationId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByPatientIdClinicIdResourceIdSpecificationId(patientId, clinicId, resourceId, specificationId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getAppointmentByPatientIdResourceIdSpecificationId(long patientId, long resourceId, long specificationId){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		patientAppointmentList = appointmentPersistence.findByPatientIdResourceIdSpecificationId(patientId, resourceId, specificationId);
		return patientAppointmentList;
	}
	
	public List<Appointment> getPatientAppointmentsByFiterData(long clinicId, long resourceId, String filterDate){
		List<Appointment> patientAppointmentList = new ArrayList<Appointment>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		Date filterStartDate=null;
		Date filterEndDate=null;
		try {
			if(Validator.isNotNull(filterDate)){
				filterStartDate = dateFormat.parse(filterDate+StringPool.SPACE+"00:00:00");
				filterEndDate = dateFormat.parse(filterDate+StringPool.SPACE+"23:59:59");
			}
		} catch (ParseException e) {
			_log.error(e.getMessage(), e);
		}
		
		DynamicQuery dynamicQuery = AppointmentLocalServiceUtil.dynamicQuery();
		if(clinicId>0){
			dynamicQuery.add(PropertyFactoryUtil.forName("clinicId").eq(clinicId));
		}
		if(resourceId>0){
			dynamicQuery.add(PropertyFactoryUtil.forName("resourceId").eq(resourceId));
		}
		if(Validator.isNotNull(filterStartDate)&& Validator.isNotNull(filterEndDate)){
			dynamicQuery.add(PropertyFactoryUtil.forName("appointmetDate").between(filterStartDate, filterEndDate));
		}
		patientAppointmentList = AppointmentLocalServiceUtil.dynamicQuery(dynamicQuery);
		return patientAppointmentList;
	}
	
	public List<Appointment> getClinicAppointmentListBystatus(long clinicId, int status){
		List<Appointment> appointmentList = new ArrayList<Appointment>();
		appointmentList = appointmentPersistence.findByStatusAndClinicId(clinicId, status);
		return appointmentList;
	}
	
}