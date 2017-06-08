<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.omri.service.common.util.PatientStatus"%>
<%@page import="javax.portlet.WindowState"%>

<%@ include file="/init.jsp" %>
<liferay-ui:success key="patient.added.successfully" message="patient.added.successfully"/>
<br/><br/>
<div>
  <h3>Patients</h3>
</div>
<div>
	<table id="example" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>Phone No</th>
				<th>Procedure</th>
				<th>Status</th>
				<th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${patientBeanList }" var="patientBean">
            <tr>
                <td>${patientBean.firstName }</td>
                <td>${patientBean.lastName }</td>
                <td>${patientBean.phoneNo }</td>
                <td>
                	<ul>
                	<c:forEach items="${patientBean.resourceBeanList }" var="resourceBean">
                		<li>
                		 ${resourceBean.resourceName }(${ resourceBean.specificationName}) :
                		 ${resourceBean.occurnace }
                		</li>
                	</c:forEach>
                	</ul>
                </td>
                <td>
                	<c:set var="patientStatus" value="${patientBean.patientClinic.patient_status}"/>	
                	<%
                	String patientStatus="";
                    if(Validator.isNotNull(PatientStatus.findByValue((int)pageContext.getAttribute("patientStatus")))){
                    	patientStatus = PatientStatus.findByValue((int)pageContext.getAttribute("patientStatus")).getLabel();
                    }
                	 %>
                	 <%=patientStatus %>
                </td>
                <td>
                   <c:if test="${hasSchedulePatientPermission }">
	                	<portlet:renderURL var="shcedulePatientURL" windowState="<%=WindowState.MAXIMIZED.toString() %>">
	       					 <portlet:param name="mvcRenderCommandName" value="/schedule_patient" />
	       					 <portlet:param name="patientId" value="${patientBean.patientId }" />
	       					 <portlet:param name="clinicId" value="${patientBean.patientClinic.clinicId }" />
						</portlet:renderURL>
						<a href="${shcedulePatientURL }">Schedule Patient</a>
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