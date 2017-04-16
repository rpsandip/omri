<%@ include file="/init.jsp" %>

<portlet:renderURL var="createPatientURL">
        <portlet:param name="mvcRenderCommandName" value="/create-patient" />
</portlet:renderURL>

<aui:button name="createPatient" value="Create Patient" href="${createPatientURL}"/>

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

<div>
	<table id="example" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>Phone No</th>
				<th>Procedure</th>
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
                   <portlet:renderURL var="patientDocumentURL">
        					<portlet:param name="mvcRenderCommandName" value="/upload-patient-document" />
        					<portlet:param name="patientId" value="${patientBean.patientId}" />
					</portlet:renderURL>
                   <a href="${patientDocumentURL }">Add Patient Document</a>
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