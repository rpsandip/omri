<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.omri.service.common.util.PatientStatus"%>
<%@page import="javax.portlet.WindowState"%>

<%@ include file="/init.jsp" %>
<script>
    define._amd = define.amd;
    define.amd = false;
</script>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css" type="text/css">
</link>
<script>
    define.amd = define._amd;
</script>
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
                	<portlet:renderURL var="shcedulePatientURL" windowState="<%=WindowState.MAXIMIZED.toString() %>">
       					 <portlet:param name="mvcRenderCommandName" value="/schedule_patient" />
       					 <portlet:param name="patientId" value="${patientBean.patientId }" />
       					 <portlet:param name="clinicId" value="${patientBean.patientClinic.clinicId }" />
					</portlet:renderURL>
					<a href="${shcedulePatientURL }">Shedule Patient</a>
                </td>
            </tr>
           </c:forEach>
	</table>
</div>


<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#example').DataTable();
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>