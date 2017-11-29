<%@ include file="../init.jsp" %>
<portlet:actionURL var="importPatientURL" name="/import_patients">
</portlet:actionURL>

<liferay-ui:success key="patient-import-success" message="patient-import-success"/>


<div class="page-title">
  <div class="title_left">
   	<h2> Import Patients </h2>
  </div>
</div>
<div class="row">
	<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div class="x_content">
					<aui:form name="importPatientFm" action="${importPatientURL}" cssClass="form-horizontal form-label-left" enctype="multipart/form-data">
						<div class="form-group">
					       <div class="col-md-6 col-sm-6 col-xs-12">
					          <aui:input name="importPatientFile" label="Import Patients(.Xlsx)" type="file">
					          		<aui:validator name="acceptFiles">'.xlsx'</aui:validator>
					          </aui:input>
					       </div>
					    </div>
					   <div class="form-group">
					      	<div class="col-md-10 col-sm-6 col-xs-12 col-md-offset-3">
								<aui:button type="button" value="Submit"  cssClass="importPatientBtn btn btn-success"/>
							</div>
						</div>				
					</aui:form>	
					<c:if test="${isImported eq true }">
						<b>Total Patients:: </b> ${totalPatientCount }  <br/>
						<b>Successfull :: </b> ${successImportedPatientCount }  <br/>
						<b>UnSuccessfull :: </b> ${unsuccessImportedPatientCount }  <br/>
						
						<c:if test="${unsuccessImportedPatientCount gt 0}">
							<b>UnSuccessfull Import Patients :</b> <br/><br/>
							<ul>
								<b>Patient Id</b>
								<c:forEach items="${unsuccessfullPatientList }" var ="unsuccessfullPatient">
									<c:if test="${not empty unsuccessfullPatient}">
										<li>
											${unsuccessfullPatient }
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</c:if>	
					</c:if>
				</div>
			</div>
		</div>
	</div>				
<aui:script>
var userModuleNameSpace =  '<portlet:namespace/>';
AUI().use('aui-base','aui-form-validator', function(A) {
	var importPatientBtn= A.one(".importPatientBtn");
	importPatientBtn.on('click', function(e) {
		var formValidator = Liferay.Form.get('<portlet:namespace />importPatientFm').formValidator;
		formValidator.validate();
		if(!formValidator.hasErrors()){
			document.<portlet:namespace />importPatientFm.submit();
		}
	});
});
</aui:script>		