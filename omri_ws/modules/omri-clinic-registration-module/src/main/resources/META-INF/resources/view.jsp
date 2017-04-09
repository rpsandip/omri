<%@ include file="/init.jsp" %>
<portlet:renderURL var="createClinicURL">
        <portlet:param name="mvcRenderCommandName" value="/create-clinic" />
</portlet:renderURL>
<liferay-ui:message key="clinc.added.sucessfully"></liferay-ui:message>
<aui:button name="createClinic" value="Create Clinic" href="${createClinicURL}"/>
<br/><br/>
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
                <th>Clinic Name</th>
                <th>Phone No</th>
				<th>Resource</th>
				<th>City</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${clinicBeanList }" var="clinicBean">
            <tr>
                <td>${clinicBean.clinicName }</td>
                <td>${clinicBean.phoneNo }</td>
                <td>
                	<ul>
                	<c:forEach items="${clinicBean.clinicResourceBeanList }" var="clinicResourceBean">
                		<li>
                		 ${clinicResourceBean.resourceName }(${ clinicResourceBean.specificationName}) :
                		 ${clinicResourceBean.operationTime } mins
                		</li>
                	</c:forEach>
                	</ul>
                </td>
                 <td>${clinicBean.city }</td>
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