<%@ include file="/init.jsp" %>

<portlet:renderURL var="createPatientURL">
        <portlet:param name="mvcRenderCommandName" value="/create-patient" />
</portlet:renderURL>
<liferay-ui:success key="patient.added.successfully" message="patient.added.successfully"></liferay-ui:success>
<liferay-ui:success key="patient.updated.successfully" message="patient.updated.successfully"></liferay-ui:success>

<aui:button name="createPatient" value="Create Patient" href="${createPatientURL}" cssClass="btn btn-primary"/>
<br/>
<br/>
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
                	<portlet:renderURL var="editPatientURL">
       					 <portlet:param name="mvcRenderCommandName" value="/create-patient" />
       					  <portlet:param name="patientId" value="${ patientBean.patientId}" />
					</portlet:renderURL>
					 <a href="${editPatientURL }" class="btn btn-block btn-primary">Edit</a>
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