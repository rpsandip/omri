<%@ include file="../init.jsp" %>

<portlet:actionURL var="createPatientActionURL" name="/user/create_patient">
</portlet:actionURL>
<portlet:resourceURL id="/getSpecificationList" var="getSpecificationListURL" />
<portlet:resourceURL id="/getClinicResources" var="getClinicResources" />

<div class="container">
	<div class="row ">
		 <div class="col-sm-8 contact_form_area">
		 	<h3 class="contact_section_title"><liferay-ui:message key="add.patient"/></h3>
		 	<div class="contactForm row m0">
				<aui:form name="createPatientForm" action="${createPatientActionURL}" cssClass="row contact_form">
					<div class="row m0">
						 <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="firstName" label="patient.firstName" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="lastName" label="patient.lastName" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                           </div>
                           <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input type="text"  cssClass="form-control"  name="patientDOB" id="<portlet:namespace />patientDOB" size="30" placeholder="MM/DD/YYYY"/>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="phoneNo" label="patient.phone" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="address1" label="patient.address1" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
							<div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="address2" label="patient.address2" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                   <aui:input name="city" label="patient.city" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                   <aui:input name="state" label="patient.state" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
							<div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="country" label="patient.country" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="zip" label="patient.zip" cssClass="form-control">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                            </div>
                          </div>
							<hr/>
							<div class="row m0">
								<div>
									<h5><liferay-ui:message key="doctor"></liferay-ui:message></h5>
								</div>
								<div class="col-sm-6">
	                                <div class="input-group">
	                                    <aui:select name="doctor" label="" cssClass="form-control patient_select">
											<c:forEach items="${doctorAdminList }" var="doctor"> 
												<aui:option value="${doctor.userId }">  ${doctor.firstName } ${doctor.lastName }</aui:option>
											</c:forEach>
										</aui:select>
	                                </div>
	                            </div>
	                            <div class="col-sm-6">
	                                <div class="input-group">
	                                    <aui:input name="doctorPhone" label="doctor.phone" cssClass="form-control"></aui:input>
	                                </div>
	                            </div>
                            </div>
							<hr/>
							<div class="row m0">
									<div>
										<h5><liferay-ui:message key="clinic"></liferay-ui:message></h5>
									</div>
									<div class="col-sm-12">
		                                <div class="input-group">
		                                    <aui:select name="clinic" label="">
												<aui:option value="0">Select Clinic</aui:option>
												<c:forEach items="${clinicList }" var="clinicMaster">
									 				<aui:option value="${clinicMaster.clinicId }"> ${clinicMaster.clinicName }</aui:option>
												</c:forEach>
											</aui:select>
		                                </div>
		                            </div>
									<div id="resources">
										<aui:input name="resourceCount" type="hidden"  />
										<div class="lfr-form-row lfr-form-row-inline">
											<div class="row-fields">
												<div class="col-sm-12">
														<aui:select name="resource0" id="resource0" label="resource" cssClass="form-control patient_select resourceItem"></aui:select>
														<aui:select name="specification0" id="specification0" label="specification" cssClass="form-control patient_select specificationItem"></aui:select>
														<aui:input fieldParam='occurance0' name="occurance0" id="occurance0" label="occurance"></aui:input>
												</div>	
										 				
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
							</div>	
					 		<aui:input name="resourceCount" type="hidden"/>	
							<div class="row m0">
                            	<div class="col-sm-12">
                                	<aui:button type="button" value="Add Patient"  cssClass="addPatientBtn"/>
                            	</div>
                     		</div> 
				</aui:form>
				</div>
			</div>	
		 </div>
    </div>
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
