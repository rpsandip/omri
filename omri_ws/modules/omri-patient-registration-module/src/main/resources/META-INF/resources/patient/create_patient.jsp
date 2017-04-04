<%@ include file="../init.jsp" %>

<portlet:actionURL var="createPatientActionURL" name="/user/create_patient">
</portlet:actionURL>
<portlet:resourceURL id="/getSpecificationList" var="getSpecificationListURL" />
<portlet:resourceURL id="/getClinicResources" var="getClinicResources" />

<aui:form name="createPatientForm" action="${createPatientActionURL}">
	<h3><liferay-ui:message key="patient"></liferay-ui:message></h3>
	<aui:input name="firstName" label="patient.firstName">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="lastName" label="patient.lastName">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="phoneNo" label="patient.phone">
		<aui:validator name="required" />
	</aui:input>
	<div class="aui-datepicker aui-helper-clearfix" id="#<portlet:namespace />startDatePicker">
		<aui:input type="text" name="patientDOB" id="<portlet:namespace />patientDOB" size="30" placeholder="MM/DD/YYYY"/>
	</div>
	<aui:input name="address1" label="patient.address1">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="address2" label="patient.address2">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="city" label="patient.city">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="state" label="patient.state">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="country" label="patient.country">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="zip" label="patient.zip">
		<aui:validator name="required" />
	</aui:input>
	<hr/>
	<h3><liferay-ui:message key="doctor"></liferay-ui:message></h3>
	
	<aui:select name="doctor" label="doctor">
		<c:forEach items="${doctorAdminList }" var="doctor"> 
			<aui:option value="${doctor.userId }">  ${doctor.firstName } ${doctor.lastName }</aui:option>
		</c:forEach>
	</aui:select>
	<aui:input name="doctorPhone" label="doctor.phone" ></aui:input>
	<hr/>
	
	<h3><liferay-ui:message key="clinic"></liferay-ui:message></h3>
	
	<aui:select name="clinic" label="clinic">
		<c:forEach items="${clinicList }" var="clinicMaster">
			 <aui:option value="${clinicMaster.clinicId }"> ${clinicMaster.clinicName }</aui:option>
		</c:forEach>
	</aui:select>
	<div id="resources">
		<div class="lfr-form-row lfr-form-row-inline">
			<div class="row-fields">
				<aui:select name="resource0" id="resource0" label="resource" cssClass="resourceItem"></aui:select>
				<aui:select name="specification0" id="specification0" label="specification" cssClass="specificationItem">
					<%-- <aui:option value="25786">Abdomen</aui:option>
					<aui:option value="30234">Ankle</aui:option> --%>
				</aui:select>
				<aui:input fieldParam='occurance0' name="occurance0" id="occurance0" label="occurance"></aui:input>
				 <aui:input name="resourceCount" type="hidden"  />
				 <aui:script use="liferay-auto-fields">
				 new Liferay.AutoFields({
					 contentBox: '#resources',	
					 fieldIndexes: '<portlet:namespace/>resourceIndexes',
					 on :{
						 'clone' : function(newField){
							 var resourceItemList = A.all('.resourceItem');
							 resourceItemList.each(function() {
			                 	var resourceItemElement = this;
			                 	//resourceItemElement.all('option').remove();
			                 	resourceItemElement.on('change', function(e) {
				    				var resourceId = resourceItemElement.val();
				    				A.io.request('${getSpecificationListURL}', {
							               method: 'post',
							               data: {
							            	   '<portlet:namespace/>resourceId': resourceId
							               },
							               on: {
							                    success: function(data) {
							                      var specificationElement = resourceItemElement.get('parentNode').next().one('.specificationItem');
							                      specificationElement.all('option').remove();
							                      var responseSpecificationArray = JSON.parse(this.get('responseData'));
							                      for(var i=0;i<responseSpecificationArray.length;i++){
							                    	  specificationElement.append("<option value="+responseSpecificationArray[i].specificationId+" >"+ responseSpecificationArray[i].specificationName +"</option> ");
							                      }
							                    }
							               }
							        });
				    			});
							 });   	
							 resourceCount++;
					    	 A.one("#"+patientRegistrationMouleNS+"resourceCount").val(resourceCount);
						 },
						 'delete' : function(){
							 resourceCount--;
    					     A.one("#"+patientRegistrationMouleNS+"resourceCount").val(resourceCount);
						 }
					 }
				 }).render();
				 </aui:script>
			</div>
		</div>
	</div>		
	 <aui:input name="resourceCount" type="hidden"  />	
	 <aui:button type="button" value="Add Patient"  cssClass="addPatientBtn"/>
</aui:form>

<aui:script>
var resourceCount=0;
var patientRegistrationMouleNS =  '<portlet:namespace/>';
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters','aui-datepicker', function(A) {
	
	
	var addPatientBtn= A.one(".addPatientBtn");
	addPatientBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />createPatientForm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />createPatientForm.submit();
		}
	});
	
	var simpleDatepicker1 = new A.DatePicker({
		trigger: '#<portlet:namespace />patientDOB',
		}).render('#<portlet:namespace />startDatePicker');
	
	var clinicSelect= A.one("#"+patientRegistrationMouleNS+"clinic");
	A.one("#"+patientRegistrationMouleNS+"resourceCount").val(resourceCount);
	clinicSelect.on('change', function(e) {
		var clinicId= this.val();
		var currentNode = this;
		A.io.request('${getClinicResources}', {
            method: 'post',
            data: {
         	   '<portlet:namespace/>clinicId': clinicId
            },
            on: {
                 success: function(data) {
                   var clinicResourcesArray = JSON.parse(this.get('responseData'));
                   console.log(clinicResourcesArray.length);
                   var resourceItemList = A.all('.resourceItem');
                    resourceItemList.each(function() {
                    	var resourceItemElement = this;	
                		resourceItemElement.all('option').remove();
                   		for(var i=0;i<clinicResourcesArray.length;i++){
	              			resourceItemElement.append("<option value="+clinicResourcesArray[i].resourceId+" >"+ clinicResourcesArray[i].resourceName +"</option> ");
	              			resourceItemElement.on('change', function(e) {
			    				var resourceId = resourceItemElement.val();
			    				A.io.request('${getSpecificationListURL}', {
						               method: 'post',
						               data: {
						            	   '<portlet:namespace/>resourceId': resourceId
						               },
						               on: {
						                    success: function(data) {
						                      var specificationElement = resourceItemElement.get('parentNode').next().one('.specificationItem');
						                      specificationElement.all('option').remove();
						                      var responseSpecificationArray = JSON.parse(this.get('responseData'));
						                      for(var i=0;i<responseSpecificationArray.length;i++){
						                    	  specificationElement.append("<option value="+responseSpecificationArray[i].specificationId+" >"+ responseSpecificationArray[i].specificationName +"</option> ");
						                      }
						                    }
						               }
						        });
			    			});
        	           	}
                   });
               }
            }
     });
	});
});
</aui:script>
