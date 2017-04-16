<%@ include file="../init.jsp" %>

<portlet:actionURL var="uploadPatientDocumentURL" name="/upload-patient-document">
	<portlet:param name="patientId" value="${patientId }"></portlet:param>
</portlet:actionURL>
<br/>
<br/>
<aui:form name="uploadCaseDocument" action="${uploadPatientDocumentURL}" cssClass="row contact_form" method="post" enctype="multipart/form-data">
		<div class="row m0">	
			<input type="file" name="uploadedFile" multiple="multiple"  id="uploadedFile">
		</div>
		<div class="row m0">
          	<div class="col-sm-12">
              	<aui:button type="button" value="Uppload"  cssClass="uploadDoc"/>
          	</div>
   		</div> 
</aui:form>

<aui:script>
var resourceCount=0;
var patientRegistrationMouleNS =  '<portlet:namespace/>';
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters','aui-datepicker', function(A) {
	var uploadDocBtn= A.one(".uploadDoc");
	uploadDocBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />uploadCaseDocument').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />uploadCaseDocument.submit();
		}
	});
});	
</aui:script>	