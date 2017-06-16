<%@ include file="../init.jsp" %>

<portlet:actionURL var="createPatientActionURL" name="/user/create_patient">
</portlet:actionURL>
<portlet:resourceURL id="/getSpecificationList" var="getSpecificationListURL" />
<portlet:resourceURL id="/getClinicResources" var="getClinicResources" />
<portlet:resourceURL id="/getDoctorDetail" var="getDoctorDetailURL" />

<section class="content-header">
  <h1>
    <liferay-ui:message key="add.patient"/>
  </h1>
</section>	
 <div class="row">
	<div class="col-xs-12">
    	<div class="box">
       		<div class="box-body">
                <ul class="nav nav-pills nav-justified">
                    <li class="link active pi"><a id="step-1-link">
                        <h4 class="list-group-item-heading">Patient Information</h4>
                    </a></li>
                    <li class="link doct"><a id="step-2-link">
                        <h4 class="list-group-item-heading">Doctor & Lawyer</h4>
                    </a></li>
                    <li class="link cap"><a id="step-3-link">
                        <h4 class="list-group-item-heading">Clinic And Procedure</h4>
                    </a></li>
                    <li class="link docu"><a id="step-4-link">
                        <h4 class="list-group-item-heading">Documents</h4>
                    </a></li>    
                </ul>
                <div class="col-md-12 divider-line"><hr></div>
                <div class="tab-content">
					<div id="pi" class="tab-pane fade in active">
						<aui:form name="createPatientForm" action="${createPatientActionURL}" cssClass="row contact_form" method="post" enctype="multipart/form-data">
								<div class="box-body">
										<div class="row m0 setup-content" id="step-1">
										    <div class="form-group col-md-6">
				                            	<aui:input name="firstName" label="patient.firstName" cssClass="form-control">
													<aui:validator name="required" />
												</aui:input>
				                          	</div>
				                          	<div class="form-group col-md-6">
				                            	<aui:input name="lastName" label="patient.lastName" cssClass="form-control">
														<aui:validator name="required" />
												</aui:input>
				                           </div>
				                           <div class="form-group col-md-4">
				                                <aui:input name="cptCode" label="patient.cptCode" cssClass="form-control"></aui:input>
				                           </div>
				                           <div class="form-group col-md-4">
				                                <aui:input type="text"  cssClass="form-control" label="patient.dob"  name="patientDOB" size="30" placeholder="MM/DD/YYYY"/>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="phoneNo" label="patient.phone" cssClass="form-control"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="address1" label="patient.address1" cssClass="form-control"></aui:input>
				                            </div>
											<div class="form-group col-md-4">
				                                <aui:input name="address2" label="patient.address2" cssClass="form-control"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                               <aui:input name="city" label="patient.city" cssClass="form-control"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="state" label="patient.state" cssClass="form-control"></aui:input>
				                            </div>
											<div class="form-group col-md-4">
				                                <aui:input name="country" label="patient.country" cssClass="form-control"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                               <aui:input name="zip" label="patient.zip" cssClass="form-control"></aui:input>
				                            </div>
					                         <br/>
					                         <div class="form-group col-md-12">
												<aui:select name="patient_status" label="Patient Status" cssClass="patient_select">
													<aui:option value="0">Referral Received</aui:option>
													<aui:option value="1">Lop Received</aui:option>
													<aui:option value="2">Patient contacted</aui:option>
													<aui:option value="3">Patient Scheduled</aui:option>
													<aui:option value="4">Patient Checked In</aui:option>
													<aui:option value="5">Patient Canceled</aui:option>
													<aui:option value="6">Patient Rescheduled</aui:option>
													<aui:option value="7">Patient No-showed</aui:option>
													<aui:option value="8">Study Complete</aui:option>
													<aui:option value="9">Report Received</aui:option>
													<aui:option value="10">Invoice/Report Sent</aui:option>
													<aui:option value="11">Payment Received</aui:option>
												</aui:select>
											 </div>
											 <div class="box-footer">
					                			<a class="btn btn-primary pull-right" data-section="pi"  onclick='patientFn.setNext("pi")' >Next</a>
					              			 </div>
										 </div> 
										 <div class="row m0 setup-content" id="step-2">
												<div class="form-group col-md-12">
					                                    <aui:select name="doctor" label="doctor" cssClass="form-control patient_select">
																<aui:option value="0"> Select Doctor</aui:option>
															<c:forEach items="${doctorAdminList }" var="doctor"> 
																<aui:option value="${doctor.userId }">  ${doctor.firstName } ${doctor.lastName }</aui:option>
															</c:forEach>
														</aui:select>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="doctorPhone" label="doctor.phone" cssClass="form-control"></aui:input>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="doctorFax" label="doctor.fax" cssClass="form-control"></aui:input>
					                            </div>
					                            <br/><br/>
					                            
					                            <div class="form-group col-md-12">
					                                    <aui:select name="lawyer" label="lawyer" cssClass="form-control patient_select">
																<aui:option value="0"> Select Lawyer</aui:option>
															<c:forEach items="${lawyerAdminList }" var="lawyer"> 
																<aui:option value="${lawyer.userId }">  ${lawyer.firstName } ${lawyer.lastName }</aui:option>
															</c:forEach>
														</aui:select>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="lawyerPhone" label="lawyer.phone" cssClass="form-control"></aui:input>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="lawyerFax" label="lawyer.fax" cssClass="form-control"></aui:input>
					                            </div>
					                            
					                            
					                            <div class="box-footer">
					                				<button class="btn btn-primary"  onclick='patientFn.setPrev("dl")'>Previous</button>
					                				<button class="btn btn-primary pull-right" onclick='patientFn.setNext("dl")'>Next</button>
					              				</div>
				                            </div>
											<div class="row m0 setup-content" id="step-3">
													<div class="form-group col-md-12">
						                                <aui:select name="clinic" label="clinic">
																<aui:option value="0">Select Clinic</aui:option>
																<c:forEach items="${clinicList }" var="clinicMaster">
													 				<aui:option value="${clinicMaster.clinicId }"> ${clinicMaster.clinicName }</aui:option>
																</c:forEach>
														</aui:select>
						                            </div>
													<div id="resources">
														<aui:input name="resourceCount" type="hidden"  />
														<div class="lfr-form-row lfr-form-row-inline">
															<div class="row-fields">
																<div class="form-group col-md-12">
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
																	    				var clinicId = A.one("#"+patientRegistrationMouleNS+"clinic").val();
																	    				console.log("clinicId -?" + clinicId);
																	    				A.io.request('${getSpecificationListURL}', {
																				               method: 'post',
																				               data: {
																				            	   '<portlet:namespace/>resourceId': resourceId,
																				            	   '<portlet:namespace/>clinicId': clinicId
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
												<div class="box-footer">
					                				<button class="btn btn-primary"  onclick='patientFn.setPrev("proc")'>Previous</button>
					                				<button class="btn btn-primary pull-right" onclick='patientFn.setNext("proc")'>Next</button>
					              				</div>
											</div>	
									 		<aui:input name="resourceCount" type="hidden"/>	
									 		<div class="row m0 setup-content" id="step-4">	
				                            		<div class="form-group col-md-12">
									 					LOP : <input type="file" name="lopDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="lopNotes" label="lop.notes" max="500" cssClass=""/>
				                            		</div>
				                            		<div class="form-group col-md-12">
									 					Order : <input type="file" name="orderDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="orderNotes" label="order.notes" max="500" cssClass=""/>
				                            		</div>
				                            		<div class="form-group col-md-12">
									 					Invoice : <input type="file" name="invoiceDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="invoiceNotes" label="invoice.notes" max="500" cssClass=""/>
									 				</div>
				                            		<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="otherNotes" label="other.notes" max="500" cssClass=""/>
				                            		</div>
									 			<br/>
				                            	<div class="box-footer">
					                				<button class="btn btn-primary"  onclick='patientFn.setPrev("doc")'>Previous</button>
					              				</div>
				                            	<div class="form-group col-md-12">
				                                	<aui:button type="button" value="Submit"  cssClass="addPatientBtn btn btn-primary"/>
				                            	</div>
									 		</div>
									 	 </div>	
								     </aui:form>
							 </div>
					    </div>
        	    </div>
	    	</div>
 		 </div>
  	</div>
<aui:script>
var resourceCount=0;
var patientFn={};
var patientRegistrationMouleNS =  '<portlet:namespace/>';
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters', 'node-event-simulate' ,'autocomplete-highlighters','aui-datepicker', function(A) {
	
	var addPatientBtn= A.one(".addPatientBtn");
	addPatientBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />createPatientForm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />createPatientForm.submit();
		}
	});
	
	var doctorSelect= A.one("#"+patientRegistrationMouleNS+"doctor");
	doctorSelect.on('change', function(e) {
		var currentNode = this;
		var doctorId= this.val();
		console.log("doctorID ->" + doctorId);
		if(doctorId>0){
		A.io.request('${getDoctorDetailURL}', {
            method: 'post',
            data: {
         	   '<portlet:namespace/>userId': doctorId
            },
            on: {
                success: function(data) {
                    var docotrDetail = JSON.parse(this.get('responseData'));
                    console.log("docotrDetail ->" +docotrDetail.phone);
                    A.one("#"+patientRegistrationMouleNS+"doctorPhone").val(docotrDetail.phone);
                    A.one("#"+patientRegistrationMouleNS+"doctorFax").val(docotrDetail.fax);
                }
             }
		 });
	   }
	});
	
	
	var lawyerSelect= A.one("#"+patientRegistrationMouleNS+"lawyer");
	lawyerSelect.on('change', function(e) {
		var currentNode = this;
		var lawyerId= this.val();
		console.log("doctorID ->" + lawyerId);
		if(lawyerId>0){
		A.io.request('${getDoctorDetailURL}', {
            method: 'post',
            data: {
         	   '<portlet:namespace/>userId': lawyerId
            },
            on: {
                success: function(data) {
                    var docotrDetail = JSON.parse(this.get('responseData'));
                    console.log("docotrDetail ->" +docotrDetail.phone);
                    A.one("#"+patientRegistrationMouleNS+"lawyerPhone").val(docotrDetail.phone);
                    A.one("#"+patientRegistrationMouleNS+"lawyerFax").val(docotrDetail.fax);
                }
             }
		 });
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
						            	   '<portlet:namespace/>resourceId': resourceId,
						            	   '<portlet:namespace/>clinicId': clinicId
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
	
	var allContent = A.all(".setup-content");
	allContent.hide();
	var step1Link = A.one("#step-1-link");
	var step2Link = A.one("#step-2-link");
	var step3Link = A.one("#step-3-link");
	var step4Link = A.one("#step-4-link");

	var step1Content = A.one("#step-1");
	var step2Content = A.one("#step-2");
	var step3Content = A.one("#step-3");
	var step4Content = A.one("#step-4");

	step1Content.show();

	step1Link.on('click', function(e) {
		console.log("step1");
		step1Content.show();
		step2Content.hide();
		step3Content.hide();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step2Link.on('click', function(e) {
		console.log("step2");
		step1Content.hide();
		step2Content.show();
		step3Content.hide();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step3Link.on('click', function(e) {
		console.log("step3");
		step1Content.hide();
		step2Content.hide();
		step3Content.show();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step4Link.on('click', function(e) {
		console.log("step4");
		step1Content.hide();
		step2Content.hide();
		step3Content.hide();
		step4Content.show();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});

	var removeAllActiveClass = function(){
		A.all('.link').each(
				  function (node) {
				    node.removeClass("active");
				  }
	    );
	}
	
	patientFn.setNext = function(currStep){
		if(currStep==='pi'){
			A.one('#step-2-link').simulate('click');
		}else if(currStep==='dl'){
			A.one('#step-3-link').simulate('click');
		}else if(currStep==='proc'){
			A.one('#step-4-link').simulate('click');
		}
	}
	
	patientFn.setPrev = function(currStep){
		if(currStep==='dl'){
			A.one('#step-1-link').simulate('click');
		}else if(currStep==='proc'){
			A.one('#step-2-link').simulate('click');
		}else if(currStep==='doc'){
			A.one('#step-3-link').simulate('click');
		}
	}


});
</aui:script>
<script>
var ss;
jQuery.noConflict();
(function($) {
    $(function() {
	  //Date picker
	  $('#'+ '<portlet:namespace/>' + 'patientDOB').datepicker({
	    autoclose: true
	  });
    });

})(jQuery);
</script>
