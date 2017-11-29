package com.omri.service.common.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Resource;
import com.omri.service.common.service.ClinicLocalServiceUtil;

public class PatientBean{
	
	Log _log = LogFactoryUtil.getLog(PatientBean.class.getName());
	
	private long patientId;
	private String customPatientId;
	private String firstName;
	private String clinicName=StringPool.BLANK;
	private String lastName;
	private Date dob;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String phoneNo;
	private Date createdDate;
	private Date modifiedDate;
	private long createdBy;
	private long modifiedBy;
	private String lopNotes;
	private String orderNotes;
	private String invoiceNotes;
	private String otherNotes;
	private Date dos;
	private Date doi;
	private String payee;
	
	
	
	private Patient_Clinic patientClinic;
	private List<PatientResourceBean> resourceBeanList = new ArrayList<PatientResourceBean>();
	private List<DocumentBean> lopDocuments= new ArrayList<DocumentBean>();
	private List<DocumentBean> invoiceDocuments= new ArrayList<DocumentBean>();
	private List<DocumentBean> orderDocuments= new ArrayList<DocumentBean>();
	private List<DocumentBean> procedureDocumnts= new ArrayList<DocumentBean>();
	private List<DocumentBean> lopRequestDocuments= new ArrayList<DocumentBean>();
	
 	public PatientBean(){
		
	}
	
	public PatientBean(Patient patient, Patient_Clinic patientClinic ,List<PatientResourceBean> resourceBeanList){
		this.patientId = patient.getPatientId();
		this.customPatientId = patient.getCustomPatientId();
		this.firstName = patient.getFirstName();
		this.lastName = patient.getLastName();
		this.dob = patient.getDob();
		/*Date of Service*/
		this.dos= patient.getDos();
		/* Date of Injury*/
		this.doi = patient.getDoi();
		this.addressLine1 = patient.getAddressLine1();
		this.addressLine2 = patient.getAddressLine2();
		this.city = patient.getCity();
		this.state = patient.getState();
		this.zip = patient.getZip();
		this.country = patient.getCountry();
		this.phoneNo = patient.getPhoneNo();
		this.createdDate  =patient.getCreateDate();
		this.modifiedDate = patient.getModifiedDate();
		this.createdBy = patient.getCreatedBy();
		this.modifiedBy  = patient.getModifiedBy();
		this.resourceBeanList = resourceBeanList;
		this.patientClinic = patientClinic;
		this.lopNotes = patient.getLopNotes();
		this.orderNotes = patient.getOrderNotes();
		this.invoiceNotes = patient.getInvoiceNotes();
		this.otherNotes = patient.getOrderNotes();
		this.payee = patient.getPayee();
		
		if(Validator.isNotNull(this.patientClinic)){
			try {
				Clinic clinic = ClinicLocalServiceUtil.getClinic(this.patientClinic.getClinicId());
				this.clinicName = clinic.getClinicName();
			} catch (PortalException e) {
				_log.error(e.getMessage());
			}
		}
	}
	
	public long getPatientId() {
		return patientId;
	}
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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
	public List<PatientResourceBean> getResourceBeanList() {
		return resourceBeanList;
	}
	public void setResourceBeanList(List<PatientResourceBean> resourceBeanList) {
		this.resourceBeanList = resourceBeanList;
	}
	public Patient_Clinic getPatientClinic() {
		return patientClinic;
	}

	public void setPatientClinic(Patient_Clinic patientClinic) {
		this.patientClinic = patientClinic;
	}

	public List<DocumentBean> getLopDocuments() {
		return lopDocuments;
	}

	public void setLopDocuments(List<DocumentBean> lopDocuments) {
		this.lopDocuments = lopDocuments;
	}

	public List<DocumentBean> getInvoiceDocuments() {
		return invoiceDocuments;
	}

	public void setInvoiceDocuments(List<DocumentBean> invoiceDocuments) {
		this.invoiceDocuments = invoiceDocuments;
	}

	public List<DocumentBean> getOrderDocuments() {
		return orderDocuments;
	}

	public void setOrderDocuments(List<DocumentBean> orderDocuments) {
		this.orderDocuments = orderDocuments;
	}

	public List<DocumentBean> getProcedureDocumnts() {
		return procedureDocumnts;
	}

	public void setProcedureDocumnts(List<DocumentBean> procedureDocumnts) {
		this.procedureDocumnts = procedureDocumnts;
	}
	public List<DocumentBean> getLopRequestDocuments() {
		return lopRequestDocuments;
	}

	public void setLopRequestDocuments(List<DocumentBean> lopRequestDocuments) {
		this.lopRequestDocuments = lopRequestDocuments;
	}

	public String getLopNotes() {
		return lopNotes;
	}

	public void setLopNotes(String lopNotes) {
		this.lopNotes = lopNotes;
	}

	public String getOrderNotes() {
		return orderNotes;
	}

	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	public String getInvoiceNotes() {
		return invoiceNotes;
	}

	public void setInvoiceNotes(String invoiceNotes) {
		this.invoiceNotes = invoiceNotes;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public String getCustomPatientId() {
		return customPatientId;
	}

	public void setCustomPatientId(String customPatientId) {
		this.customPatientId = customPatientId;
	}

	public Date getDos() {
		return dos;
	}

	public void setDos(Date dos) {
		this.dos = dos;
	}

	public Date getDoi() {
		return doi;
	}

	public void setDoi(Date doi) {
		this.doi = doi;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	
	
	
}
