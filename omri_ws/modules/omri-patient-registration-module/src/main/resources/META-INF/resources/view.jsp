<%@ include file="/init.jsp" %>
<portlet:resourceURL id="/getPaginatedPatientList" var="getPaginatedPatientListURL">
	 <portlet:param name="roleType" value="${roleType }" />
</portlet:resourceURL>
<portlet:renderURL var="createPatientURL">
        <portlet:param name="mvcRenderCommandName" value="/create-patient" />
</portlet:renderURL>
<liferay-ui:success key="patient.added.successfully" message="patient.added.successfully"></liferay-ui:success>
<liferay-ui:success key="patient.updated.successfully" message="patient.updated.successfully"></liferay-ui:success>

<aui:button name="createPatient" value="Create Patient" href="${createPatientURL}" cssClass="btn btn-primary"/>

<section class="content-header">
  <h1>
    Patients
  </h1>
</section>
<section class="content">
 <div class="row">
 	 <div class="col-xs-12">
     	<div class="box">
            <div class="box-body">
	<table id="example" class="display table table-bordered table-hover table-striped" cellspacing="0" width="100%">
        <tbody>
           <%--  <c:forEach items="${patientBeanList }" var="patientBean">
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
           </c:forEach> --%>
          </tbody> 
	</table>
</div>
 </div>
</div> 	
</div>
</section>

<script type="text/javascript">
        var ss="";
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 AUI().use('aui-base','liferay-portlet-url', function(A) {
            	 $('#example').DataTable({
            		 "processing": true,
            	     "serverSide": true,
            	     "ajax": '${getPaginatedPatientListURL}',
            		 "order": [],
            		 "columns": [
            	                    { "data": "firstName", "name" : "FirstName", "title" : "FirstName"  },
            	                    { "data": "lastName", "name" : "LastName" , "title" : "LastName"},
            	                    { "data": "phoneNo", "name" : "Phone No" , "title" : "Phone No"},
            	                    { "data": "procedure", "name" : "Procedure" , "title" : "Procedure",
            	                    	"render": function(data, type, row, meta){
            	                            if(type === 'display'){
            	                            	var prodArray = data.split("~");
            	                            	var displayData = '<ul>';
            	                            	prodArray.forEach(function(entry) {
            	                            		if(entry.length>0){
            	                            			displayData = displayData + '<li>' + entry + '</li>';
            	                            		}
            	                            	});
            	                            	displayData+="</ul>";
            	                                data = displayData;
            	                            }
            	                            return data;
            	                         }
            	                    	
            	                    },
            	                    { "data": "action", "name" : "Action" , "title" : "Action",
            	                    	"render": function(data, type, row, meta){
            	                    		var displayData="";
            	                    		//AUI().use('aui-base','liferay-portlet-url', function(A) {
            	                    			var editPatientURL = Liferay.PortletURL.createRenderURL();
            	                    			editPatientURL.setParameter("patientId",row.patientId);
            	                    			editPatientURL.setPortletId("<%=themeDisplay.getPortletDisplay().getId() %>");
            	                    			editPatientURL.setParameter("mvcRenderCommandName","/create-patient");
            	                    			displayData = '<a href="'+editPatientURL+'" class="btn btn-block btn-primary">Edit</a>';
            	                    		//});
            	                    		console.log("displayDate ->"+ displayData);
            	                    		return displayData;
            	                    	 }
            	                    }
            	                ]
            	 });
            	//console.log("example->" + $("#example").val());
            });
          });
        })(jQuery);
    </script>