<%@page import="com.omri.service.common.service.PatientLocalServiceUtil"%>
<%@page import="com.omri.service.common.service.ClinicLocalServiceUtil"%>
<%@page import="com.omri.service.common.model.Clinic"%>
<%@page import="com.omri.service.common.beans.ProcedureBean"%>
<%@page import="com.omri.service.common.model.Patient"%>

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

<div class="content-wrapper">
	<div class="container">
		    <!-- Content Header (Page header) -->
		<section class="content-header">
		      <h1>
		        Invoice
		      </h1>
		</section>
		<section class="invoice">
			<div class="row">
		        <div class="col-xs-12">
		          <h2 class="page-header">
		            <i class="fa fa-globe"></i> ${procedureBean.appointmentList[0].clinicName } 
		            <small class="pull-right">Date: <fmt:formatDate pattern="MM/dd/yyyy" value="<%= new Date() %>" /></small>
		          </h2>
		        </div>
		        <!-- /.col -->
		    </div>
		    <div class="row invoice-info">
		        <div class="col-sm-4 invoice-col">
		          <address>
		            <strong>${procedureBean.appointmentList[0].clinicName }</strong><br>
		         	${clinic.addressLine1 }, <br>
		            ${clinic.addressLine2 }., <br>
		            ${clinic.city }, ${clinic.state} <br>
		            Phone: ${clinic.phoneNo }<br>
		          </address>
		        </div>
		        <!-- /.col -->
		        <div class="col-sm-4 invoice-col">
		          <address>
		            <strong> ${procedureBean.appointmentList[0].patientFirtName } ${procedureBean.appointmentList[0].patientLastName } </strong><br>
		          
		             ${patient.addressLine1 }<br>
		              ${patient.addressLine2 }<br>
		             ${patient.city },  ${patient.state }<br/>
		            Phone: ${patient.phoneNo }<br>
		          </address>
		        </div>
		        <div class="col-sm-4 invoice-col">
		          <b>Physician : ${procedureBean.appointmentList[0].doctorName }</b><br>
		        </div>
		      </div>
		      <div class="row">
		        <div class="col-xs-12 table-responsive">
		          <table class="table table-striped">
		            <thead>
		            <tr>
		              <th>Resource</th>
		              <th>Date & Time</th>
		              <th>Amount</th>
		            </tr>
		            </thead>
		            <tbody>
		            <c:forEach items="${procedureBean.appointmentList }" var="appointmentBean">
					 <tr>
		              <td>${appointmentBean.resourceName }</td>
		              <td><fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${appointmentBean.appointmentDate}" /></td>
		              <td>$${appointmentBean.price }</td>
		            </tr>
		            </c:forEach>
		            </tbody>
		            <tr>
		            	<td>
		            	</td>
		            	<td>
		            		Total: 
		            	</td>
		            	<td>
		            		$${totalAmount }
		            	</td>
		            </tr>
		          </table>
		        </div>
		      </div>
		      <div class="row no-print">
		        <div class="col-xs-12">
		          <aui:button type="button" value='<i class="fa fa-credit-card"></i>Save Invoice'  cssClass="btn btn-success pull-right saveInvoiceBtn"/>
		          <aui:button type="button" value='<i class="fa fa-download"></i> Generate Invoice' style="margin-right: 5px;" cssClass="generateReportBtn btn btn-primary pull-right"/>
		        </div>
		      </div>
		      <div class="box-body">    
				<aui:form name="sendMailForm" action="${sendMailURL}" cssClass="row contact_form">
					<div class="box-body">
					
					<h3>Send Mail </h3>
					<div class="form-group col-md-6">
					<aui:select name="doctor" label="Doctor" cssClass="form-control patient_select" multiple="multiple">
						<aui:option value="0"> Select Doctor</aui:option>
						<c:forEach items="${doctorAdminList }" var="doctor"> 
							<aui:option value="${doctor.emailAddress }">  ${doctor.firstName } ${doctor.lastName }</aui:option>
						</c:forEach>
					</aui:select>
					</div>
					<div class="form-group col-md-6">
					<aui:select name="lawyer" label="Lawyer" cssClass="form-control patient_select" multiple="multiple">
						<aui:option value="0"> Select Lawyer</aui:option>
						<c:forEach items="${lawyerAdminList }" var="lawyer"> 
							<aui:option value="${lawyer.emailAddress }">  ${lawyer.firstName } ${lawyer.lastName }</aui:option>
						</c:forEach>
					</aui:select>
					</div>
					<div>
						<aui:button type="button" value="Send Mail"  cssClass="sendMailButton btn btn-primary"/>
					</div>
					</div>
				</aui:form>
			</div>
		</section>
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