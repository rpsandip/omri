package com.omri.patient.portlet.actioncommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.spi.IIOServiceProvider;
import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.bouncycastle.jce.provider.JDKPSSSigner.PSSwithRSA;
import org.osgi.service.component.annotations.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.exception.DuplicateFileException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.mail.kernel.model.FileAttachment;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.omri.patient.portlet.resourcecommand.GetClinicResourcesResourceCommand;
import com.omri.service.common.model.Clinic;
import com.omri.service.common.model.Patient;
import com.omri.service.common.model.Patient_Clinic;
import com.omri.service.common.model.Patient_Clinic_Resource;
import com.omri.service.common.model.Procedure;
import com.omri.service.common.model.Resource;
import com.omri.service.common.model.Specification;
import com.omri.service.common.service.ClinicLocalServiceUtil;
import com.omri.service.common.service.Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.PatientLocalServiceUtil;
import com.omri.service.common.service.Patient_ClinicLocalServiceUtil;
import com.omri.service.common.service.Patient_Clinic_ResourceLocalServiceUtil;
import com.omri.service.common.service.ProcedureLocalServiceUtil;
import com.omri.service.common.service.ResourceLocalService;
import com.omri.service.common.service.ResourceLocalServiceUtil;
import com.omri.service.common.service.SpecificationLocalServiceUtil;
import com.omri.service.common.util.PatientStatus;

@Component(
	    property = {
	    	"javax.portlet.name=com_omri_patient_portlet_OmriPatientRegistrationModulemvcportletPortlet",
	        "mvc.command.name=/user/create_patient"
	    },
	    service = MVCActionCommand.class
	)
public class CreatePatientActionCommand extends BaseMVCActionCommand{
	private static Log LOG = LogFactoryUtil.getLog(CreatePatientActionCommand.class);
	private static long PARENT_FOLDER_ID = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	private static String FROM_EMAIL = "no-reply@omri.com";
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String procedureDetail = StringPool.BLANK;
		boolean isEdit = false;
		long patientId = ParamUtil.getLong(actionRequest, "patientId");
		if(patientId>0){
			isEdit = true;
		}
		try {
			Patient patient = addUpdatePatient(actionRequest,isEdit);
			if(Validator.isNotNull(patient)){
				Patient_Clinic patientClinic = addUpdatePatientClinic(actionRequest, patient,isEdit);
				Procedure procedure = null;
				if(!isEdit){
					procedure = addPatientProcedure(patientClinic);
				}else{
					procedure = ProcedureLocalServiceUtil.getProcedureByPatientIdAndClinicId(patientClinic.getPatientId(), patientClinic.getClinicId());
				}
				if(Validator.isNotNull(patientClinic)){
					if(!isEdit){
						procedureDetail = addPatientClinicResource(actionRequest, patientClinic, procedure.getProcedureId());
					}else{
						deleteExistingPatientClinicResource(patientClinic);
						procedureDetail = addPatientClinicResource(actionRequest, patientClinic, procedure.getProcedureId());
					}
				}
				
			    addPatientDocuments(actionRequest,patient);
			    if(!isEdit){
				    try {
							generateLOPRequest(actionRequest,patient,procedureDetail);
					} catch (FileNotFoundException e) {
						LOG.error(e.getMessage(), e);
					}catch (IOException e) {
						LOG.error(e.getMessage(), e);
					}
			    }
			    if(!isEdit){
			    	SessionMessages.add(actionRequest, "patient.added.successfully");
			    }else{
			    	SessionMessages.add(actionRequest, "patient.updated.successfully");
			    }
			}
		} catch (ParseException | PortalException e) {
			actionResponse.setRenderParameter("mvcPath", "/user/create_user.jsp");
			LOG.error(e.getMessage(), e);
		}
	}
	
	private Patient addUpdatePatient(ActionRequest actionRequest, boolean isEdit) throws ParseException{
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
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
		String cptCode = ParamUtil.getString(actionRequest, "cptCode");
		String lopNotes = ParamUtil.getString(actionRequest, "lopNotes");
		String orderNotes = ParamUtil.getString(actionRequest, "orderNotes");
		String invoiceNotes = ParamUtil.getString(actionRequest, "invoiceNotes");
		String otherNotes = ParamUtil.getString(actionRequest, "otherNotes");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dob = dateFormat.parse(patientDOB);
		if(!isEdit){
			Patient patient = PatientLocalServiceUtil.createPatient(firstName, lastName, cptCode,dob, phoneNo,address1, address2, city, state, country, zip, lopNotes, orderNotes,invoiceNotes,otherNotes,themeDisplay.getUserId(), themeDisplay.getUserId());
			return patient;
		}else{
			long patientId = ParamUtil.getLong(actionRequest, "patientId");
			Patient patient = PatientLocalServiceUtil.updatePatient(patientId, firstName, lastName, cptCode, dob, phoneNo, address1, address2, city, state, country, zip, lopNotes, orderNotes, invoiceNotes, otherNotes, themeDisplay.getUserId());
			return patient;
		}
	}

	private Patient_Clinic addUpdatePatientClinic(ActionRequest actionRequest, Patient patient, boolean isEdit){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long  doctorId = ParamUtil.getLong(actionRequest, "doctor");
		long  lawyerId = ParamUtil.getLong(actionRequest, "lawyer");
		long clinicId = ParamUtil.getLong(actionRequest, "clinic");
		int status = ParamUtil.getInteger(actionRequest, "patient_status");
		Patient_Clinic patientClinic = null;
		try {
			User doctorUser = UserLocalServiceUtil.getUser(doctorId);
			String doctorPhonNo = ParamUtil.getString(actionRequest, "doctorPhone");
			
			User lawyerUser = UserLocalServiceUtil.getUser(lawyerId);
			String lawyerPhoneNo = ParamUtil.getString(actionRequest, "lawyerPhone");
			
			if(!isEdit){
				patientClinic = Patient_ClinicLocalServiceUtil.addPatient_Clinic(patient.getPatientId(), clinicId, doctorId, doctorUser.getFirstName()+StringPool.BLANK+doctorUser.getLastName(), doctorPhonNo, lawyerId, lawyerUser.getFirstName()+StringPool.BLANK+lawyerUser.getLastName(), lawyerPhoneNo,status,themeDisplay.getUserId(), themeDisplay.getUserId());
			}else{
				patientClinic = Patient_ClinicLocalServiceUtil.updatePatient_Clinic(patient.getPatientId(), clinicId, doctorId, doctorUser.getFirstName()+StringPool.BLANK+doctorUser.getLastName(), doctorPhonNo, lawyerId, lawyerUser.getFirstName()+StringPool.BLANK+lawyerUser.getLastName(), lawyerPhoneNo, themeDisplay.getUserId());
			}
		} catch (PortalException e) {
			LOG.error(e.getMessage(), e);
		}
		return patientClinic;
	}
	
	private Procedure addPatientProcedure(Patient_Clinic patientClinic){
		return ProcedureLocalServiceUtil.addPatientProcedure(patientClinic.getPatientId(), patientClinic.getClinicId());
	}
	private String addPatientClinicResource(ActionRequest actionRequest,Patient_Clinic patientClinic, long procedureId){
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		int resourcesCounts =  ParamUtil.getInteger(actionRequest, "resourceCount");
		resourcesCounts++;
		String procedureDetail= StringPool.BLANK;
		for (int i=1; i<=resourcesCounts;i++) {
			long resourceId = ParamUtil.getLong(actionRequest, "resource" + i);
			long specificationId = ParamUtil.getLong(actionRequest, "specification" + i);
			int occurance = ParamUtil.getInteger(actionRequest, "occurance"+i);
			try{
				Resource res = ResourceLocalServiceUtil.getResource(resourceId);
				Specification spec = SpecificationLocalServiceUtil.getSpecification(specificationId);
				procedureDetail+=res.getResourceName()+"("+spec.getSpecificationName()+")"+",";				
			}catch(PortalException e){
				LOG.error(e.getMessage(), e);
			}
			if(resourceId!=0 && specificationId!=0){
				try{
					Patient_Clinic_ResourceLocalServiceUtil.addPatientClinicResource(patientClinic.getPatientId(), resourceId, patientClinic.getClinicId(), specificationId, procedureId,occurance, themeDisplay.getUserId(), themeDisplay.getUserId());
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
				}
			}
		}
		return procedureDetail;
	}
	
	private void deleteExistingPatientClinicResource(Patient_Clinic patientClinic){
		List<Patient_Clinic_Resource> patientClinicResourceList = Patient_Clinic_ResourceLocalServiceUtil.getPatientClinicByPatiendIdAndClinicId(patientClinic.getPatientId(), patientClinic.getClinicId());
		for(Patient_Clinic_Resource patientClinicResource : patientClinicResourceList){
			Patient_Clinic_ResourceLocalServiceUtil.deletePatient_Clinic_Resource(patientClinicResource);
		}
	}
	
	private void addPatientDocuments(ActionRequest actionRequest,Patient patient){
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Folder patientFolder = getFolder(actionRequest, PARENT_FOLDER_ID,String.valueOf(patient.getPatientId()));
		if(Validator.isNotNull(patientFolder)){
			fileUpload(actionRequest, patient, patientFolder,"lop");
			fileUpload(actionRequest, patient, patientFolder,"order");
			fileUpload(actionRequest, patient, patientFolder,"invoice");
			fileUpload(actionRequest, patient, patientFolder,"other");
		}
	}
	
	private Folder getFolder(ActionRequest actionRequest, long parentFolderId,String folderName) { 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean defaultFolderExist = isFolderExist(themeDisplay,parentFolderId,String.valueOf(folderName)); 
		Folder patientFolder=null;
		if (!defaultFolderExist) {
			long repositoryId = themeDisplay.getCompanyGroupId();
			try {
				ServiceContext serviceContext =   ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest); 
				patientFolder = DLAppServiceUtil.addFolder(repositoryId,parentFolderId, folderName,"", serviceContext);
			} catch (PortalException e1) { 
				LOG.error(e1.getMessage(), e1);
			} catch (SystemException e1) {
				LOG.error(e1.getMessage(), e1);
			}	
		}else{
			try {
				patientFolder =	DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), parentFolderId, folderName);
			} catch (PortalException e1) {
				LOG.error(e1.getMessage(), e1);
			}
		}
		return patientFolder;
	}
	
	private void fileUpload(ActionRequest actionRequest, Patient patient, Folder parentFolder ,String fileType){ 
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		String fileName="";
		File file = null; 
		String mimeType = "";
		String title = "";
		String description = ""; 
		long repositoryId = themeDisplay.getCompanyGroupId();
		Folder folder = null;
		try { 
			if(fileType.equals("lop")){
				fileName=uploadPortletRequest.getFileName("lopDocument");
				file = uploadPortletRequest.getFile("lopDocument"); 
				mimeType = uploadPortletRequest.getContentType("lopDocument");
				title = fileName;
				description = "Patient LOP Document";
				folder = getFolder(actionRequest, parentFolder.getFolderId(),"LOP"); 
			}
			if(fileType.equals("order")){
				fileName=uploadPortletRequest.getFileName("orderDocument");
				file = uploadPortletRequest.getFile("orderDocument"); 
				mimeType = uploadPortletRequest.getContentType("orderDocument");
				title = fileName;
				description = "Patient Order Document";
				folder = getFolder(actionRequest, parentFolder.getFolderId(),"Order"); 
			}
			if(fileType.equals("invoice")){
				fileName=uploadPortletRequest.getFileName("invoiceDocument");
				file = uploadPortletRequest.getFile("invoiceDocument"); 
				mimeType = uploadPortletRequest.getContentType("invoiceDocument");
				title = fileName;
				description = "Patient Invoice Document";
				folder = getFolder(actionRequest, parentFolder.getFolderId(),"Invoice"); 
			}
			if(fileType.equals("other")){
				fileName=uploadPortletRequest.getFileName("otherDocument");
				file = uploadPortletRequest.getFile("otherDocument"); 
				mimeType = uploadPortletRequest.getContentType("otherDocument");
				title = fileName;
				description = "Patient Other Document";
				folder = parentFolder; 
			}
			if(Validator.isNotNull(fileName) && Validator.isNotNull(folder)){
				ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest); 
				DLAppServiceUtil.addFileEntry(repositoryId, folder.getFolderId(), fileName, MimeTypesUtil.getContentType(file), title, description, "", file, serviceContext); 
			}
			} catch (Exception e) {
				System.out.println(e.getMessage()); e.printStackTrace();
			} 
			
	  }
	
	
	private void generateLOPRequest(ActionRequest actionRequest, Patient patient, String procedureDetail) throws IOException{
		Document document = new Document();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String doctorName= StringPool.BLANK;
		String lawyerName=StringPool.BLANK;
		String doctorEmail= StringPool.BLANK;
		String lawyerEmail = StringPool.BLANK;
		long  doctorId = ParamUtil.getLong(actionRequest, "doctor");
		long  lawyerId = ParamUtil.getLong(actionRequest, "lawyer");
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {
			User doctorUser = UserLocalServiceUtil.getUser(doctorId);
			doctorName = doctorUser.getFirstName()+" " +doctorUser.getLastName();
			doctorEmail = doctorUser.getEmailAddress();
			
			User lawyerUser =UserLocalServiceUtil.getUser(lawyerId);
			lawyerName = lawyerUser.getFirstName()+" " + lawyerUser.getLastName();
			lawyerEmail = lawyerUser.getEmailAddress();
			
		} catch (PortalException e1) {
			LOG.error(e1.getMessage(), e1);		}
		
		try {
			    File file = new File(System.getProperty("catalina.home")+"/temp/"+"LOP Request.pdf");
			    long clinicId = ParamUtil.getLong(actionRequest, "clinic");
			    Clinic clinic = ClinicLocalServiceUtil.getClinic(clinicId);
				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

	            //open
	            document.open();
	            
	            Font headingBoldFont = new Font();
	            headingBoldFont.setStyle(Font.BOLD);
	            headingBoldFont.setSize(18);
	            
	            Font normalBoldFont = new Font();
	            normalBoldFont.setStyle(Font.BOLD);
	            normalBoldFont.setSize(12);
	            
	            Font normalFont = new Font();
	            normalBoldFont.setSize(14);
	            

	            Paragraph clinicTitle = new Paragraph(clinic.getClinicName(),headingBoldFont);
	            clinicTitle.setAlignment(Element.ALIGN_CENTER);
	            document.add(clinicTitle);
	            
	            Paragraph clinicAddress = new Paragraph(clinic.getAddressLine1()+","+clinic.getAddressLine2()+","+clinic.getCity()+","+clinic.getState(),normalBoldFont);
	            clinicAddress.setAlignment(Element.ALIGN_CENTER);
	            document.add(clinicAddress);
	            document.add(Chunk.NEWLINE);
	            
	            Paragraph lopLabel = new Paragraph("Request for L.O.P",headingBoldFont);
	            lopLabel.setAlignment(Element.ALIGN_CENTER);
	            document.add(lopLabel);
	            
	            Paragraph lopDeatail = new Paragraph("(Letter of Protections)",normalBoldFont);
	            lopDeatail.setAlignment(Element.ALIGN_CENTER);
	            document.add(lopDeatail);
	            document.add(Chunk.NEWLINE);
	            
	            Paragraph attorneyLabel = new Paragraph("Attorney:",normalBoldFont);
	            lopDeatail.setAlignment(Element.ALIGN_LEFT);
	            document.add(attorneyLabel);
	            
	            Paragraph attorneyDetail = new Paragraph(lawyerName,normalFont);
	            attorneyDetail.setAlignment(Element.ALIGN_LEFT);
	            document.add(attorneyDetail);
	            document.add(Chunk.NEWLINE);
	            
	            // Client detail
	            Paragraph clientLabel = new Paragraph("Your Client Name: ",normalBoldFont);
	            clientLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(clientLabel);
	         
	            Paragraph clientDetail = new Paragraph(patient.getFirstName()+" " + patient.getLastName(),normalFont);
	            clientDetail.setAlignment(Element.ALIGN_LEFT);
	            document.add(clientDetail);
	            document.add(Chunk.NEWLINE);
	            
	            // DOB detail

	            Paragraph dobLabel = new Paragraph("DOB: ",normalBoldFont);
	            dobLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(dobLabel);
	            
	            Paragraph dobDeail = new Paragraph(dateFormat.format(patient.getDob()),normalFont);
	            dobDeail.setAlignment(Element.ALIGN_LEFT);
	            document.add(dobDeail);
	            document.add(Chunk.NEWLINE);
	            
	            // Procedures
	            
	            Paragraph procedureLabel = new Paragraph("Procedure(s): ",normalBoldFont);
	            procedureLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(procedureLabel);
	            
	            Paragraph procedureDetailParagraph = new Paragraph(procedureDetail,normalFont);
	            procedureDetailParagraph.setAlignment(Element.ALIGN_LEFT);
	            document.add(procedureDetailParagraph);
	            document.add(Chunk.NEWLINE);
	            
	            // Physician
	            Paragraph physicianLabel = new Paragraph("Ordering Physician: ",normalBoldFont);
	            physicianLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(physicianLabel);
	            
	            Paragraph physicialDetail = new Paragraph(doctorName,normalFont);
	            physicialDetail.setAlignment(Element.ALIGN_LEFT);
	            document.add(physicialDetail);
	            document.add(Chunk.NEWLINE);
	            
	            // Fax
	            
	            Paragraph faxLabel = new Paragraph("Please Fax LOP to: ",normalBoldFont);
	            faxLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(faxLabel);
	            
	            Paragraph faxDetail = new Paragraph(clinic.getFaxNo(),normalFont);
	            faxDetail.setAlignment(Element.ALIGN_LEFT);
	            document.add(faxDetail);
	            document.add(Chunk.NEWLINE);
	            
	            // Desclimer
	            Paragraph desclaimerLabel = new Paragraph("Disclaimer: ",normalBoldFont);
	            desclaimerLabel.setAlignment(Element.ALIGN_LEFT);
	            document.add(desclaimerLabel);
	            
	            Paragraph declaimerDetail = new Paragraph("In Order to best serve our mutual interests, ALL exams requiring an LOP are not usually scheduled prior to the LOP being received. If there are any questions or concerns about our service, bill, reports, or our settlement negotiations, please feel free to contact us at "+clinic.getFaxNo(),normalFont);
	            declaimerDetail.setAlignment(Element.ALIGN_LEFT);
	            document.add(declaimerDetail);
	            
	            //close
	            document.close();
	            
	            // Document upload..
	            Folder patientFolder = getFolder(actionRequest, PARENT_FOLDER_ID,String.valueOf(patient.getPatientId()));
	            if(Validator.isNotNull(patientFolder)){
	            	 Folder folder = getFolder(actionRequest, patientFolder.getFolderId(),"LOP Requests");
					 ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest); 
					 InputStream dlFileIs = new FileInputStream(file);
					 System.out.println(file.length());
					 try{
						 DLAppServiceUtil.addFileEntry(themeDisplay.getCompanyGroupId(), folder.getFolderId(),  file.getName(), MimeTypesUtil.getContentType(file),  file.getName(), "Lop request" , "", file, serviceContext);
						 sendMailNotification(actionRequest, file, patient, doctorEmail, lawyerEmail);
					 }catch(DuplicateFileEntryException e){
						LOG.error(e.getMessage(),e);
					 }
	            }
	            
	        } catch (DocumentException e) {
	        	LOG.error(e);
	        } catch(PortalException e){
	        	LOG.error(e);
	        }
		 
	}
	
	private boolean isFolderExist(ThemeDisplay themeDisplay,long parentFolderId,String folderName){
		boolean folderExist = false;
		try { 
			DLAppServiceUtil.getFolder(themeDisplay.getCompanyGroupId(), parentFolderId, folderName); 
			folderExist = true;
		} catch (Exception e) {	
			//LOG.error(e.getMessage(), e);
		} return folderExist; 
	}
	
	private void sendMailNotification(PortletRequest request, File file, Patient patient, String doctorEmail, String lawyerEmail){
		
		 ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		 InputStream is = null;
		 UnsyncBufferedReader unsyncBufferedReader = null;
		 ClassLoader classloader = getClass().getClassLoader();
		 String[] doctorEmailIds = {doctorEmail};
		 String[] lawerEmailIds = {lawyerEmail};
		 
		 try {
			is = classloader.getResourceAsStream("email-body.tmpl");
			StringBundler sb = new StringBundler();
			unsyncBufferedReader = new UnsyncBufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = unsyncBufferedReader.readLine()) != null) {
				 sb.append(line);
				 sb.append(CharPool.NEW_LINE);
			}
			unsyncBufferedReader.close();
			is.close();
			String body = sb.toString().trim();
			String[] variables = new String[] { "[$PATIENT_NAME]"};
			String[] values = new String[] { patient.getFirstName()+StringPool.SPACE+patient.getLastName()};
			body = StringUtil.replace(body, variables, values);
			String subject = "LOP Request";
			String fromMail = FROM_EMAIL;
			InternetAddress from = new InternetAddress(fromMail);

			InternetAddress[] to =new InternetAddress[doctorEmailIds.length+lawerEmailIds.length];
			for(int i=0; i<doctorEmailIds.length; i++){
				to[i]= new InternetAddress(doctorEmailIds[i]);
			}
			
			for(int i=0; i<lawerEmailIds.length; i++){
				to[doctorEmailIds.length+i]= new InternetAddress(doctorEmailIds[i]);
			}
			
			FileAttachment fileAttachment = new FileAttachment(file, file.getName());
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.add(fileAttachment);
			MailMessage mailMessage = new MailMessage(from, subject, body, true);
			
			mailMessage.addFileAttachment(new File(file.getAbsolutePath()));
			mailMessage.setTo(to);
			MailServiceUtil.sendEmail(mailMessage);
			//file.delete();
		} catch (Exception e) {
			if(null != is && null != unsyncBufferedReader){
			try{
			is.close();
			unsyncBufferedReader.close();
			}catch(IOException e1){
				LOG.error(e1.getMessage(), e1);
			}
			}
			LOG.error(e.getMessage(), e);
		}
	 }
}
