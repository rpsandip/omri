package com.omri.portlet.actioncommand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.PatientDetail;
import com.omri.service.common.service.PatientDetailLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriAppointmentPortletmvcportletPortlet",
	        "mvc.command.name=/addPatientMoreDetail"
	    },
	    service = MVCActionCommand.class
	)
public class AddPatientMoreDetailActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(AddPatientMoreDetailActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		long patientId = ParamUtil.getLong(actionRequest, "patientId");
		try {
			Patient patient = PatientLocalServiceUtil.getPatient(patientId);
			String firstName = ParamUtil.getString(actionRequest, "firstName");
			String lastName = ParamUtil.getString(actionRequest, "lastName");
			String phoneNo = ParamUtil.getString(actionRequest, "phoneNo");
			String patientDOB = ParamUtil.getString(actionRequest, "patientDOB");
			String address1 = ParamUtil.getString(actionRequest, "address1");
			String address2 = ParamUtil.getString(actionRequest, "address2");
			String city = ParamUtil.getString(actionRequest, "city");
			String state = ParamUtil.getString(actionRequest, "state");
			String country = ParamUtil.getString(actionRequest, "country");
			String zip = ParamUtil.getString(actionRequest, "zip");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			try {
				Date dob = dateFormat.parse(patientDOB);
				patient.setDob(dob);
				patient.setFirstName(firstName);
				patient.setLastName(lastName);
				patient.setPhoneNo(phoneNo);
				patient.setAddressLine1(address1);
				patient.setAddressLine2(address2);
				patient.setCity(city);
				patient.setState(state);
				patient.setCountry(country);
				patient.setZip(zip);

				PatientLocalServiceUtil.updatePatient(patient);
				addPatientMoreDetail(actionRequest, patientId);
				SessionMessages.add(actionRequest, "patient-updated-sucessfully");
			} catch (ParseException e) {
				_log.error(e.getMessage(), e);
			}
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
	}
	
	private void addPatientMoreDetail(ActionRequest actionRequest,long patientId){
			
		    boolean mriBefore = ParamUtil.getBoolean(actionRequest, "mriBefore");
			boolean claustrophobic = ParamUtil.getBoolean(actionRequest, "claustrophobic");
			boolean lbs300 = ParamUtil.getBoolean(actionRequest, "300lbs");
			boolean prevPatient = ParamUtil.getBoolean(actionRequest, "prevPatient");
			boolean metalInBody = ParamUtil.getBoolean(actionRequest, "metalInBody");
			boolean pacemaker = ParamUtil.getBoolean(actionRequest, "pacemaker");
			boolean priorSurgery = ParamUtil.getBoolean(actionRequest, "priorSurgery");
			boolean chanceOfPregent = ParamUtil.getBoolean(actionRequest, "chanceOfPregent");
			boolean overAgeof60 = ParamUtil.getBoolean(actionRequest, "overAgeof60");
			boolean labsDone = ParamUtil.getBoolean(actionRequest, "labsDone");
			boolean allergic = ParamUtil.getBoolean(actionRequest, "allergic");
			boolean diabetic = ParamUtil.getBoolean(actionRequest, "diabetic");
			boolean hypertension = ParamUtil.getBoolean(actionRequest, "hypertension");
			boolean cancer = ParamUtil.getBoolean(actionRequest, "cancer");
			boolean allergicToIdodine = ParamUtil.getBoolean(actionRequest, "allergicToIdodine");
			boolean bloodthinners = ParamUtil.getBoolean(actionRequest, "bloodthinners");
			
			String claustrophobicDetail = ParamUtil.getString(actionRequest, "claustrophobicDetail");
			String pacemakerDetail = ParamUtil.getString(actionRequest, "pacemakerDetail");
			String metalInBodyDetail = ParamUtil.getString(actionRequest, "metalInBodyDetail");
			String priorSurgeryDetail = ParamUtil.getString(actionRequest, "priorSurgeryDetail");
			String alergicDetail = ParamUtil.getString(actionRequest, "alergicDetail");
			String diabeticDetail = ParamUtil.getString(actionRequest, "diabeticDetail");
			String allergicToIdodineDetail = ParamUtil.getString(actionRequest, "allergicToIdodineDetail");
			String bloodthinnersDetail = ParamUtil.getString(actionRequest, "bloodthinnersDetail");
			
			String detailWithTimeStamp = ParamUtil.getString(actionRequest, "detailWithTimeStamp");
			
			PatientDetailLocalServiceUtil.addPatientMoreDetail(patientId, mriBefore, claustrophobic, lbs300, prevPatient, pacemaker,metalInBody, priorSurgery,
					chanceOfPregent, overAgeof60, labsDone, allergic, diabetic, hypertension, cancer, allergicToIdodine, bloodthinners,
					claustrophobicDetail, pacemakerDetail, metalInBodyDetail, priorSurgeryDetail, alergicDetail, diabeticDetail, 
					allergicToIdodineDetail, bloodthinnersDetail, detailWithTimeStamp);
	 }
}
