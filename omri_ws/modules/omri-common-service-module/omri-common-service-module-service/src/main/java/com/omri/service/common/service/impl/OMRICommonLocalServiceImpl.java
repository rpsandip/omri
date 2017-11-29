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

import javax.portlet.RenderRequest;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.DocumentBean;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.exception.NoSuchPatient_ClinicException;
import com.omri.service.common.model.Appointment;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.CustomUser;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.AppointmentLocalServiceUtil;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.CustomUserLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.service.base.OMRICommonLocalServiceBaseImpl;
import com.omri.service.common.util.AppointmentStatus;

/**
 * The implementation of the o m r i common local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.omri.service.common.service.OMRICommonLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OMRICommonLocalServiceBaseImpl
 * @see com.omri.service.common.service.OMRICommonLocalServiceUtil
 */
@ProviderType
public class OMRICommonLocalServiceImpl extends OMRICommonLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.omri.service.common.service.OMRICommonLocalServiceUtil} to access the o m r i common local service.
	
	 */
	
	private static Log _log = LogFactoryUtil.getLog(OMRICommonLocalServiceImpl.class.getName());
	
	public long getUserAssociatedOrgId(long userId) {
		long organizationId = 0l;
		try {
			CustomUser customUser =  CustomUserLocalServiceUtil.getCustomUserByLRUserId(userId);
			return customUser.getOrganizationId();
		} catch (NoSuchCustomUserException e) {
			_log.error(e.getMessage());
		}
		return organizationId;
	  }
	 
	 public long getOrganizationGroupId(long orgId){
		 long orgGroupId=0l;
		 try {
			Organization org = OrganizationLocalServiceUtil.getOrganization(orgId);
			orgGroupId = org.getGroupId();
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		 return orgGroupId; 
	 }
	 
	 public String getDLFileAbsPath(FileEntry fileEntry) 
			 throws PortalException, SystemException {
			   return PropsUtil.get("dl.hook.file.system.root.dir") + "/"
			     + fileEntry.getCompanyId() + "/"
			     + fileEntry.getFolderId() + "/"
			     + ((DLFileEntry) fileEntry.getModel()).getName() + "/"
			     + fileEntry.getVersion();
	 }
	 
	 public String getDLFileURL(DLFileEntry file) {
		     return "/documents/" + file.getGroupId() + StringPool.SLASH + file.getFolderId() + StringPool.SLASH
		             + file.getTitle() + StringPool.SLASH + file.getUuid();
	 }	 
	 
	 
	 public User getUserByFirstNameAndLastName(String fName, String lName){
		 User user = null;
		 DynamicQuery dynamicQuery = UserLocalServiceUtil.dynamicQuery();
		 Criterion criterion = RestrictionsFactoryUtil.like("firstName", fName);
		 criterion =  RestrictionsFactoryUtil.and(criterion, RestrictionsFactoryUtil.like("lastName", lName));
		 dynamicQuery.add(criterion);
		 
		 List<User> users = UserLocalServiceUtil.dynamicQuery(dynamicQuery);
		 if(users.size()>0){
			 return users.get(0);
		 }
		 return user;
	 }
	 
	 public User getUserByFirstName(String fName){
		 User user = null;
		 DynamicQuery dynamicQuery = UserLocalServiceUtil.dynamicQuery();
		 Criterion criterion = RestrictionsFactoryUtil.like("firstName", fName);
		 dynamicQuery.add(criterion);
		 
		 List<User> users = UserLocalServiceUtil.dynamicQuery(dynamicQuery);
		 if(users.size()>0){
			 return users.get(0);
		 }
		 return user;
	 }
	 
	 
	public void setDoctorList(RenderRequest renderRequest) {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			try {
				Role doctorAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Doctor Admin");
				Organization doctorOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(),
						"Doctor");
				List<User> doctorAdminList = new ArrayList<User>();
				List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil
						.getUserGroupRolesByGroupAndRole(doctorOrg.getGroupId(), doctorAdminRole.getRoleId());
				for (UserGroupRole userGroupRole : userGroupRoleList) {
					User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
					doctorAdminList.add(user);
				}
				renderRequest.setAttribute("doctorAdminList", doctorAdminList);
			} catch (PortalException e) {
				_log.error(e.getMessage());
			}
	}
	
	public void setLayerList(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {
			Role lawyerAdminRole = getRoleWithName(themeDisplay.getCompanyId(), "Lawyer Admin");
			Organization lawyerOrg = OrganizationLocalServiceUtil.getOrganization(themeDisplay.getCompanyId(),
					"Lawyer");
			List<User> lawyeAdminList = new ArrayList<User>();
			List<UserGroupRole> userGroupRoleList = UserGroupRoleLocalServiceUtil
					.getUserGroupRolesByGroupAndRole(lawyerOrg.getGroupId(), lawyerAdminRole.getRoleId());
			for (UserGroupRole userGroupRole : userGroupRoleList) {
				User user = UserLocalServiceUtil.getUser(userGroupRole.getUserId());
				lawyeAdminList.add(user);
			}
			renderRequest.setAttribute("lawyerAdminList", lawyeAdminList);
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
	}
	
	public Role getRoleWithName(long companyId, String roleName) {
		Role role = null;
		try {
			role = RoleLocalServiceUtil.getRole(companyId, roleName);
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		return role;
	}
	
	public PatientBean getPatientBean(Patient patient){
		PatientBean patientBean = null;
		try {
			List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			Patient_Clinic patientClinc = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientId(patient.getPatientId());
			if(Validator.isNotNull(patientClinc)){
				List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patient.getPatientId(), patientClinc.getClinicId());
				for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
					try {
						Clinic clinic = ClinicLocalServiceUtil.getClinic(patientClinc.getClinicId());
						Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
						Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
						PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
						patientResourceBeanList.add(patientResourceBean);
					} catch (PortalException e) {
						_log.error(e.getMessage(), e);
					}
				}
			}
			patientBean = new PatientBean(patient, patientClinc,patientResourceBeanList);
		} catch (NoSuchPatient_ClinicException e) {
			_log.error(e.getMessage(), e);
		}

		return patientBean;
	}
	
	
	public void setPatientDocuments(long companyGroupId, PatientBean patientBean){
		List<DocumentBean> documentBeanList = new ArrayList<DocumentBean>();
		
		Folder patientFolder=null;
		try {
			patientFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, 0l, String.valueOf(patientBean.getPatientId()));
		} catch (PortalException e1) {
			_log.error(e1.getMessage(), e1);
		}
		
		if(Validator.isNotNull(patientFolder)){
		try {
			
			// Set LOP Requests
			Folder lopRequestFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "LOP Requests");
			List<DLFileEntry> lopRequestDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, lopRequestFolder.getFolderId());
			for(DLFileEntry fileEntry : lopRequestDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setLopRequestDocuments(documentBeanList);
			
			// Set LOP Documents
			Folder LOPFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "LOP");
			List<DLFileEntry> lopDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, LOPFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : lopDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setLopDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			// Set Invoice documents
			Folder invoiceFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "Invoice");
			List<DLFileEntry> invoiceDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, invoiceFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : invoiceDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setInvoiceDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			
			// Set Order documents
			Folder orderFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), "Order");
			List<DLFileEntry> orderDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, orderFolder.getFolderId());
			documentBeanList = new ArrayList<DocumentBean>();
			for(DLFileEntry fileEntry : orderDocuments){
				DocumentBean documentBean = new DocumentBean();
				documentBean.setTitle(fileEntry.getFileName());
				documentBean.setDownLoadURL(getDLFileURL(fileEntry));
				documentBeanList.add(documentBean);
			}
			patientBean.setOrderDocuments(documentBeanList);
			
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		
		try{
			// Procedure documents
			List<Appointment> patientCompleteAppoinmtnet = AppointmentLocalServiceUtil.getAppointmentsByStatusAndPatientId(patientBean.getPatientId(), AppointmentStatus.TECHNOLOGIST_REPORT_SUBMITTED.getValue());
			documentBeanList = new ArrayList<DocumentBean>();
			for(Appointment appointment : patientCompleteAppoinmtnet){
				Folder appointmentFolder = DLAppLocalServiceUtil.getFolder(companyGroupId, patientFolder.getFolderId(), appointment.getAppointmentId()+StringPool.UNDERLINE+appointment.getPatientId());
				List<DLFileEntry> appointmentDocuments = DLFileEntryLocalServiceUtil.getFileEntries(companyGroupId, appointmentFolder.getFolderId());
				documentBeanList = new ArrayList<DocumentBean>();
				for(DLFileEntry fileEntry : appointmentDocuments){
					DocumentBean documentBean = new DocumentBean();
					documentBean.setTitle(fileEntry.getFileName());
					documentBean.setDownLoadURL(getDLFileURL(fileEntry));
					documentBeanList.add(documentBean);
				}
				
			}
			patientBean.setProcedureDocumnts(documentBeanList);
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}	
	  }
	}
}