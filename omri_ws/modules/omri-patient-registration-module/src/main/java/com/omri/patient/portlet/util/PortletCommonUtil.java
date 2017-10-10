package com.omri.patient.portlet.util;

import java.util.List;

import com.omri.service.common.model.Patient;
import com.omri.service.common.service.PatientLocalServiceUtil;

public class PortletCommonUtil {
	public static List<Patient> getPatientList(int start, int end, String keyword,long createdByUserId){
		List<Patient> patientList = PatientLocalServiceUtil.getPatientList(start, end, keyword,createdByUserId);
		return patientList;
	}
}
