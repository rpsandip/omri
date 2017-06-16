<%@ include file="/init.jsp" %>

<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.omri.service.common.util.PatientStatus"%>
<%@page import="javax.portlet.WindowState"%>
<section class="content-header">
  <h1>
     <h3>${month} ${year}</h3>
  </h1>
</section>
<section class="content">
 <div class="row">
 	 <div class="col-xs-12">
     	<div class="box">
            <div class="box-body">
	<table id="month-wise-patient" class="display table table-bordered table-hover table-striped">
		<thead>
            <tr>
                <th>FirstName</th>
                <th>LastName</th>
                <th>Phone No</th>
				<th>Procedure</th>
				<th>Status</th>
                <th>Attachment</th>
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
                <td class="text-red">
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
                	<div>
                	LOP : 
                	 <c:forEach items="${patientBean.lopDocuments }" var="lopDocument">
                			<a href="${lopDocument.downLoadURL }">
                				<img src="<%=themeDisplay.getPathThemeImages()+"/document.png"%>">
                			</a>	
                		</c:forEach>
                	</div>	
                	<div>
                		Invoice : 
                		<c:forEach items="${patientBean.invoiceDocuments }" var="invoiceDocument">
                			<a href="${invoiceDocument.downLoadURL }">
                				<img src="<%=themeDisplay.getPathThemeImages()+"/document.png"%>">
                			</a>	
                		</c:forEach>
                	</div>
                	<div>
                		Order : 
                		<c:forEach items="${patientBean.orderDocuments }" var="orderDocument">
                			<a href="${orderDocument.downLoadURL }">
                				<img src="<%=themeDisplay.getPathThemeImages()+"/document.png"%>">
                			</a>	
                		</c:forEach>
                	</div>
                	<div>
                		Procedure : 
                		<c:forEach items="${patientBean.procedureDocumnts }" var="procedureDocument">
                			<a href="${procedureDocument.downLoadURL }">
                				<img src="<%=themeDisplay.getPathThemeImages()+"/document.png"%>">
                			</a>	
                		</c:forEach>
                	</div>
                </td>
            </tr>
           </c:forEach>
	</table>
</div>
 </div>
</div> 	
</div>
</section>
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