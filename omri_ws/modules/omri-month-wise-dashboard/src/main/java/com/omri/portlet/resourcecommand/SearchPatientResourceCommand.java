package com.omri.portlet.resourcecommand;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.beans.DocumentBean;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.util.PatientDocumentStatus;
import com.omri.service.common.util.PatientStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet",
	        "mvc.command.name=getPatientReports"
	    },
	    service = MVCResourceCommand.class
	)
public class SearchPatientResourceCommand implements MVCResourceCommand{

	Log _log = LogFactoryUtil.getLog(SearchPatientResourceCommand.class.getName());
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		JSONObject responseObj = JSONFactoryUtil.createJSONObject();
		JSONArray dataArray = JSONFactoryUtil.createJSONArray();
		
		HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		int start = Integer.parseInt(httpRequest.getParameter("start"));
		int length = Integer.parseInt(httpRequest.getParameter("length"));
		String startDateStr = ParamUtil.getString(resourceRequest, "startDate");
		String endDateStr = ParamUtil.getString(resourceRequest, "endDate");
		String payee = ParamUtil.getString(resourceRequest, "payee");
		long doctorId = ParamUtil.getLong(resourceRequest, "doctorId");
		long lawyerId = ParamUtil.getLong(resourceRequest, "lawyerId");
		boolean isClinicAdmin = ParamUtil.getBoolean(resourceRequest, "isClinicAdmin");
		boolean isLawyerAdmin = ParamUtil.getBoolean(resourceRequest, "isLawyerAdmin");
		boolean isDoctorAdmin = ParamUtil.getBoolean(resourceRequest, "isDoctorAdmin");
		
		Date startDate= null;
		Date endDate = null;
		try {
			startDate = df.parse(startDateStr);
			endDate = df.parse(endDateStr);
		} catch (ParseException e) {
			_log.error(e.getMessage());
		}
		
		long clinicId=0;
		if(isClinicAdmin){
			long clinicOrgId = OMRICommonLocalServiceUtil.getUserAssociatedOrgId(themeDisplay.getUserId());
			try {
				Clinic clinic = ClinicLocalServiceUtil.getClinicByClinicOrganizationId(clinicOrgId);
				clinicId = clinic.getClinicId();
			} catch (NoSuchClinicException e) {
				_log.error(e.getMessage());
			}
		}
		boolean searchByCreatedDate = false;
		if(isDoctorAdmin || isLawyerAdmin){
			searchByCreatedDate = true;
		}
		
		List<Patient> patientList = PatientLocalServiceUtil.searchPatient(clinicId, startDate, endDate, payee, 
				doctorId, lawyerId, searchByCreatedDate, themeDisplay.getUserId(),start, start+length);
		
		int totalPatients = PatientLocalServiceUtil.searchPatientCount(clinicId, startDate, endDate, payee, doctorId, lawyerId,
				searchByCreatedDate, themeDisplay.getUserId());
		
		for(Patient patient : patientList){
			PatientBean patientBean = OMRICommonLocalServiceUtil.getPatientBean(patient);
			OMRICommonLocalServiceUtil.setPatientDocuments(themeDisplay.getCompanyGroupId(), patientBean);
			if(Validator.isNotNull(patient)){
				JSONObject patientJsonObject = JSONFactoryUtil.createJSONObject();
				
				patientJsonObject.put("patientId", patient.getPatientId());
				patientJsonObject.put("name", patient.getFirstName()+StringPool.SPACE+patient.getLastName());
				patientJsonObject.put("customPatientId", patient.getCustomPatientId());
				patientJsonObject.put("clinicId", patientBean.getPatientClinic().getClinicId());
				patientJsonObject.put("dos", df.format(patient.getDos()));
				patientJsonObject.put("payee", patient.getPayee());
				patientJsonObject.put("clinic", patientBean.getClinicName());
				patientJsonObject.put("resources", getResourceDetail(patientBean.getResourceBeanList()));
				patientJsonObject.put("status", PatientStatus.findByValue(patientBean.getPatientClinic().getPatient_status()).getLabel());
				patientJsonObject.put("lop", getDocumentListDetail(patientBean.getLopDocuments()));
				patientJsonObject.put("lopRequest", getDocumentListDetail(patientBean.getLopRequestDocuments()));
				patientJsonObject.put("invoice", getDocumentListDetail(patientBean.getInvoiceDocuments()));
				patientJsonObject.put("order", getDocumentListDetail(patientBean.getOrderDocuments()));
				patientJsonObject.put("procedure",getDocumentListDetail(patientBean.getProcedureDocumnts()));
				if(patientBean.getPatientClinic().getDocumentStatus()==PatientDocumentStatus.APPROVED.getValue()){
					patientJsonObject.put("documentStatus", PatientDocumentStatus.APPROVED.getLabel());
				}else{
					patientJsonObject.put("documentStatus", "Verify");
				}
				
				dataArray.put(patientJsonObject);
			}
		}
		
		responseObj.put("iTotalRecords", totalPatients);
		responseObj.put("iTotalDisplayRecords", totalPatients);
		responseObj.put("aaData", dataArray);
		    
		 try {
				resourceResponse.getWriter().write(responseObj.toString());
			} catch (IOException e) {
				_log.error(e.getMessage(), e);
		 }
		
		return true;
	}

	private String getResourceDetail(List<PatientResourceBean> resourceBeanList){
		String resourceInfo = StringPool.BLANK;
		for(PatientResourceBean patientResourceBean : resourceBeanList){
			resourceInfo+=patientResourceBean.getResourceName()+StringPool.SPACE+
					StringPool.OPEN_PARENTHESIS+patientResourceBean.getSpecificationName()+StringPool.CLOSE_PARENTHESIS+
					StringPool.SPACE + StringPool.COLON + patientResourceBean.getOccurnace()+StringPool.COMMA;
		}
		return resourceInfo;
	}
	
	private String getDocumentListDetail(List<DocumentBean> documents){
		String documentURL = StringPool.BLANK;
		for(DocumentBean documentBean : documents){
			documentURL+= documentBean.getDownLoadURL()+StringPool.COMMA;
		}
		return documentURL;
		
	}
}
