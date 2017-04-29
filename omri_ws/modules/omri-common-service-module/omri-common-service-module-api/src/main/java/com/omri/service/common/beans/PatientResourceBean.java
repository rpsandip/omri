package com.omri.service.common.beans;

import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;

public class PatientResourceBean{
	private long patientId;
	private long clinicId;
	private String clinicName;
	private String resourceName;
	private long resourceId;
	private String specificationName;
	private long specificationId;
	private int occurnace;
	private int remainingOccurance;
	
	public PatientResourceBean(Patient patient, Clinic clinic,Resource resource, Specification specification, Patient_Clinic_Resource patientClinicResource){
		this.patientId = patient.getPatientId();
		this.clinicId = clinic.getClinicId();
		this.resourceName = resource.getResourceName();
		this.resourceId = resource.getResourceId();
		this.specificationId = specification.getSpecificationId();
		this.specificationName = specification.getSpecificationName();
		this.occurnace = patientClinicResource.getNoOfOccurance();
		this.remainingOccurance = patientClinicResource.getNoOfOccurance();
	}
	
	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	
	public int getOccurnace() {
		return occurnace;
	}

	public void setOccurnace(int occurnace) {
		this.occurnace = occurnace;
	}
	
	public long getPatientId() {
		return patientId;
	}
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
	public long getClinicId() {
		return clinicId;
	}
	public void setClinicId(long clinicId) {
		this.clinicId = clinicId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getSpecificationName() {
		return specificationName;
	}
	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}
	public long getSpecificationId() {
		return specificationId;
	}
	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}
	public int getRemainingOccurance() {
		return remainingOccurance;
	}

	public void setRemainingOccurance(int remainingOccurance) {
		this.remainingOccurance = remainingOccurance;
	}
}
