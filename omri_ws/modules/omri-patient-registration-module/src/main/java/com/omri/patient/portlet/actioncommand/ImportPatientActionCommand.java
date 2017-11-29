package com.omri.patient.portlet.actioncommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.service.common.exception.NoSuchCustomUserException;
import com.omri.service.common.exception.NoSuchPatientException;
import com.omri.service.common.exception.NoSuchResourceException;
import com.omri.service.common.exception.NoSuchSpecificationException;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.OMRICommonLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.util.OMRIConstants;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/import_patients"
	    },
	    service = MVCActionCommand.class
	)
public class ImportPatientActionCommand extends BaseMVCActionCommand{

	Log LOG = LogFactoryUtil.getLog(ImportPatientActionCommand.class.getName());
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
			
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		File file = uploadRequest.getFile("importPatientFile");
		String sourceFileName =uploadRequest.getFileName("importPatientFile");
		String fileExtension = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
		if(Validator.isNotNull(file)){
			if(fileExtension.equals("xlsx")|| fileExtension.equals("xls")){
				uploadPatients(actionRequest, actionResponse,file);
			}
		}else{
			SessionErrors.add(actionRequest, "not-valid-file");
		}
	}

	private void uploadPatients(ActionRequest request, ActionResponse response, File file) throws IOException{
		 ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		 FileInputStream inputStream = new FileInputStream(file);
		 SimpleDateFormat df = new SimpleDateFormat("MM/DD/YYYY");
		 XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	     Sheet firstSheet = workbook.getSheetAt(0);
	     Iterator<Row> iterator = firstSheet.iterator();
	     List<String> unsuccessfullPatientList = new ArrayList<String>();
	     Map<String,Long> resourceMap = new HashMap<String,Long>();
	     Map<String,Long> specificationMap = new HashMap<String,Long>();
	     
	     if(iterator.hasNext()){
	    	  	Row row = iterator.next(); 
	        	boolean isValidFile = validateFile(row);
	        	if(!isValidFile){
	        		SessionErrors.add(request, "file-format-err");
	        		response.setRenderParameter("mvcRenderCommandName", "/import-patient");
	        		return;
	        	}
	      }
	     
	     int totalPatientCount = 0;
	     int successImportedPatientCount = 0;
	     while (iterator.hasNext()) {
	    	 Row nextRow = iterator.next();
	    	 if(nextRow.getRowNum()!=0 && Validator.isNotNull(nextRow.getCell(0).toString())){
	    		 totalPatientCount++;
	    		 boolean validRow = validateRow(nextRow);
	    		 if(validRow){
	    			 
	    			 String customPatientId = nextRow.getCell(0).getStringCellValue();
	    			
	    			 Patient patient = null;
	    			 Clinic clinic = null;
    			    try{
    			    	patient = PatientLocalServiceUtil.getPatientByCustomPatientId(customPatientId);
    			    }catch(PortalException e){
    			    	LOG.error(e.getMessage());
    			    }
    			    
    			    try{
    			    	String clinicName = nextRow.getCell(14).getStringCellValue();
    			    	clinic = ClinicLocalServiceUtil.getClinicByName(clinicName);
    			    }catch(PortalException e){
    			    	LOG.error(e.getMessage());
    			    }
	    			 
	    			 if(Validator.isNull(patient) && Validator.isNotNull(clinic)){    
	    			    
		    			 Date dos=null;
		    			 try {
		    	    		 Date cellDate  =  nextRow.getCell(1).getDateCellValue();
		    	    		 dos = cellDate;
		    			 } catch (Exception e) {
		    				LOG.error(e.getMessage());
		    			 }
		    			
		    			 String patientName = nextRow.getCell(2).getStringCellValue();
		    			 String patientFirstName = patientName.split(StringPool.SPACE)[0];
		    			 String patientLastName = StringPool.BLANK;
		    			 if(patientName.split(StringPool.SPACE).length>0){
		    				 patientLastName = patientName.split(StringPool.SPACE)[1];
		    		 	 }
		    			 
		    			 Date dob=null;
		    			 try {
		    	    		 Date cellDate  =  nextRow.getCell(3).getDateCellValue();
		    	    		 dob = cellDate;
		    			 } catch (Exception e) {
		    				LOG.error(e.getMessage());
		    			 }
		    			 
		    			 Cell phoneCell = nextRow.getCell(4);
		    			 phoneCell.setCellType(Cell.CELL_TYPE_STRING);
		    			 
		    			 Cell zipCell = nextRow.getCell(7);
		    			 zipCell.setCellType(Cell.CELL_TYPE_STRING);
		    			 
		    			 String phone = nextRow.getCell(4).getStringCellValue();
		    			 String address = nextRow.getCell(5).getStringCellValue();
		    			 String city = nextRow.getCell(6).getStringCellValue();
		    			 String zip = nextRow.getCell(7).getStringCellValue();
	
		    			 long resourceId = getResourceId(nextRow.getCell(8).getStringCellValue(), resourceMap);
		    			 long specificationId = getSpecificationId(nextRow.getCell(9).getStringCellValue(), specificationMap);
		    			 
		    			 String payee = nextRow.getCell(11).getStringCellValue();
		    			 
		    			 String doctorfirstName = nextRow.getCell(11).getStringCellValue();
		    			 String doctorlastName = nextRow.getCell(12).getStringCellValue();
		    			 
		    			 User dUser = OMRICommonLocalServiceUtil.getUserByFirstNameAndLastName(doctorfirstName, doctorlastName);
		    			 
		    			 String lawyerFirstName = nextRow.getCell(13).getStringCellValue();
		    			 String lawyerLastName = OMRIConstants.DEFAULT_USER_LASTNAME;
		    			
		    			 User lUser = OMRICommonLocalServiceUtil.getUserByFirstName(lawyerFirstName);
			    			
		    			 try {
							patient = PatientLocalServiceUtil.importPatient(customPatientId, dos, patientFirstName, patientLastName, dob,
									 phone, address, city, zip, clinic.getClinicId(), resourceId, specificationId, 
									 payee, dUser, lUser, themeDisplay.getUserId());
						} catch (NoSuchCustomUserException e) {
							LOG.error(e);
						}
		    			 
		    			 if(Validator.isNotNull(patient)){
		    				 successImportedPatientCount++;
		    			 }else{
		    				 unsuccessfullPatientList.add(nextRow.getCell(0).toString());
		    			 }
	    			 }else{
	    				 unsuccessfullPatientList.add(nextRow.getCell(0).toString());
	    			 }
	    			 
	    		 }else{
	    			 unsuccessfullPatientList.add(nextRow.getCell(0).toString());
	    		 }
	    	 }else{
	    		 //unsuccessfullPatientList.add(nextRow.getCell(0).toString());
	    	 }
	     }
	     
	     response.setRenderParameter("totalPatientCount", String.valueOf(totalPatientCount));
	     response.setRenderParameter("successImportedPatientCount", String.valueOf(successImportedPatientCount));
	     response.setRenderParameter("UnsuccessImportedPatientCount", String.valueOf(totalPatientCount-successImportedPatientCount));
	     response.setRenderParameter("isImported",String.valueOf(true));
	     SessionMessages.add(request, "patient-import-success");
	     
	     PortalCache portalCache =   MultiVMPoolUtil.getCache(Patient.class.getName());
    	 portalCache.put("unsuccessfullPatientList", unsuccessfullPatientList);

    	 response.setRenderParameter("mvcRenderCommandName", "/import-patient");
	     
	}
	
	public boolean validateFile(Row firstRow){
		 if(!firstRow.getCell(0).toString().trim().equalsIgnoreCase("Patint ID")){
			 return false;
		 }else if(!firstRow.getCell(1).toString().trim().equalsIgnoreCase("DOS")){
			 return false;
		 }else if(!firstRow.getCell(2).toString().trim().equalsIgnoreCase("Patient Name (First, Last)")){
			 return false;
		 }else if(!firstRow.getCell(3).toString().trim().equalsIgnoreCase("DOB")){
			 return false;
		 }else if(!firstRow.getCell(4).toString().trim().equalsIgnoreCase("Phone")){
			 return false;
		 }else if(!firstRow.getCell(5).toString().trim().equalsIgnoreCase("Street Address")){
			 return false;
		 }else if(!firstRow.getCell(6).toString().trim().equalsIgnoreCase("City")){
			 return false;
		 }else if(!firstRow.getCell(7).toString().trim().equalsIgnoreCase("Zipt")){
			 return false;
		 }else if(!firstRow.getCell(8).toString().trim().equalsIgnoreCase("Resource")){
			 return false;
		 }else if(!firstRow.getCell(9).toString().trim().equalsIgnoreCase("Specification")){
			 return false;
		 }else if(!firstRow.getCell(10).toString().trim().equalsIgnoreCase("Payer")){
			 return false;
		 }else if(!firstRow.getCell(11).toString().trim().equalsIgnoreCase("Doctor FirstName")){
			 return false;
		 }else if(!firstRow.getCell(12).toString().trim().equalsIgnoreCase("Doctor LastName")){
			 return false;
		 }else if(!firstRow.getCell(13).toString().trim().equalsIgnoreCase("Lawyer (First, Last)")){
			 return false;
		 }else if(!firstRow.getCell(14).toString().trim().equalsIgnoreCase("Clinic Name")){
			 return false;
		 }else{
			 return true;
		 }
	 }
	
	private boolean validateRow(Row row){
		if(Validator.isNull(row.getCell(0).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(1).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(2).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(3).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(4).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(5).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(6).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(7).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(8).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(9).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(10).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(11).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(12).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(13).toString().trim())){
			 return false;
		 }else if(Validator.isNull(row.getCell(14).toString().trim())){
			 return false;
		 }else{
			 return true;
		 }
	}
	
	private long getResourceId(String resourceName, Map<String, Long> resourceMap){
		long resourceId = 0;
		if(Validator.isNull(resourceMap.get(resourceName))){
		try {
			Resource resource =  ResourceLocalServiceUtil.getResourceByName(resourceName);
			resourceMap.put(resource.getResourceName(), resource.getResourceId());
			return resource.getResourceId();
		} catch (NoSuchResourceException e) {
			LOG.debug(e.getMessage());
		}
		}else{
		  return resourceMap.get(resourceName);
		}
		return resourceId;
	}
	
	private long getSpecificationId(String specificationName, Map<String, Long> specificationMap){
		long specificationId = 0;
		if(Validator.isNull(specificationMap.get(specificationName))){
		try {
			Specification specification = SpecificationLocalServiceUtil.getSpecificationByName(specificationName); 
			specificationMap.put(specification.getSpecificationName(), specification.getSpecificationId());
			return specification.getSpecificationId();
		} catch (NoSuchSpecificationException e) {
			LOG.debug(e.getMessage());
		}
		}else{
			return specificationMap.get(specificationName);
		}
		return specificationId;
	}

}
