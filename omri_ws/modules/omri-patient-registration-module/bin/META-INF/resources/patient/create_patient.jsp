<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@ include file="../init.jsp" %>
<%@ page import="com.omri.service.common.beans.PatientBean" %>
<%@ page import="com.omri.service.common.beans.PatientResourceBean" %>
<%@ page import="java.util.List" %>
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
										    <div class="row">
											    <div class="form-group col-md-6">
					                            	<aui:input name="firstName" label="patient.firstName" cssClass="form-control" placeholder="First Name" value="${patientBean.firstName }">
														<aui:validator name="required" />
													</aui:input>
					                          	</div>
					                          	<div class="form-group col-md-6">
					                            	<aui:input name="lastName" label="patient.lastName" cssClass="form-control" placeholder="Last Name" value="${patientBean.lastName }">
															<aui:validator name="required" />
													</aui:input>
					                           </div>
				                           </div>
				                            <div class="row">
				                           <div class="form-group col-md-4">
				                                 <fmt:formatDate pattern = "MM/dd/yyyy" value = "${patientBean.dob}" var="patientDob"/>
				                                <aui:input type="text"  cssClass="form-control" label="patient.dob"  name="patientDOB" placeholder="MM/DD/YYYY" value="${patientDob }"> 
				                                	<aui:validator name="required" />
				                                </aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="phoneNo" label="patient.phone" cssClass="form-control" value="${patientBean.phoneNo }" placeholder="(XXX)-XXX-XXXX">
				                                	<aui:validator name="required" />
				                                	<aui:validator name="custom" errorMessage="err-valid-regex-mobile">
														function(val, fieldNode, ruleValue) {
															var regex = /(\(\d{3}\)-)\d{3}-\d{4}/g;
															return regex.test(val);
														}
													</aui:validator>
				                                </aui:input>
				                                <aui:script>
													new Formatter(document.getElementById('<portlet:namespace/>'+'phoneNo'), {
														'pattern': '({{999}})-{{999}}-{{9999}}',
														'persistent': false
													});
												</aui:script>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="address1" label="patient.address1" cssClass="form-control" placeholder="Address1" value="${ patientBean.addressLine1}"></aui:input>
				                            </div>
				                            </div>
											<div class="form-group col-md-4">
				                                <aui:input name="address2" label="patient.address2" cssClass="form-control" placeholder="Address2" value="${ patientBean.addressLine2}"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                               <aui:input name="city" label="patient.city" cssClass="form-control" placeholder="City" value="${ patientBean.city}"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                                <aui:input name="state" label="patient.state" cssClass="form-control" placeholder="State" value="${ patientBean.state}"></aui:input>
				                            </div>
				                            <br/>
											<div class="form-group col-md-4">
				                                <aui:input name="country" label="patient.country" cssClass="form-control" placeholder="Country" value="${ patientBean.country}"></aui:input>
				                            </div>
				                            <div class="form-group col-md-4">
				                               <aui:input name="zip" label="patient.zip" cssClass="form-control" placeholder="Zip" value="${ patientBean.zip}"></aui:input>
				                            </div>
					                         <br/>
					                         <div class="form-group col-md-12">
												<aui:select name="patient_status" label="Patient Status" cssClass="patient_select">
													<aui:option value="0" selected='${patientBean.patientClinic.patient_status == "0" ? true : false }'>Referral Received</aui:option>
													<aui:option value="1" selected='${patientBean.patientClinic.patient_status == "1" ? true : false }'>Lop Received</aui:option>
													<aui:option value="2" selected='${patientBean.patientClinic.patient_status == "2" ? true : false }'>Patient contacted</aui:option>
													<aui:option value="3" selected='${patientBean.patientClinic.patient_status == "3" ? true : false }'>Patient Scheduled</aui:option>
													<aui:option value="4" selected='${patientBean.patientClinic.patient_status == "4" ? true : false }'>Patient Checked In</aui:option>
													<aui:option value="5" selected='${patientBean.patientClinic.patient_status == "5" ? true : false }'>Patient Canceled</aui:option>
													<aui:option value="6" selected='${patientBean.patientClinic.patient_status == "6" ? true : false }'>Patient Rescheduled</aui:option>
													<aui:option value="7" selected='${patientBean.patientClinic.patient_status == "7" ? true : false }'>Patient No-showed</aui:option>
													<aui:option value="8" selected='${patientBean.patientClinic.patient_status == "8" ? true : false }'>Study Complete</aui:option>
													<aui:option value="9" selected='${patientBean.patientClinic.patient_status == "9" ? true : false }'>Report Received</aui:option>
													<aui:option value="10" selected='${patientBean.patientClinic.patient_status == "10" ? true : false }'>Invoice/Report Sent</aui:option>
													<aui:option value="11" selected='${patientBean.patientClinic.patient_status == "11" ? true : false }'>Payment Received</aui:option>
												</aui:select>
											 </div>
											 <div class="">
					                			<a class="btn btn-primary pull-right" style="margin-right: 21px;"  data-section="pi"  onclick='patientFn.setNext("pi")' >Next</a>
					              			 </div>
										 </div> 
										 <div class="row m0 setup-content" id="step-2">
												<div class="form-group col-md-12">
					                                    <aui:select name="doctor" label="doctor" cssClass="form-control patient_select">
																<aui:option value=""> Select Doctor</aui:option>
															<c:forEach items="${doctorAdminList }" var="doctor"> 
																<aui:option value="${doctor.userId }" selected='${patientBean.patientClinic.doctorId == doctor.userId ? true : false }'>  ${doctor.firstName } ${doctor.lastName }</aui:option>
															</c:forEach>
														</aui:select>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="doctorPhone" label="doctor.phone" cssClass="form-control" readonly="true" placeholder="Doctor Phone" value="${patientBean.patientClinic.doctorPhoneNo }"></aui:input>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="doctorFax" label="doctor.fax" cssClass="form-control" readonly="true" placeholder="Doctor Fax" value="${doctorFax}"></aui:input>
					                            </div>
					                            <br/><br/>
					                            
					                            <div class="form-group col-md-12">
					                                    <aui:select name="lawyer" label="lawyer" cssClass="form-control patient_select">
																<aui:option value=""> Select Lawyer</aui:option>
															<c:forEach items="${lawyerAdminList }" var="lawyer"> 
																<aui:option value="${lawyer.userId }" selected='${patientBean.patientClinic.lawyerId == lawyer.userId ? true : false }'>  ${lawyer.firstName } ${lawyer.lastName }</aui:option>
															</c:forEach>
														</aui:select>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="lawyerPhone" label="lawyer.phone" readonly="true" cssClass="form-control" placeholder="Lawyer Phone" value="${patientBean.patientClinic.lawyerPhoneNo }"></aui:input>
					                            </div>
					                            <div class="form-group col-md-6">
					                                    <aui:input name="lawyerFax" label="lawyer.fax" readonly="true" cssClass="form-control" placeholder="Lawyer Fax" value="${lawyerFax}"></aui:input>
					                            </div>
					                            
					                            
					                            <div class="">
					                				<a class="btn btn-primary" style="margin-left: 21px;" onclick='patientFn.setPrev("dl")' >Previous</a>
					                				<a class="btn btn-primary pull-right" style="margin-right: 21px;"  onclick='patientFn.setNext("dl")' >Next</a>
					              				</div>
				                            </div>
											<div class="row m0 setup-content" id="step-3">
													<div class="form-group col-md-12">
						                                <aui:select name="clinic" label="clinic">
																<aui:option value="">Select Clinic</aui:option>
																<c:forEach items="${clinicList }" var="clinicMaster">
													 				<aui:option value="${clinicMaster.clinicId }" selected='${patientBean.patientClinic.clinicId == clinicMaster.clinicId ? true : false }'> ${clinicMaster.clinicName }</aui:option>
																</c:forEach>
														</aui:select>
						                            </div>
						                            <br/><br/><br/><br/>
													<c:if test="${ empty patientBean }">
														<div id="resources">
															<aui:input name="resourceCount" type="hidden"  />
															<div class="lfr-form-row lfr-form-row-inline">
																<div class="row-fields">
																	<div class="form-group col-md-12">
																			<aui:select fieldParam='resource1' name="resource1" id="resource1" label="resource" cssClass="form-control patient_select resourceItem"></aui:select>
																			<aui:select fieldParam='specification1' name="specification1" id="specification1" label="specification" cssClass="form-control patient_select specificationItem"></aui:select>
																			<aui:input fieldParam='occurance1' name="occurance1" id="occurance1" label="occurance"></aui:input>
																	</div>	
																</div>
															</div>
														</div>
													</c:if>
													<c:if test="${not empty patientBean }">
														<div id="resources">
														   <%
														   PatientBean patientBean = (PatientBean)renderRequest.getAttribute("patientBean");
														   JSONArray resourceArray = (JSONArray)renderRequest.getAttribute("resourceArray");
														   List<PatientResourceBean> resourceList =  patientBean.getResourceBeanList();
														   pageContext.setAttribute("resourceSize", resourceList.size());
														   %>
															<aui:input name="resourceCount" type="hidden" value="<%=(resourceList.size()-1) %>"/>
															
															<%
																for(int i=1;i<=resourceList.size();i++){
																long selectedResourceId = resourceList.get(i-1).getResourceId();
																long selectedSpecificationId = resourceList.get(i-1).getSpecificationId();
																JSONObject selectedResJsonObj= null;
															%>
															<div class="lfr-form-row lfr-form-row-inline">
																<div class="row-fields">
																	<div class="form-group col-md-12">
																			<aui:select fieldParam='<%="resource"+i %>' name='<%="resource" + i %>' id='<%="resource" + i %>'   label="resource" cssClass="form-control patient_select resourceItem">
																				<%
																					for(int j=0;j<resourceArray.length();j++){
																					JSONObject resourceJsonObj = resourceArray.getJSONObject(j);
																					boolean isSelected = false;
																					if(resourceJsonObj.getLong("resourceId")==selectedResourceId){
																						selectedResJsonObj = resourceJsonObj;
																						isSelected = true;
																					}
																				%>
																					<aui:option value='<%=resourceJsonObj.getString("resourceId") %>' selected="<%=isSelected %>"><%=resourceJsonObj.getString("resourceName") %></aui:option>
																				<%
																					}
																				%>
																			</aui:select>
																			<aui:select fieldParam='<%="specification"+i %>' name='<%="specification"+i %>' id='<%="specification"+i %>' label="specification" cssClass="form-control patient_select specificationItem">
																				<%
																					JSONArray specificationArray = selectedResJsonObj.getJSONArray("specifications");
																					for(int k=0;k<specificationArray.length();k++){
																						boolean isSelected = false;
																						JSONObject specificationObj = specificationArray.getJSONObject(k);
																						if(specificationObj.getLong("specificatonId")==selectedSpecificationId){
																							isSelected = true;
																						}
																				%>
																						<aui:option value='<%=specificationObj.getString("specificatonId") %>' selected="<%=isSelected %>"><%=specificationObj.getString("specificatonName") %></aui:option>
																				<% }%>
																			</aui:select>
																			<aui:input fieldParam='<%="occurance"+i %>' name='<%="occurance"+i %>' id='<%="occurance"+i %>' label="occurance" value="<%=resourceList.get(i-1).getOccurnace() %>"></aui:input>
																	</div>	
																</div>
															</div>
															<%
																}
															%>
														</div>
													</c:if>
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
												<div class="">
					                				<a class="btn btn-primary" style="margin-left: 21px;" onclick='patientFn.setPrev("proc")' >Previous</a>
					                				<a class="btn btn-primary pull-right"  style="margin-right: 21px;" onclick='patientFn.setNext("proc")' >Next</a>
					              				</div>
											</div>	
									 		<div class="row m0 setup-content" id="step-4">	
				                            		<div class="form-group col-md-12">
									 					LOP : <input type="file" name="lopDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="lopNotes" label="lop.notes" max="500" cssClass="" value="${ patientBean.lopNotes}"/>
				                            		</div>
				                            		<div class="form-group col-md-12">
									 					Order : <input type="file" name="orderDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="orderNotes" label="order.notes" max="500" cssClass="" value="${ patientBean.orderNotes}"/>
				                            		</div>
				                            		<div class="form-group col-md-12">
									 					Invoice : <input type="file" name="invoiceDocument" id="uploadedFile"/>
									 				</div>
									 				<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="invoiceNotes" label="invoice.notes" max="500" cssClass="" value="${ patientBean.invoiceNotes}"/>
									 				</div>
				                            		<div class="form-group col-md-12">
				                                		<aui:input type="textarea" name="otherNotes" label="other.notes" max="500" cssClass="" value="${ patientBean.otherNotes}"/>
				                            		</div>
									 			<br/>
				                            	<div class="">
					                				<a class="btn btn-primary" style="margin-left: 21px;" onclick='patientFn.setPrev("doc")' >Previous</a>
					              				</div>
					              				<br/>
				                            	<div class="form-group col-md-12">
				                                	<aui:button type="button" value="Submit"  cssClass="addPatientBtn btn btn-primary"/>
				                            	</div>
									 		</div>
									 	 </div>	
									 	 <aui:input type="hidden" name="patientId" value="${patientId }"/>
								     </aui:form>
							 </div>
					    </div>
        	    </div>
	    	</div>
 		 </div>
  	</div>
<script type="text/javascript">
jQuery.noConflict();
(function($) {
    $(function() {
	  //Date picker
	  $('#'+ '<portlet:namespace/>' + 'patientDOB').datepicker({
	    autoclose: true
	  }).on('changeDate', function(ev) {
		  AUI().use('aui-base','aui-form-validator', function(A) {
				var myFormValidator = Liferay.Form.get('<portlet:namespace />createPatientForm').formValidator;
				myFormValidator.validateField('<portlet:namespace />patientDOB');
			});
	  });
    });

})(jQuery);
</script>  	
<aui:script>
var resourceCount=0;
var resourceSize = '${resourceSize}';
if(resourceSize>0){
	resourceCount =(resourceSize-1); 
}
var patientFn={};
var resourceIdDetail = '${resourceIdDetail}';
var patientRegistrationMouleNS =  '<portlet:namespace/>';
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters', 'node-event-simulate' ,'autocomplete-highlighters','aui-datepicker', function(A) {
	
	var doctorLawyerValidator = new A.FormValidator({
		boundingBox: document.<portlet:namespace/>createPatientForm,
		rules: {
			<portlet:namespace/>doctor: {
				required: true
			},
			<portlet:namespace/>lawyer: {
				required: true
			}
		},
		fieldStrings: {
			<portlet:namespace/>doctor: {
				required: 'Please select Doctor'
				},
				<portlet:namespace/>lawyer: {
				required: 'Please select Lawyer'
			}
		}
	});
	
	
	var procdeureValidator = new A.FormValidator({
		boundingBox: document.<portlet:namespace/>createPatientForm,
		rules: {
			<portlet:namespace/>clinic: {
				required: true
			},
			<portlet:namespace/>resource0: {
				required: true
			},
			<portlet:namespace/>specification0: {
				required: true
			},
			<portlet:namespace/>occurance0: {
				required: true
			}
		},
		fieldStrings: {
			<portlet:namespace/>clinic: {
				required: 'Please select Clinic'
				},
				<portlet:namespace/>resource0: {
				required: 'Please select resource'
				},
				<portlet:namespace/>specification0: {
					required: 'Please select specification'
				},
				<portlet:namespace/>occurance0: {
				required: 'Please enter valid occurance'
				}
		}
	});
	
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
		console.log("clinic chnaged ->" + clinicId);
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
	              			/*if(resourceIdDetail.length>0){
	              				if(resourceIdDetail.indexOf(clinicResourcesArray[i].resourceId)>=0){
	              					resourceItemElement.append("<option value="+clinicResourcesArray[i].resourceId+" selected='true' >"+ clinicResourcesArray[i].resourceName +"</option> ");
	              				}else{
	              					resourceItemElement.append("<option value="+clinicResourcesArray[i].resourceId+">"+ clinicResourcesArray[i].resourceName +"</option> ");
	              				}
	              			}else{*/
                   				resourceItemElement.append("<option value="+clinicResourcesArray[i].resourceId+" >"+ clinicResourcesArray[i].resourceName +"</option> ");
	              			//}
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

	/*
	step1Link.on('click', function(e) {
		if(!patientFn.isFormValid()){
			return false;
		}
		step1Content.show();
		step2Content.hide();
		step3Content.hide();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step2Link.on('click', function(e) {
		if(!patientFn.isFormValid()){
			return false;
		}		
		step1Content.hide();
		step2Content.show();
		step3Content.hide();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step3Link.on('click', function(e) {
		if(!patientFn.isFormValid()){
			return false;
		}
		step1Content.hide();
		step2Content.hide();
		step3Content.show();
		step4Content.hide();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});
	step4Link.on('click', function(e) {
		if(!patientFn.isFormValid()){
			return false;
		}
		step1Content.hide();
		step2Content.hide();
		step3Content.hide();
		step4Content.show();
		removeAllActiveClass();
		this.ancestor('li').addClass("active");
	});*/

	var removeAllActiveClass = function(){
		A.all('.link').each(
				  function (node) {
				    node.removeClass("active");
				  }
	    );
	}
	
	patientFn.setNext = function(currStep){
		if(!patientFn.isFormValid()){
			return false;
		}
		if(currStep==='pi'){
			//A.one('#step-2-link').simulate('click');
			step1Content.hide();
			step2Content.show();
			step3Content.hide();
			step4Content.hide();
			removeAllActiveClass();
			//this.ancestor('li').addClass("active");
			A.one(".doct").addClass("active");
		}else if(currStep==='dl'){
			// check doctor and lawyer detail
			doctorLawyerValidator.validate();
			if(doctorLawyerValidator.hasErrors()){
				return false;
			}
			//A.one('#step-3-link').simulate('click');
			
			step1Content.hide();
			step2Content.hide();
			step3Content.show();
			step4Content.hide();
			removeAllActiveClass();
			A.one(".cap").addClass("active");
			
		}else if(currStep==='proc'){
			// Check procedure and resource
			procdeureValidator.validate();
			if(procdeureValidator.hasErrors()){
				return false;	
			}
			//A.one('#step-4-link').simulate('click');
			
			step1Content.hide();
			step2Content.hide();
			step3Content.hide();
			step4Content.show();
			removeAllActiveClass();
			A.one(".docu").addClass("active");
		}
	}
	
	patientFn.setPrev = function(currStep){
		if(!patientFn.isFormValid()){
			return false;
		}
		if(currStep==='dl'){
			//A.one('#step-1-link').simulate('click');
			step1Content.show();
			step2Content.hide();
			step3Content.hide();
			step4Content.hide();
			removeAllActiveClass();
			A.one(".pi").addClass("active");
		}else if(currStep==='proc'){
			//A.one('#step-2-link').simulate('click');
			step1Content.hide();
			step2Content.show();
			step3Content.hide();
			step4Content.hide();
			removeAllActiveClass();
			A.one(".doct").addClass("active");
		}else if(currStep==='doc'){
			//A.one('#step-3-link').simulate('click');
			step1Content.hide();
			step2Content.hide();
			step3Content.show();
			step4Content.hide();
			removeAllActiveClass();
			A.one(".cap").addClass("active");
		}
	}

	patientFn.isFormValid =  function(){
		var myFormValidator = Liferay.Form.get('<portlet:namespace />createPatientForm').formValidator;
		myFormValidator.validate();
		if(myFormValidator.hasErrors()){
			return false;
		}else{
			return true;
		}
	}
	
	if(resourceSize>0){
		var resourceItemList = A.all('.resourceItem');
		var clinicId = A.one("#"+patientRegistrationMouleNS+"clinic").val();
        resourceItemList.each(function() {
        	var resourceItemElement = this;	
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
       });
	}

});
</aui:script>
