<%@ include file="/init.jsp" %>
<%@ page import="com.omri.service.common.model.Appointment" %>
<liferay-ui:success key="patient-scheduled-techonologist-succesfully" message="patient-scheduled-techonologist-succesfully"></liferay-ui:success>
<liferay-ui:success key="patient-updated-sucessfully" message="patient-updated-sucessfully"></liferay-ui:success>
<liferay-ui:success key="report-submitte-successfully" message="report-submitte-successfully"></liferay-ui:success>

<div>
  <h3>Appointments</h3>
</div>
<div>
	<table id="example" class="display table table-bordered table-hover table-striped" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>Phone No</th>
				<th>Resource</th>
				<th>Date Time</th>
				<th>Status</th>
				<th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${appointmentList }" var="appointmentBean">
            <tr>
                <td>${appointmentBean.patientFirtName }</td>
                <td>${appointmentBean.patientLastName }</td>
                <td>${appointmentBean.patientPhoneNo }</td>
                <td>${appointmentBean.resourceName }</td>
                <td><fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${appointmentBean.appointmentDate}" /></td>
                <td class="text-red">${appointmentBean.appointmentStatus }</td>
                <td>
                    <c:if test="${hasPatientAddMoreDetailPermission }">
	                	<portlet:renderURL var="addPatientMoreDetailURL">
	       					 <portlet:param name="mvcRenderCommandName" value="/add_patient_more_detail" />
	       					 <portlet:param name="patientId" value="${appointmentBean.patientId }" />
	       					 <portlet:param name="clinicId" value="${appointmentBean.clinicId }" />
						</portlet:renderURL>
						<a href="${addPatientMoreDetailURL }">Add Patient More detail</a><br/>
					</c:if>
					<c:if test="${hasSheduleForTechnologistPermission }">
					<portlet:actionURL var="scheduleForTechnologistURL" name="/scheduleForTechnologist">
						<portlet:param name="appointmentId" value="${appointmentBean.appointmentId }" />
					</portlet:actionURL>
					<a href="${scheduleForTechnologistURL }">Schedule For Technologist</a><br/>
					</c:if>
					<c:if test="${hasSubmitTechnologistReportPermission }">
					<portlet:renderURL var="scheduleForTechnologistURL">
						<portlet:param name="appointmentId" value="${appointmentBean.appointmentId }" />
						<portlet:param name="mvcRenderCommandName" value="/sublitTechnologistReport" />
					</portlet:renderURL>
					<a href="${scheduleForTechnologistURL }">Submit Technologist Report</a>
					</c:if>
                </td>
            </tr>
           </c:forEach>
	</table>
</div>

<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#example').DataTable({
            		 "order": []
            	 });
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>