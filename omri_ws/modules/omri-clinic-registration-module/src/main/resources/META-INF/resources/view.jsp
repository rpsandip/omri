<%@ include file="/init.jsp" %>
<portlet:renderURL var="createClinicURL">
        <portlet:param name="mvcRenderCommandName" value="/create-clinic" />
</portlet:renderURL>
<liferay-ui:success key="clinc.added.sucessfully"/>
<aui:button name="createClinic" value="Create Clinic" href="${createClinicURL}"/>
<br/><br/>
<script>
    define._amd = define.amd;
    define.amd = false;
</script>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min.js"></script>
<script src="
https://cdn.datatables.net/autofill/2.2.0/js/dataTables.autoFill.min.js"></script>
<script src="
https://cdn.datatables.net/autofill/2.2.0/js/autoFill.bootstrap.min.js"></script>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.15/css/dataTables.bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="https://cdn.datatables.net/autofill/2.2.0/css/autoFill.bootstrap.min.css" type="text/css">
</link>
<script>
    define.amd = define._amd;
</script>

<div>
	<table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
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
            	$('#example').DataTable( {
        autoFill: true
    } );
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>