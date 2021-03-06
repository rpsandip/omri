<%@ include file="/init.jsp" %>
<div>
	<table id="example" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>PatientName</th>
                <th>Phone No</th>
				<th>Appointment Detail</th>
				<th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${procedureBeanList }" var="procedureBean">
            <tr>
                <td>${procedureBean.appointmentList[0].patientFirtName } ${procedureBean.appointmentList[0].patientLastName }</td>
            	<td> ${procedureBean.appointmentList[0].patientPhoneNo } </td>
            	<td>
            		<ul>
            	 <c:forEach items="${procedureBean.appointmentList }" var="appointmentBean">
            	 	<li>
            	 		<div>
            	 			<fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${appointmentBean.appointmentDate}" />
            	 		</div>
            	 		<siv>
            	 			${appointmentBean.resourceName }
            	 		</siv>
            	 		<div>
            	 			$${appointmentBean.price }
            	 		</div>
            	 	</li>
            	 </c:forEach>
            	 	</ul>
            	 </td>
            	 <td>
            	 	<portlet:renderURL var="generateBillURL">
       					 <portlet:param name="mvcRenderCommandName" value="/generateProcedureBill" />
       					 <portlet:param name="procedureId" value="${procedureBean.procedureId }" />
					</portlet:renderURL>
            	 	 <a href="${generateBillURL }" class="btn btn-block btn-primary">Generate Report</a>
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