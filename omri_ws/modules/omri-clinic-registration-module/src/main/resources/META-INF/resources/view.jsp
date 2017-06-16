<%@ include file="/init.jsp" %>
<portlet:renderURL var="createClinicURL">
        <portlet:param name="mvcRenderCommandName" value="/create-clinic" />
</portlet:renderURL>
<liferay-ui:success key="clinc.added.sucessfully"/>
<aui:button name="createClinic" value="Create Clinic" href="${createClinicURL}" cssClass="btn btn-primary"/>
<br/><br/>

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
            	$('#example').DataTable({
           		 "order": []
           	 });
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>