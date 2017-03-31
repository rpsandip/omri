<%@ include file="../init.jsp" %>

<portlet:actionURL var="createPatientActionURL" name="/user/create_patient">
</portlet:actionURL>

<aui:form name="createPatientForm" action="${createPatientActionURL}">
	<h3><liferay-ui:message key="patient"></liferay-ui:message></h3>
	<aui:input name="firstName" label="patient.firstName">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="lastName" label="patient.lastName">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="mobile" label="patient.phone">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="birthDate" label="patient.dob">
		<aui:validator name="required" />
	</aui:input>
	<hr/>
	<h3><liferay-ui:message key="doctor"></liferay-ui:message></h3>
	
	<aui:select name="doctor" label="doctor">
	</aui:select>
	<aui:input name="doctorPhone" label="doctor.phone" type="hidden"></aui:input>
	<hr/>
	
	<h3><liferay-ui:message key="clinic"></liferay-ui:message></h3>
	
	<aui:select name="clinic" label="clinic"></aui:select>
	<div id="resources">
		<div class="lfr-form-row lfr-form-row-inline">
			<div class="row-fields">
				<aui:select name="resource0" id="resource0" label="resource" cssClass="resourceItem"></aui:select>
				<aui:select name="specification0" id="specification0" label="specification" cssClass="specificationItem"></aui:select>
				<aui:input fieldParam='occurance0' name="occurance0" id="occurance0" label="occurance"></aui:input>
				 <aui:input name="resourceCount" type="hidden"  />
				 <aui:script use="liferay-auto-fields">
				 new Liferay.AutoFields({
						 contentBox: '#resources',	
					 fieldIndexes: '<portlet:namespace/>resourceIndexes',
					 on :{
						 'clone' : function(newField){
							 
						 },
						 'delete' : function(){
							 
						 }
					 }
				 }).render();
				 </aui:script>
			</div>
		</div>
	</div>			
</aui:form>

<aui:script>
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters', function(A) {
	var clinicSelect= A.one("#clinic");
	clinicSelect.on('change', function(e) {
		
	});
});
</aui:script>
