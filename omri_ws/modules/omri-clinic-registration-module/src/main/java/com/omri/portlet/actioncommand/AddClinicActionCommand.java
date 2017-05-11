package com.omri.portlet.actioncommand;

import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;


@Component(
	    property = {
	    	"javax.portlet.name=com_omri_portlet_OmriClinicRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/clinic/add_clinic"
	    },
	    service = MVCActionCommand.class
	)
public class AddClinicActionCommand extends BaseMVCActionCommand{
	private static Log _log = LogFactoryUtil.getLog(AddClinicActionCommand.class);
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {
		Clinic clinic = null;
		Organization clinicOrg = null;
		try {
			 clinic = addClinicData(actionRequest, actionResponse);
			 clinicOrg = createClinicOrganization(actionRequest, actionResponse);
			 updateClinicToSetLROrganization(clinic,clinicOrg);
			 addClinicResources(actionRequest, actionResponse,clinic);
			 SessionMessages.add(actionRequest, "clinc.added.sucessfully");
		} catch (Exception e) {
			if(Validator.isNotNull(clinic)){
				ClinicLocalServiceUtil.deleteClinic(clinic);
			}
			if(Validator.isNotNull(clinicOrg)){
				try {
					OrganizationLocalServiceUtil.deleteOrganization(clinicOrg);
				} catch (PortalException e1) {
					_log.error(e1.getMessage(), e1);
				}
			}
		}
		
	}
	
	private Clinic addClinicData(ActionRequest actionRequest, ActionResponse actionResponse){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String clinicName = ParamUtil.getString(actionRequest, "clinicName");
		String addressLine1 = ParamUtil.getString(actionRequest, "addressLine1");
		String addressLine2 = ParamUtil.getString(actionRequest, "addressLine2");
		String city = ParamUtil.getString(actionRequest, "city");
		String state = ParamUtil.getString(actionRequest, "state");
		String zip = ParamUtil.getString(actionRequest, "zip");
		String phone = ParamUtil.getString(actionRequest, "phone");
		String fax = ParamUtil.getString(actionRequest, "fax");
		
		return ClinicLocalServiceUtil.addClinic(clinicName, addressLine1, addressLine2, city, state, zip, phone, fax, themeDisplay.getUserId(), themeDisplay.getUserId());
		
	}

	private Organization createClinicOrganization(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException{
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		return OrganizationLocalServiceUtil.addOrganization(themeDisplay.getUserId(), 0l, ParamUtil.getString(actionRequest, "clinicName"), true);
	}
	
	private void updateClinicToSetLROrganization(Clinic clinic, Organization clinicOrg) throws Exception{
		if(Validator.isNotNull(clinic) && Validator.isNotNull(clinicOrg)){
			clinic.setClinicorganizationId(clinicOrg.getOrganizationId());
			clinic.setClinicorganizationGroupId(clinicOrg.getGroupId());
			ClinicLocalServiceUtil.updateClinic(clinic);
		}else{
			throw new Exception();
		}
	}
	
	private void addClinicResources(ActionRequest actionRequest, ActionResponse actionResponse, Clinic clinic){
		int resourcesCounts =  ParamUtil.getInteger(actionRequest, "resourceCount");
		resourcesCounts++;
		//int[] resourcesIndexes = StringUtil.split(resourcesIndexString, 0);
		for (int i=0; i<=resourcesCounts;i++) {
			long resourceId = ParamUtil.getLong(actionRequest, "resource" + i);
			long specificationId = ParamUtil.getLong(actionRequest, "specification" + i);
			int operationTime = ParamUtil.getInteger(actionRequest, "operationTime"+i);
			int price = ParamUtil.getInteger(actionRequest, "price"+i);
			if(resourceId!=0 && specificationId!=0 && operationTime!=0){
				Clinic_ResourceLocalServiceUtil.addClinic_Resource(clinic.getClinicId(), resourceId, specificationId, operationTime, price);
			}
		}
	}
}
