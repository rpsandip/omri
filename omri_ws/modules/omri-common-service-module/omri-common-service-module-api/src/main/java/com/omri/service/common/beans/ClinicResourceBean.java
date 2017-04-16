package com.omri.service.common.beans;

import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;

public class ClinicResourceBean {
		private long clinicId;
		private String clinicName;
		private long resourceId;
		private String resourceName;
		private long specificationId;
		private String specificationName;
		private int operationTime;
		
		public ClinicResourceBean(Clinic clinic, Resource resource, Specification specification, Clinic_Resource clinicResoure){
			this.clinicId = clinic.getClinicId();
			this.clinicName = clinic.getClinicName();
			this.resourceId = resource.getResourceId();
			this.resourceName = resource.getResourceName();
			this.specificationId = specification.getSpecificationId();
			this.specificationName  = specification.getSpecificationName();
			this.operationTime = clinicResoure.getOperationTime();
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
		public int getOperationTime() {
			return operationTime;
		}
		public void setOperationTime(int operationTime) {
			this.operationTime = operationTime;
		}
}
