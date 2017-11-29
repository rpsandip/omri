package com.omri.portlet.resourcecommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.portlet.util.ReportFileUtil;
import com.omri.service.common.beans.PatientBean;
import com.omri.service.common.exception.NoSuchClinicException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet",
	        "mvc.command.name=export-patients"
	    },
	    service = MVCResourceCommand.class
	)
public class ExportPatientResourceCommand implements MVCResourceCommand{

	Log _log = LogFactoryUtil.getLog(ExportPatientResourceCommand.class.getName());
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
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
				doctorId, lawyerId, searchByCreatedDate, themeDisplay.getUserId(),-1, -1);
		
		List<PatientBean> patientBeanList = new ArrayList<PatientBean>();
		
		for(Patient patient : patientList){
			PatientBean patientBean = OMRICommonLocalServiceUtil.getPatientBean(patient);
			OMRICommonLocalServiceUtil.setPatientDocuments(themeDisplay.getCompanyGroupId(), patientBean);
			if(Validator.isNotNull(patient)){
				patientBeanList.add(patientBean);	
			}
		}

		try {
        	resourceResponse.setContentType("application/vnd.ms-excel");
        	resourceResponse.addProperty(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=Patient_Reports.xlsx");

        	File file = ReportFileUtil.generateReport(patientBeanList);
        	
            OutputStream pos = resourceResponse.getPortletOutputStream();
            try {
            	byte[] bytesArray = new byte[(int) file.length()];
            	FileInputStream fis = new FileInputStream(file);
            	fis.read(bytesArray); //read file into bytes[]
            	fis.close();

                pos.write(bytesArray);
                pos.flush();
            } finally {
                pos.close();
            }
        } catch(IOException e){
        	_log.error(e);
        }
		
		
		return true;
	}

}
