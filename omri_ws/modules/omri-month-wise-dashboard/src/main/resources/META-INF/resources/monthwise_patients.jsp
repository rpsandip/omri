<%@ include file="/init.jsp" %>

<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.omri.service.common.util.PatientStatus"%>
<%@page import="javax.portlet.WindowState"%>
<div>
  <h3>${month} ${year}</h3>
</div>

<div>
	<table id="month-wise-patient" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>LOP Documents</th>
                <th>Invoice Documents</th>
                <th>Order Documents</th>
                <th>Procedure Documents</th>
	        </tr>
        </thead>
        <tbody>
            <c:forEach items="${patientBeanList }" var="patientBean">
            <tr>
                <td>${patientBean.firstName }</td>
                <td>${patientBean.lastName }</td>
                <td>
                	<ul>
                		<c:forEach items="${patientBean.lopDocuments }" var="lopDocument">
                			<li>
                				<a href="${lopDocument.downLoadURL }">${lopDocument.title}</a>
                			</li>	
                		</c:forEach>
                	</ul>
                </td>
                <td>
                	<ul>
                		<c:forEach items="${patientBean.invoiceDocuments }" var="invoiceDocument">
                			<li>
                				<a href="${invoiceDocument.downLoadURL }">${invoiceDocument.title}</a>
                			</li>	
                		</c:forEach>
                	</ul>
                </td>
                 <td>
                	<ul>
                		<c:forEach items="${patientBean.orderDocuments }" var="orderDocument">
                			<li>
                				<a href="${orderDocument.downLoadURL }">${orderDocument.title}</a>
                			</li>	
                		</c:forEach>
                	</ul>
                </td>
                 <td>
                	<ul>
                		<c:forEach items="${patientBean.procedureDocumnts }" var="procedureDocument">
                			<li>
                				<a href="${procedureDocument.downLoadURL }">${procedureDocument.title}</a>
                			</li>	
                		</c:forEach>
                	</ul>
                </td>
            </tr>
           </c:forEach>
	</table>
</div>
<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#month-wise-patient').DataTable({
            		 "order": []
            	 });
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>