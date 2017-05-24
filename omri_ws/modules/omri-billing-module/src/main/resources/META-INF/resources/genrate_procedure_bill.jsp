<%@ include file="/init.jsp" %>
<liferay-ui:success key="mail.sent.successfully" message="mail.sent.successfully"/>
<liferay-ui:success key="invoice.save.successfully" message="invoice.save.successfully"/>


<portlet:resourceURL id="/generate-invoice" var="generateInvoiceURL">
	<portlet:param name="procedureId" value="${procedureId}"></portlet:param>
</portlet:resourceURL>

<portlet:actionURL var="sendMailURL" name="/send-mail">
<portlet:param name="procedureId" value="${procedureId}"></portlet:param>
</portlet:actionURL>

<portlet:actionURL var="saveInvoiceURL" name="/save-invoice">
<portlet:param name="procedureId" value="${procedureId}"></portlet:param>
</portlet:actionURL>

<h3>
	Procedure Invoice 
</h3>
<div>
	<div>
		<h5>Patient :</h5>
		<div>
			FirstName : ${procedureBean.appointmentList[0].patientFirtName } <br/>
			LastName : ${procedureBean.appointmentList[0].patientLastName } <br/>
			Phone No : ${procedureBean.appointmentList[0].patientPhoneNo } <br/>
		</div>
	</div>
	
	<div>
		<h5>Clinic :</h5>
		<div>
			${procedureBean.appointmentList[0].clinicName } <br/>
		</div>
	</div>
	
	<div>
		<h5>Physician :</h5>
		<div>
			${procedureBean.appointmentList[0].doctorName } <br/>
		</div>
	</div>
	
	<br/>
	<div>
		<h5>Appointment Detail:</h5>
		<div>
			<ul>
            	 <c:forEach items="${procedureBean.appointmentList }" var="appointmentBean">
            	 	<li>
            	 		<div>
            	 			<h5>Date & Time : </h5> <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${appointmentBean.appointmentDate}" />
            	 		</div>
            	 		<div>
            	 			<h5> Resource : </h5> ${appointmentBean.resourceName }
            	 		</div>
            	 		<div class="resourcePrice">
            	 			<h5> Amount : </h5> $${appointmentBean.price }
            	 		</div>
            	 	</li>
            	 </c:forEach>
            </ul>
		</div>	
	</div>
	<hr/>
	<div>
		<span><b>Total : $${totalAmount }</b></span>
	</div>
	<br/>
	<br/>
	<div>
		<aui:button type="button" value="Generate Invoice"  cssClass="generateReportBtn"/>
	</div>
	<aui:form name="sendMailForm" action="${sendMailURL}" cssClass="row contact_form">
		<h3>Send Mail </h3>
		
		<aui:select name="doctor" label="Doctor" cssClass="form-control patient_select" multiple="multiple">
			<aui:option value="0"> Select Doctor</aui:option>
			<c:forEach items="${doctorAdminList }" var="doctor"> 
				<aui:option value="${doctor.emailAddress }">  ${doctor.firstName } ${doctor.lastName }</aui:option>
			</c:forEach>
		</aui:select>
		
		<aui:select name="lawyer" label="Lawyer" cssClass="form-control patient_select" multiple="multiple">
			<aui:option value="0"> Select Lawyer</aui:option>
			<c:forEach items="${lawyerAdminList }" var="lawyer"> 
				<aui:option value="${lawyer.emailAddress }">  ${lawyer.firstName } ${lawyer.lastName }</aui:option>
			</c:forEach>
		</aui:select>
		
		<div>
			<aui:button type="button" value="Send Mail"  cssClass="sendMailButton"/>
		</div>
	</aui:form>
	<br/>
	<br/>
	<div>
		<aui:button type="button" value="Save Invoice"  cssClass="saveInvoiceBtn"/>
	</div>
</div>

<aui:script>
var totalPrice = 0;
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters','aui-datepicker', function(A) {
	var generateReportBtn= A.one(".generateReportBtn");
	var resourcePriceArray =  A.all(".resourcePrice");
	var sendMailButton= A.one(".sendMailButton");
	var saveInvoiceBtn= A.one(".saveInvoiceBtn");
	
	generateReportBtn.on('click', function(e) {
        window.location.href = '${generateInvoiceURL}';
	});
	
	sendMailButton.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />sendMailForm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />sendMailForm.submit();
		}
	});
	
	saveInvoiceBtn.on('click', function(e) {
		window.location.href = '${saveInvoiceURL}';
	});
});
</aui:script>