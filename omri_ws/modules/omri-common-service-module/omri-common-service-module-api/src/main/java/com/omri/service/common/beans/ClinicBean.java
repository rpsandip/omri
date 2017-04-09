package com.omri.service.common.beans;

import java.util.Date;
import java.util.List;

import com.omri.service.common.model.Clinic;

public class ClinicBean {
	private long clinicId;
	private long clinicorganizationId;
	private long clinicorganizationGroupId;
	private String clinicName;
	private long clinicAdminId;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private String phoneNo;
	private String faxNo;
	private Date createDate;
	private long createdBy;
	private Date modifiedDate;
	private long modifiedBy;
	private List<ClientResourceBean> clinicResourceBeanList;
	
	public ClinicBean(Clinic clinic, List<ClientResourceBean> clinicResourceBeanList){
		this.clinicId = clinic.getClinicId();
		this.clinicorganizationId = clinic.getClinicorganizationId();
		this.clinicorganizationGroupId = clinic.getClinicorganizationGroupId();
		this.clinicName = clinic.getClinicName();
		this.clinicAdminId = clinic.getClinicAdminId();
		this.addressLine1= clinic.getAddressLine1();
		this.addressLine2 = clinic.getAddressLine2();
		this.city = clinic.getCity();
		this.state = clinic.getState();
		this.zipcode= clinic.getZipcode();
		this.country = clinic.getCountry();
		this.phoneNo = clinic.getPhoneNo();
		this.faxNo = clinic.getFaxNo();
		this.createDate = clinic.getCreateDate();
		this.modifiedDate = clinic.getModifiedDate();
		this.createdBy = clinic.getCreatedBy();
		this.modifiedBy = clinic.getModifiedBy();
		this.clinicResourceBeanList = clinicResourceBeanList;
				
	}
	
	
	public long getClinicId() {
		return clinicId;
	}
	public void setClinicId(long clinicId) {
		this.clinicId = clinicId;
	}
	public long getClinicorganizationId() {
		return clinicorganizationId;
	}
	public void setClinicorganizationId(long clinicorganizationId) {
		this.clinicorganizationId = clinicorganizationId;
	}
	public long getClinicorganizationGroupId() {
		return clinicorganizationGroupId;
	}
	public void setClinicorganizationGroupId(long clinicorganizationGroupId) {
		this.clinicorganizationGroupId = clinicorganizationGroupId;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public long getClinicAdminId() {
		return clinicAdminId;
	}
	public void setClinicAdminId(long clinicAdminId) {
		this.clinicAdminId = clinicAdminId;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public List<ClientResourceBean> getClinicResourceBeanList() {
		return clinicResourceBeanList;
	}
	public void setClinicResourceBeanList(List<ClientResourceBean> clinicResourceBeanList) {
		this.clinicResourceBeanList = clinicResourceBeanList;
	}
}
