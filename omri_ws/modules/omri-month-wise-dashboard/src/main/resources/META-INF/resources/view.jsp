<%@page import="javax.portlet.RenderRequest"%>
<%@ include file="/init.jsp" %>
<portlet:resourceURL id="getPatientReports" var="getPatientsListURL"></portlet:resourceURL>
<div class="row">
	<div class="col-xs-12">
    	<div class="box">
       		<div class="box-body">
				<div class="form-group col-md-2">
					<aui:input type="text" name="startDate" label="Start Date"/>
				</div>
				<div class="form-group col-md-2">
					<aui:input type="text" name="endDate" label="End Date"/>
				</div>
				<div class="form-group col-md-2">
					<aui:select name="payee" label="Payee" >
	                	<aui:option value="">Select Payee</aui:option>
	                	<aui:option value="Attorney">Attorney</aui:option>
	                	<aui:option value="Cash">Cash</aui:option>
	                	<aui:option value="Doctor">Doctor</aui:option>
	                	<aui:option value="Free">Free</aui:option>
	                	<aui:option value="Insurance">Insurance</aui:option>
	                	<aui:option value="LOP">LOP</aui:option>
	                	<aui:option value="Raheel">Raheel</aui:option>
	                	<aui:option value="Transferred">Transferred</aui:option>
	                	<aui:option value="Funding">Funding</aui:option>
	                </aui:select>
				</div>	
				<div class="form-group col-md-3">
					<aui:select name="doctorId" label="Doctor" >
						<aui:option value="">Select Doctor</aui:option>
						<c:forEach items="${doctorAdminList }" var="doctor">
							<aui:option value="${ doctor.userId}"> ${ doctor.firstName} ${doctor.lastName }</aui:option>
						</c:forEach>
					</aui:select>
				</div>
				<div class="form-group col-md-3">
					<aui:select name="lawyerId" label="Lawyer" >
						<aui:option value="">Select Lawyer</aui:option>
						<c:forEach items="${lawyerAdminList }" var="lawyer">
							<aui:option value="${ lawyer.userId}"> ${ lawyer.firstName}</aui:option>
						</c:forEach>
					</aui:select>
				</div>	
				<br/>
				<a class="btn btn-primary search-patient">Submit</a>
                <button type="button" class="btn btn-success export-report">Export</button>
			</div>
		</div>
	</div>
	<div class="col-md-12 col-sm-12 col-xs-12 text-center">
	   <table id="patients" class="table table-striped" cellspacing="0" width="100%">
        	<tbody></tbody>
       </table>
    </div>
</div>
<c:set var="docimage" value="<%=themeDisplay.getPathThemeImages()+"/document.png"%>"/>
<script type="text/javascript">
var patientFn={};
jQuery.noConflict();
(function($) {
    $(function() {
	  //Date picker
	  $('#'+ '<portlet:namespace/>' + 'startDate').datepicker({
	    autoclose: true
	  });
	  
	  $('#'+ '<portlet:namespace/>' + 'endDate').datepicker({
		    autoclose: true
	  });
	  
	  console.log("isCliniAdmin ->" + '${isClinicAdmin}');
	  
	  var pns = '<portlet:namespace/>'
      AUI().use('aui-io-request' ,'aui-base','liferay-portlet-url', function(A) {
		  	
		  	function createResourceURL(resourceName){
		  		
		  		var resourceURL= Liferay.PortletURL.createResourceURL();
		  		 resourceURL.setPortletId('com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet');
		  		 resourceURL.setResourceId(resourceName);
		  		 resourceURL.setParameter("startDate",A.one("#<portlet:namespace/>startDate").val());
		  		 resourceURL.setParameter("endDate",A.one("#<portlet:namespace/>endDate").val());
		  		 resourceURL.setParameter("payee",A.one("#<portlet:namespace/>payee").val());
		  		 resourceURL.setParameter("doctorId",A.one("#<portlet:namespace/>doctorId").val());
		  		 resourceURL.setParameter("lawyerId",A.one("#<portlet:namespace/>lawyerId").val());
		  		 resourceURL.setParameter("isLawyerAdmin",'${isLawyerAdmin}');
		  		 resourceURL.setParameter("isClinicAdmin", '${isClinicAdmin}');
		  		 resourceURL.setParameter("isDoctorAdmin",'${isDoctorAdmin}');
		  		 
		  		 return resourceURL.toString();
		  	}
		  	
		  	function searchPatients(){
		  		patientDataTables.ajax.url(createResourceURL('getPatientReports')).load();
		  	}
		  	
		  	A.one(".search-patient").on('click',function(){
		  		searchPatients();
		  	});
		  	
		  	A.one(".export-report").on('click',function(){
		  		exportPatients();
		  	});
		  	
		  	function exportPatients(){
				window.location.href=createResourceURL('export-patients');
			}
		  		  	
		  	  var resourceURL=  createResourceURL('getPatientReports');		
		  	  
		  	  console.log("resourceURL ->" + resourceURL.toString());
		
		  	  var docImage = '${docimage}';

		  	  var patientDataTables =  $('#patients').DataTable({
				 "processing": true,
			     "serverSide": true,
			     "searching": false,
			     "pageLength": 10,
			     "ajax": resourceURL.toString(),
				 "order": [],
				 "columns": [
				             { "data": "name", "name" : "name" , "title" : "Name"},
			                 { "data": "customPatientId", "name" : "customPatientId", "title" : "Patient Id"  },
				           	 { "data": "dos", "name" : "dos", "title" : "DOS"  },
				           	 { "data": "payee", "name" : "payee", "title" : "Payee"  },
				           	 { "data": "clinic", "name" : "clinic", "title" : "Clinic"  },
				           	 { "data": "resources", "name" : "resources" , "title" : "Resource",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
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
				           	 { "data": "status", "name" : "status", "title" : "Status"  },
	     	                 { "data": "lop", "name" : "lop" , "title" : "Lop Doc.",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
	     	                            	var displayData = '';
	     	                            	prodArray.forEach(function(entry) {
	     	                            		if(entry.length>0){
	     	                            			displayData = displayData + '<a target="_blank" href="'+entry+'"><img src='+docImage+'></a>';
	     	                            		}
	     	                            	});
	     	                                data = displayData;
	     	                            }
	     	                            return data;
	     	                         }
	     	                 },
	     	                { "data": "lopRequest", "name" : "lopRequest" , "title" : "Lop Request Doc.",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
	     	                            	var displayData = '';
	     	                            	prodArray.forEach(function(entry) {
	     	                            		if(entry.length>0){
	     	                            			displayData = displayData + '<a target="_blank" href="'+entry+'"><img src='+docImage+'></a>';
	     	                            		}
	     	                            	});
	     	                                data = displayData;
	     	                            }
	     	                            return data;
	     	                         }
	     	                 },
	     	                { "data": "invoice", "name" : "invoice" , "title" : "Invoice Doc.",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
	     	                            	var displayData = '';
	     	                            	prodArray.forEach(function(entry) {
	     	                            		if(entry.length>0){
	     	                            			displayData = displayData + '<a target="_blank" href="'+entry+'"><img src='+docImage+'></a>';
	     	                            		}
	     	                            	});
	     	                                data = displayData;
	     	                            }
	     	                            return data;
	     	                         }
	     	                 },
	     	                { "data": "order", "name" : "order" , "title" : "Order Doc.",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
	     	                            	var displayData = '';
	     	                            	prodArray.forEach(function(entry) {
	     	                            		if(entry.length>0){
	     	                            			displayData = displayData + '<a target="_blank" href="'+entry+'"><img src='+docImage+'></a>';
	     	                            		}
	     	                            	});
	     	                                data = displayData;
	     	                            }
	     	                            return data;
	     	                         }
	     	                 },
	     	                { "data": "procedure", "name" : "procedure" , "title" : "Procedure Doc.",
	     	                    	"render": function(data, type, row, meta){
	     	                            if(type === 'display'){
	     	                            	var prodArray = data.split(",");
	     	                            	var displayData = '';
	     	                            	prodArray.forEach(function(entry) {
	     	                            		if(entry.length>0){
	     	                            			displayData = displayData + '<a target="_blank" href="'+entry+'"><img src='+docImage+'></a>';
	     	                            		}
	     	                            	});
	     	                                data = displayData;
	     	                            }
	     	                            return data;
	     	                         }
	     	                 },
	     	                { "data": "documentStatus", "name" : "documentStatus" , "title" : "Document Status.",
     	                    	"render": function(data, type, row, meta){
     	                            if(type === 'display'){
     	                            	var displayData = '';
     	                            	displayData = '<a class="btn btn-block btn-primary verify-doc" data-patient-id="'+row.patientId+'"  data-clinic-id="'+row.clinicId+'">'+row.documentStatus+'</a>';
     	                                data = displayData;
     	                            }
     	                            return data;
     	                         }
     	                 }

			                ]
			 });
		  	 
		  	
		  	$(document).on('click', '.verify-doc', function(){
		  	    console.log("jyyys" + $(this).data('patient-id') +" "+ $(this).data('clinic-id'));
		  	    
		  	  var resourceURL= Liferay.PortletURL.createResourceURL();
		  		 resourceURL.setPortletId('com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet');
		  		 resourceURL.setResourceId('verify-document');
		  		 resourceURL.setParameter("patientId", $(this).data('patient-id'));
		  		resourceURL.setParameter("clinicId", $(this).data('clinic-id'));
		  		
		  	    
		  	  A.io.request(resourceURL.toString(),{
					dataType: 'json',
					method: 'GET',
					on: {
					success: function() {
						var response=this.get('responseData');
						console.log("response->"+response.status);
						if(response.status==="success"){
							$(this).text('Approved');
						}else{
							alert("Error while update document status.");
						}
						
					}
				  }
				
				});
		  	});
		  	
	   });
	  
    });
})(jQuery);
</script>  	