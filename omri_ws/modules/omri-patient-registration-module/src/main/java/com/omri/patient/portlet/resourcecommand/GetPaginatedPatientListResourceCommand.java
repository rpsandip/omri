package com.omri.patient.portlet.resourcecommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
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
import com.omri.patient.portlet.util.PortletCommonUtil;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.beans.PatientResourceBean;
import com.omri.service.common.exception.NoSuchPatient_ClinicException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/getPaginatedPatientList"
	    },
	    service = MVCResourceCommand.class
	)
public class GetPaginatedPatientListResourceCommand implements MVCResourceCommand{
	Log LOG = LogFactoryUtil.getLog(GetPaginatedPatientListResourceCommand.class.getName());
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		JSONObject responseObj = JSONFactoryUtil.createJSONObject();
		String roleType = ParamUtil.getString(resourceRequest, "roleType");
		String searchKeyword = httpRequest.getParameter("search[value]");
		int start = Integer.parseInt(httpRequest.getParameter("start"));
		int length = Integer.parseInt(httpRequest.getParameter("length"));
		int totalPatients = 0;
		
		List<Patient> patientList = new ArrayList<Patient>();
	    if(roleType.equals("Admin")){
	    	patientList = PortletCommonUtil.getPatientList(start, start+length, searchKeyword,0l);
	    	 totalPatients = PortletCommonUtil.getPatientList(-1, -1, searchKeyword,0l).size();
	    }else{
	    	patientList = PortletCommonUtil.getPatientList(start, start+length, searchKeyword,themeDisplay.getUserId());
	    	 totalPatients = PortletCommonUtil.getPatientList(-1, -1, searchKeyword,themeDisplay.getUserId()).size();
	    }
	    
	    JSONArray dataArray = getPatientBeanList(patientList, roleType, themeDisplay.getUserId());
	    
	    responseObj.put("iTotalRecords", totalPatients);
	    responseObj.put("iTotalDisplayRecords", totalPatients);
	    responseObj.put("aaData", dataArray);
	    
	    try {
			resourceResponse.getWriter().write(responseObj.toString());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	    
		return true;
	}
	
	private JSONArray getPatientBeanList(List<Patient> patientList, String roleType, long creatorUserId){
		JSONArray dataArray = JSONFactoryUtil.createJSONArray();
		for(Patient patient : patientList){
			JSONObject patientObj = JSONFactoryUtil.createJSONObject();
			patientObj.put("firstName", patient.getFirstName());
			patientObj.put("lastName", patient.getLastName());
			patientObj.put("phoneNo", patient.getPhoneNo());
			patientObj.put("patientId", patient.getPatientId());
			patientObj.put("action", StringPool.BLANK);
			String procedureDetail = StringPool.BLANK;
			//List<PatientResourceBean> patientResourceBeanList = new ArrayList<PatientResourceBean>();
			Patient_Clinic patientClinc = null;
			try {
				if(roleType.equals("Admin")){
					patientClinc = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientId(patient.getPatientId());
				}else{
					patientClinc = Patient_ClinicLocalServiceUtil.getPatientClinicByPatientIdandCreatorUserId(patient.getPatientId(), creatorUserId);
				}
				if(Validator.isNotNull(patientClinc)){
					List<Patient_Clinic_Resource> patientClinicResourceList  = new ArrayList<Patient_Clinic_Resource>();
					if(roleType.equals("Admin")){
						patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patient.getPatientId(), patientClinc.getClinicId());
					}else{
						patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicIdAndCreateUserId(patient.getPatientId(), patientClinc.getClinicId(), creatorUserId);
					}
					for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
						try {
							//Clinic clinic = ClinicLocalServiceUtil.getClinic(patientClinc.getClinicId());
							Resource resource = ResourceLocalServiceUtil.getResource(patientClinicResource.getResourceId());
							Specification specification = SpecificationLocalServiceUtil.getSpecification(patientClinicResource.getSpecificationId());
							//PatientResourceBean patientResourceBean = new PatientResourceBean(patient, clinic, resource, specification,patientClinicResource);
							//patientResourceBeanList.add(patientResourceBean);
							procedureDetail+= resource.getResourceName()+StringPool.OPEN_PARENTHESIS+specification.getSpecificationName()+ StringPool.CLOSE_PARENTHESIS+
									StringPool.SPACE+ StringPool.COLON + StringPool.SPACE +  patientClinicResource.getNoOfOccurance() + StringPool.TILDE;
							
						} catch (PortalException e) {
							LOG.error(e.getMessage());
						}
					}
					patientObj.put("procedure", procedureDetail);
				}
				
			} catch (NoSuchPatient_ClinicException e) {
				LOG.error(e.getMessage(), e);
			}
			dataArray.put(patientObj);
		}
		return dataArray;
	}

}
