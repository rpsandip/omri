package com.omri.service.common.beans;

import java.util.Date;

import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;

public class AppointmentBean {
		private long appointmentId;
		private long patientId;
		private String patientFirtName;
		private String patientLastName;
		private String patientPhoneNo;
		private long clinicId;
		private String clinicName;
		private long doctorId;
		private String doctorName;
		private long resourceId;
		private String appointmentStatus;
		private String resourceName;
		private long specificationId;
		private String specificationName;
		private Date appointmentDate;
		private int appointmetProcessTime;
		private long createdBy;
		private long modifiedBy;
		private Date createdDate;
		private Date modifiedDate;
		
		public AppointmentBean(Appointment appointment, Patient patient, Clinic clinic,Resource resource, Specification specification){
			
			if(Validator.isNotNull(patient)){
				this.patientFirtName = patient.getFirstName();
				this.patientLastName = patient.getLastName();
				this.patientPhoneNo = patient.getPhoneNo();
			}
			if(Validator.isNotNull(clinic)){
				this.clinicId = clinic.getClinicId();
				this.clinicName = clinic.getClinicName();
			}
			this.doctorId = appointment.getDoctorId();
			if(Validator.isNotNull(resource)){
				this.resourceId = resource.getResourceId();
				this.resourceName = resource.getResourceName();
			}
			if(Validator.isNotNull(specification)){
				this.specificationId = specification.getSpecificationId();
				this.specificationName = specification.getSpecificationName();	
			}
			if(Validator.isNotNull(appointment)){
				this.appointmentId = appointment.getAppointmentId();
				this.patientId = appointment.getPatientId();
				this.appointmentDate = appointment.getAppointmetDate();
				this.appointmetProcessTime = appointment.getAppointmetProcessTime();
				this.createdBy = appointment.getCreatedBy();
				this.createdDate = appointment.getCreateDate();
				this.modifiedBy = appointment.getModifiedBy();
				this.modifiedDate = appointment.getModifiedDate();
				this.appointmentStatus = 
			}
		}
		
		
		
		
		public long getAppointmentId() {
			return appointmentId;
		}
		public void setAppointmentId(long appointmentId) {
			this.appointmentId = appointmentId;
		}
		public long getPatientId() {
			return patientId;
		}
		public void setPatientId(long patientId) {
			this.patientId = patientId;
		}
		public String getPatientFirtName() {
			return patientFirtName;
		}
		public void setPatientFirtName(String patientFirtName) {
			this.patientFirtName = patientFirtName;
		}
		public String getPatientLastName() {
			return patientLastName;
		}
		public void setPatientLastName(String patientLastName) {
			this.patientLastName = patientLastName;
		}
		public long getClinicId() {
			return clinicId;
		}
		public void setClinicId(long clinicId) {
			this.clinicId = clinicId;
		}
		public String getClinicName() {
			return clinicName;
		}
		public void setClinicName(String clinicName) {
			this.clinicName = clinicName;
		}
		public long getDoctorId() {
			return doctorId;
		}
		public void setDoctorId(long doctorId) {
			this.doctorId = doctorId;
		}
		public String getDoctorName() {
			return doctorName;
		}
		public void setDoctorName(String doctorName) {
			this.doctorName = doctorName;
		}
		public long getResourceId() {
			return resourceId;
		}
		public void setResourceId(long resourceId) {
			this.resourceId = resourceId;
		}
		public String getResourceName() {
			return resourceName;
		}
		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}
		public long getSpecificationId() {
			return specificationId;
		}
		public void setSpecificationId(long specificationId) {
			this.specificationId = specificationId;
		}
		public String getSpecificationName() {
			return specificationName;
		}
		public void setSpecificationName(String specificationName) {
			this.specificationName = specificationName;
		}
		public Date getAppointmentDate() {
			return appointmentDate;
		}
		public void setAppointmentDate(Date appointmentDate) {
			this.appointmentDate = appointmentDate;
		}
		public int getAppointmetProcessTime() {
			return appointmetProcessTime;
		}
		public void setAppointmetProcessTime(int appointmetProcessTime) {
			this.appointmetProcessTime = appointmetProcessTime;
		}
		public long getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(long createdBy) {
			this.createdBy = createdBy;
		}
		public long getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(long modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
		public String getPatientPhoneNo() {
			return patientPhoneNo;
		}
		public void setPatientPhoneNo(String patientPhoneNo) {
			this.patientPhoneNo = patientPhoneNo;
		}
		public String getAppointmentStatus() {
			return appointmentStatus;
		}
		public void setAppointmentStatus(String appointmentStatus) {
			this.appointmentStatus = appointmentStatus;
		}
		

 }
