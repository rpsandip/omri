package com.omri.patient.portlet.actioncommand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.patient.portlet.resourcecommand.GetClinicResourcesResourceCommand;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.util.PatientStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/user/create_patient"
	    },
	    service = MVCActionCommand.class
	)
public class CreatePatientActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(CreatePatientActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {
			Patient patient = addPatient(actionRequest);
			if(Validator.isNotNull(patient)){
				Patient_Clinic patientClinic = addPatientClinic(actionRequest, patient);
				if(Validator.isNotNull(patientClinic)){
					addPatientClinicResource(actionRequest, patientClinic);
				}
			}
		} catch (ParseException e) {
			_log.error(e.getMessage(), e);
		}
	}
	
	private Patient addPatient(ActionRequest actionRequest) throws ParseException{
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		String phoneNo = ParamUtil.getString(actionRequest, "mobile");
		String birthDate = ParamUtil.getString(actionRequest, "birthDate");
		String address1 = ParamUtil.getString(actionRequest, "address1");
		String address2 = ParamUtil.getString(actionRequest, "address2");
		String city = ParamUtil.getString(actionRequest, "city");
		String state = ParamUtil.getString(actionRequest, "state");
		String country = ParamUtil.getString(actionRequest, "country");
		String zip = ParamUtil.getString(actionRequest, "zip");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dob = dateFormat.parse(birthDate);
		Patient patient = PatientLocalServiceUtil.createPatient(firstName, lastName, dob, phoneNo,address1, address2, city, state, country, zip, themeDisplay.getUserId(), themeDisplay.getUserId());
		return patient;
	}

	private Patient_Clinic addPatientClinic(ActionRequest actionRequest, Patient patient){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long  doctorId = ParamUtil.getLong(actionRequest, "doctor");
		long clinicId = ParamUtil.getLong(actionRequest, "clinic");
		Patient_Clinic patientClinic = null;
		try {
			User user = UserLocalServiceUtil.getUser(doctorId);
			String doctorPhonNo = ParamUtil.getString(actionRequest, "doctorPhone");
			patientClinic = Patient_ClinicLocalServiceUtil.addPatient_Clinic(patient.getPatientId(), clinicId, doctorId, user.getFirstName()+StringPool.BLANK+user.getLastName(), doctorPhonNo, PatientStatus.REFERRAL_RECEIVED.getValue(),themeDisplay.getUserId(), themeDisplay.getUserId());
			
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
		return patientClinic;
	}
	
	private void addPatientClinicResource(ActionRequest actionRequest,Patient_Clinic patientClinic){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		int resourcesCounts =  ParamUtil.getInteger(actionRequest, "resourceCount");
		resourcesCounts++;
		for (int i=0; i<=resourcesCounts;i++) {
			long resourceId = ParamUtil.getLong(actionRequest, "resource" + i);
			long specificationId = ParamUtil.getLong(actionRequest, "specification" + i);
			int occurance = ParamUtil.getInteger(actionRequest, "occurance"+i);
			if(resourceId!=0 && specificationId!=0){
				Patient_Clinic_ResourceLocalServiceUtil.addPatientClinicResource(patientClinic.getPatientId(), resourceId, patientClinic.getClinicId(), specificationId, occurance, themeDisplay.getUserId(), themeDisplay.getUserId() );
			}
		}
	}
}
