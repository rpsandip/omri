<%@ include file="/init.jsp" %>

<h3>Add Pateint Detail</h3>
<br/>
<portlet:actionURL var="addPatientMoreDetailURL" name="/addPatientMoreDetail">
</portlet:actionURL>

<div class="container">
	<div class="row ">
		 <div class="col-sm-8 contact_form_area">
		 	<div class="contactForm row m0">
				<aui:form name="patientMoreDetailFm" action="${addPatientMoreDetailURL}" cssClass="row contact_form" method="post">
					<div class="row m0 setup-content">
						 <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="firstName" label="patient.firstName"  value="${patient.firstName }">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="lastName" label="patient.lastName"  value="${patient.lastName }">
										<aui:validator name="required" />
									</aui:input>
                                </div>
                           </div>
                           <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="cptCode" label="patient.cptCode"  value="${patient.cptCode }">
									</aui:input>
                                </div>
                           </div>
                           <div class="col-sm-6">
                                <div class="input-group">
                                	<label for="<portlet:namespace />patientDOB"><liferay-ui:message key="patient.dob"></liferay-ui:message></label>
                                	<fmt:formatDate pattern="MM/dd/yyyy" value="${patient.dob}"  var="patientDOB"/>
                                    <aui:input type="text"  cssClass="form-control" label="" name="patientDOB" id="<portlet:namespace />patientDOB" size="30" value="${patientDOB }" placeholder="MM/DD/YYYY"/>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="phoneNo" label="patient.phone" cssClass="form-control" value="${patient.phoneNo }">
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="address1" label="patient.address1" cssClass="form-control" value="${patient.addressLine1 }">
									</aui:input>
                                </div>
                            </div>
							<div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="address2" label="patient.address2" cssClass="form-control" value="${patient.addressLine2 }">
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                   <aui:input name="city" label="patient.city" cssClass="form-control" value="${patient.city }">
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                   <aui:input name="state" label="patient.state" cssClass="form-control" value="${patient.state }">
									</aui:input>
                                </div>
                            </div>
							<div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="country" label="patient.country" cssClass="form-control" value="${patient.country }">
									</aui:input>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="input-group">
                                    <aui:input name="zip" label="patient.zip" cssClass="form-control" value="${patient.zip }">
									</aui:input>
                                </div>
                            </div>
	                         <br/>
						 </div>
						 <div class="row m0 setup-content">
							<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="mribefore"></liferay-ui:message>
                         			<p>
                         				<aui:input id="mriBeforeYes" name="mriBefore" label="" type="radio" value="true" 
                         				checked='${patientDetail.MRIBefore eq true ? true : false }'></aui:input>
										<label for="<portlet:namespace/>mriBeforeYes">Yes</label>
									</p>
									<p>
										<aui:input  id="mriBeforeNo" name="mriBefore" label="" type="radio"  value="false"
										 checked='${patientDetail.MRIBefore eq false ? true : false }'></aui:input>
										<label for="<portlet:namespace/>mriBeforeNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         		    <liferay-ui:message key="Claustrophobic"></liferay-ui:message>
                         			<p>
                         				<aui:input id="claustrophobicYes" name="claustrophobic" label="" type="radio"  value="true"
                         				checked='${patientDetail.claustrophobic eq true ? true : false }' cssClass="claustrophobicClass" ></aui:input>
										<label for="<portlet:namespace/>claustrophobicYes">Yes</label>
									</p>
									<p>
										<aui:input id="claustrophobicNo" name="claustrophobic" label="" type="radio"  value="false"
										checked='${patientDetail.claustrophobic eq false ? true : false }' cssClass="claustrophobicClass"></aui:input>
										<label for="<portlet:namespace/>claustrophobicNo">No</label>
									</p>
									<div id="claustrophobicDetailDiv">
										<aui:input type="text" name="claustrophobicDetail" label="claustrophobicDetail"/>
									</div>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="300lbs"></liferay-ui:message>
                         			<p>
                         			<aui:input id="300lbsYes" name="300lbs" label="" type="radio"  value="true" checked='${patientDetail.under300lbs eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>300lbsYes">Yes</label>
									</p>
									<p>
									<aui:input id="300lbsNo" name="300lbs" label="" type="radio"  value="false" checked='${patientDetail.under300lbs eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>300lbsNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="prevPatient"></liferay-ui:message>
                         			<p>
                         				<aui:input id="prevPatientYes" name="prevPatient" label="" type="radio"  value="true" checked='${patientDetail.previousPatient eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>prevPatientYes">Yes</label>
									</p>
									<p>
										<aui:input id="prevPatientNo" name="prevPatient" label="" type="radio"  value="false" checked='${patientDetail.previousPatient eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>prevPatientNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="paceMaker"></liferay-ui:message>
                         			<p>
                         				<aui:input id="paceMakerYes" name="paceMaker" label="" type="radio"  value="true" checked='${patientDetail.previousPatient eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>paceMakerYes">Yes</label>
									</p>
									<p>
										<aui:input id="paceMakerNo" name="paceMaker" label="" type="radio"  value="false" checked='${patientDetail.previousPatient eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>paceMakerNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="pacemakerDetail" label="pacemakerDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="metalInBody"></liferay-ui:message>
                         			<p>
                         				<aui:input id="metalInBodyYes" name="metalInBody" label="" type="radio"  value="true" checked='${patientDetail.metalInBody eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>metalInBodyYes">Yes</label>
									</p>
									<p>
										<aui:input id="metalInBodyNo"  name="metalInBody" label="" type="radio"  value="false" checked='${patientDetail.metalInBody eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>metalInBodyNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="metalInBodyDetail" label="metalInBodyDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="priorSurgery"></liferay-ui:message>
                         			<p>
	                         			<aui:input id="priorSurgeryYes" name="priorSurgery" label="" type="radio"  value="true" checked='${patientDetail.priorSurgery eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>priorSurgeryYes">Yes</label>
									</p>
									<p>
										<aui:input id="priorSurgeryNo" name="priorSurgery" label="" type="radio"  value="false" checked='${patientDetail.priorSurgery eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>priorSurgeryNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="priorSurgeryDetail" label="priorSurgeryDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="chanceOfPregent"></liferay-ui:message>
                         			<p>
                         				<aui:input id="chanceOfPregentYes" name="chanceOfPregent" label="" type="radio"  value="true" checked='${patientDetail.chanceOfPregnent eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>chanceOfPregentYes">Yes</label>
									</p>
									<p>
										<aui:input id="chanceOfPregentNo" name="chanceOfPregent" label="" type="radio"  value="false" checked='${patientDetail.chanceOfPregnent eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>chanceOfPregentNo">No</label>
									</p>
									
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="overAgeof60"></liferay-ui:message>
                         			<p>
                         			<aui:input id="overAgeof60Yes" name="overAgeof60" label="" type="radio"  value="true" checked='${patientDetail.overAge60 eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>overAgeof60Yes">Yes</label>
									</p>
									<p>
									<aui:input id="overAgeof60No" name="overAgeof60" label="" type="radio"  value="false" checked='${patientDetail.overAge60 eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>overAgeof60No">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="labsDone"></liferay-ui:message>
                         			<p>
                         				<aui:input id="labsDoneYes" name="labsDone" label="" type="radio"  value="true" checked='${patientDetail.labsDone eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>labsDoneYes">Yes</label>
									</p>
									<p>	
									<aui:input id="labsDoneNo" name="labsDone" label="" type="radio"  value="false" checked='${patientDetail.labsDone eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>labsDoneNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="allergic"></liferay-ui:message>
                         			<p>
                         			<aui:input id="allergicYes" name="allergic" label="" type="radio"   value="true" checked='${patientDetail.allergic eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>allergicYes">Yes</label>
									</p>
									<p>
									<aui:input id="allergicNo" name="allergic" label="" type="radio"   value="false" checked='${patientDetail.allergic eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>allergicNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="alergicDetail" label="alergicDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="diabetic"></liferay-ui:message>
                         			<p>
                         			<aui:input id="diabeticYes" name="diabetic" label="" type="radio"  value="true" checked='${patientDetail.diabetic eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>diabeticYes">Yes</label>
									</p>
									<p>
									<aui:input id="diabeticNo" name="diabetic" label="" type="radio"  value="false" checked='${patientDetail.diabetic eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>diabeticNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="diabeticDetail" label="diabeticDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="hypertension"></liferay-ui:message>
                         			<p>
                         				<aui:input id="hypertensionYes" name="hypertension" label="" type="radio"  value="true" checked='${patientDetail.hyperTension eq true ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>hypertensionYes">Yes</label>
									</p>
									<p>
										<aui:input id="hypertensionNo" name="hypertension" label="" type="radio"  value="false" checked='${patientDetail.hyperTension eq false ? true : false }'>
										</aui:input>
										<label for="<portlet:namespace/>hypertensionNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="cancer"></liferay-ui:message>
                         			<p>
                         			<aui:input id="cancerYes" name="cancer" label="" type="radio"  value="true" checked='${patientDetail.cancer eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>cancerYes">Yes</label>
									</p>
									<p>
									<aui:input id="cancerNo" name="cancer" label="" type="radio"  value="false" checked='${patientDetail.cancer eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>cancerNo">No</label>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			
                         			<liferay-ui:message key="allergicToIdodine"></liferay-ui:message>
                         			<p>
                         			<aui:input id="allergicToIdodineYes" name="allergicToIdodine" label="" type="radio"  value="true" checked='${patientDetail.allergicToIdodine eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>allergicToIdodineYes">Yes</label>
									</p>
									<p>
									<aui:input id="allergicToIdodineNo" name="allergicToIdodine" label="" type="radio"  value="false" checked='${patientDetail.allergicToIdodine eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>allergicToIdodineNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="allergicToIdodineDetail" label="allergicToIdodineDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<liferay-ui:message key="bloodthinners"></liferay-ui:message>
                         			<p>
                         			<aui:input id="bloodthinnersYes" name="bloodthinners" label="" type="radio"  value="true" checked='${patientDetail.bloodthinners eq true ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>bloodthinnersYes">Yes</label>
									</p>
									<p>
									<aui:input id="bloodthinnersNo" name="bloodthinners" label="" type="radio"  value="false" checked='${patientDetail.bloodthinners eq false ? true : false }'>
									</aui:input>
									<label for="<portlet:namespace/>bloodthinnersNo">No</label>
									</p>
									<p>
										<aui:input type="text" name="bloodthinnersDetail" label="bloodthinnersDetail" style="display:none"/>
									</p>
                         		</div>
                         	</div>
                         	<aui:input type="hidden" name="patientId" value="${patient.patientId }"/>
                         	<div class="col-sm-12">
                         		<div class="input-group">
                         			<aui:button type="button" value="Add Patient Detail"  cssClass="addPatientMoreDetailBtn"/>
                         		</div>
                         	</div>
                         </div>	 
				</aui:form>
			</div>
		</div>
	</div>		
</div>
<aui:script>
var resourceCount=0;
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters','aui-datepicker', function(A) {
	var addPatientMoreDetailBtn= A.one(".addPatientMoreDetailBtn");
	var claustrophobicDetail = A.one("#claustrophobicDetailDiv");
	claustrophobicDetail.hide();
	addPatientMoreDetailBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />patientMoreDetailFm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />patientMoreDetailFm.submit();
		}
	});
});	
</aui:script>	