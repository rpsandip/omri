<%@ include file="/init.jsp" %>

<portlet:actionURL var="submitTechnicalReportURL" name="/submitTechnicalReport">
</portlet:actionURL>

<section class="content-header">
  <h1>
    Submit Report
  </h1>
</section>
<section class="content">
 <div class="row">
 	 <div class="col-xs-12">
     	<div class="box">
            <div class="box-body">
					<aui:form name="submitTechnicalReprotFm" action="${submitTechnicalReportURL}" cssClass="row contact_form" method="post"  enctype="multipart/form-data">
						<aui:input type="textarea" name="technicalComment" max="1000" label="comment"></aui:input>
						<br/>
						<div class="row m0">
	                  		<div class="col-sm-12">
	                      		Report : <input type="file" name="orderDocument" id="uploadedFile" multiple="multiple"/>
	                  		</div>
	           			</div>
						<aui:input type="hidden" name="appointmentId" value="${appointment.appointmentId }"/>
						<br/>
						<div class="row m0">
	                  		<div class="col-sm-12">
	                      		<aui:button type="button" value="Submit Report"  cssClass="submitTechncalReportBtn btn btn-primary"/>
	                  		</div>
	           			</div> 
					</aui:form>
				</div>
			</div>
		</div>
	</div>
</section>
<aui:script>
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters','aui-datepicker', function(A) {
	var submitTechncalReportBtn= A.one(".submitTechncalReportBtn");
	submitTechncalReportBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />submitTechnicalReprotFm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />submitTechnicalReprotFm.submit();
		}
	});

});
</aui:script>