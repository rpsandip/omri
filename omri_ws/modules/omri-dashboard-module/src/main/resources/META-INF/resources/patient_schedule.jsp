<%@ include file="/init.jsp" %>
<liferay-ui:success key="appointment-created-successfully" message="appointment-created-successfully"/>
<liferay-ui:error key="error-create-appointment" message="error-create-appointment"/>
<liferay-ui:error key="clinic-resource-error" message="clinic-resource-error"/>
<portlet:actionURL var="addAppointmentURL" name="/patient/add_appointment">
</portlet:actionURL>
<portlet:resourceURL id="/getAppointmentList" var="getAppointmentListURL" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>

	body {
		margin: 40px 10px;
		padding: 0;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		font-size: 14px;
	}

	#calendar {
		max-width: 900px;
		margin: 0 auto;
	}

</style>
<div class="container">
	<div style="width: 30%;float: left;">
	<div class="row ">
		 <div class="col-sm-8 contact_form_area">
		 	<h3 class="contact_section_title">Appointment</h3>
		 	<aui:form name="crateAppointmentFrm" action="${addAppointmentURL}" cssClass="row contact_form">
		 	<div class="contactForm row m0">
				<div class="row m0">
					<div class="col-sm-12">
                         <div class="input-group">
                         	<span style="float: left;"><strong>First Name : </strong><span style="padding-left: 10px;">${patientBean.firstName }</span></span>
                         </div>
                    </div>
                    <div class="col-sm-12">
                         <div class="input-group">
                         	<span style="float: left;"><strong>Last Name : </strong><span style="padding-left: 10px;">${patientBean.lastName } </span></span>
                         </div>
                    </div>
                    <div class="col-sm-12">
                         <div class="input-group">
                         	<span style="float: left;"><strong> Phone No : </strong><span style="padding-left: 10px;"> ${patientBean.phoneNo } </span></span>
                         </div>
                    </div>
               </div>
               <hr/> 
               <div class="row m0">
               		<h5>Appointment Created</h5>
               		<c:forEach items="${patientAppointmentList }" var="patientAppointmentBean">
						<div>
							
						    <span>Clinic: ${patientAppointmentBean.clinicName }<span><br/>
						    <span>Date : <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${patientAppointmentBean.appointmentDate}" />
						        </span><br/>
						    <span>Resource : ${patientAppointmentBean.resourceName }(${patientAppointmentBean.specificationName })</span>
						</div>	
						<hr/>	 
					</c:forEach>
               </div>
               <hr/>
               <div class="row m0">
                    
					<aui:select name="appointmentClinic" label="Clinic">
						<c:forEach items="${clinicList }" var="clinicMaster">
							<option value="${clinicMaster.clinicId }"   <c:if test='${ clinicMaster.clinicId eq patientBean.patientClinic.clinicId }'>selected</c:if>>${clinicMaster.clinicName }</option>
						</c:forEach>	
					</aui:select>
               </div>
               <div class="row m0">
					<aui:select name="resource" label="Resource">
						<c:forEach items="${patientBean.resourceBeanList }" var="resource">
							 <aui:option value="${resource.resourceId },${resource.specificationId },${ resource.occurnace}">${resource.resourceName }(${resource.specificationName }) : ${ resource.remainingOccurance}</aui:option>
						</c:forEach>
					</aui:select>
               </div>
               <h4>Appointment Time</h4>
               <div class="row m0">
               		<div class="col-sm-6">
                        <div class="input-group">
                        	<aui:input name="appointmentDate" label="Appointment Date" cssClass="form-control">
								<aui:validator name="required" />
							</aui:input>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="input-group">
                        	<aui:input name="appointmentTime" label="Appointment Time" cssClass="form-control">
								<aui:validator name="required" />
							</aui:input>
                        </div>
                    </div>
               </div>  
               <aui:input  type="hidden" name="patientId"  value="${ patientBean.patientId}"/>
               <aui:input  type="hidden" name="clinicId" value="${ patientBean.patientClinic.clinicId}"/>
               <br/>
               <div class="row m0">
                   <div class="col-sm-12">
                       	<aui:button type="button" value="Create Appointment"  cssClass="createAppointmentBtn"/>
                  </div>
               </div>    
		 </div>
		 </aui:form>
		</div>
	</div>
	</div>
	<div style="width: 70%; float: left;">
	<div>
		Date : <input type="text" name="filterDate" id="filterDate"/>
		Clinic : <select name="clinic" id="filterClinic">
					<option value="0">Select Clinic</option>
						<c:forEach items="${clinicList }" var="clinicMaster">
						<option value="${clinicMaster.clinicId }">${clinicMaster.clinicName }</option>
						</c:forEach>	
					</select>
		Resource : 
				 <select name="resource" id="filterResource">
					<option value="0">Select Resource</option>
					<c:forEach items="${resourceList }" var="resourceMaster">
						<option value="${resourceMaster.resourceId }">${resourceMaster.resourceName }</option>
					</c:forEach>	
				</select>
		       &nbsp;	<button name="Search" id="filterSearch" type="button" >Search</button>		
	</div>
	<br/>
	<div id='calendar'></div>

	</div>
</div>		 

<aui:script>
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters', function(A) {
	var createAppointmentBtn= A.one(".createAppointmentBtn");
	createAppointmentBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />crateAppointmentFrm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />crateAppointmentFrm.submit();
		}
	});
});	
</aui:script>
<script>
var dd ="";
</script>

<script>
			$(document).ready(function() {
				var appountmentDate=$('#<portlet:namespace/>appointmentDate');
				var filterDate=$('#filterDate');
				
				var date_input=$('#<portlet:namespace/>appointmentDate');
				dd = date_input;
				var options={
				        format: 'mm/dd/yyyy',
				        todayHighlight: true,
				        autoclose: true,
				      };
				appountmentDate.datepicker(options);
				filterDate.datepicker(options);
				
				$('#'+'<portlet:namespace/>'+'appointmentTime').timepicker({
					showInputs: false
				});
				
				var patientId= '${ patientBean.patientId}';
				var clinicId= '${ patientBean.patientClinic.clinicId}';
				
				 $('#calendar').fullCalendar({
						header: {
							left: 'prev,next today',
							center: 'title',
							right: 'month,agendaWeek,agendaDay,listWeek'
						},
						defaultDate: new Date(),
						navLinks: true, // can click day/week names to navigate views
						editable: false,
						defaultView : "agendaWeek",
						eventLimit: true, // allow "more" link when too many events
						events:  {
				            url: "${getAppointmentListURL}",
				            type: 'GET',
				            data: {
				            	"<portlet:namespace/>patientId": patientId,
								"<portlet:namespace/>clinicId":clinicId
				            }
				        }
				});
				 
				$("#filterSearch").on('click',function(){
					$.ajax({
						   url: '${getAppointmentListURL}',
						   type: 'GET',
						   data: {
							   "<portlet:namespace/>clinicId":$("#filterClinic").val(),
							   "<portlet:namespace/>resourceId":$("#filterResource").val(),
							   "<portlet:namespace/>filterDate":$("#filterDate").val(),
						   },
						   success: function(data) {
							  var jsonArray = jQuery.parseJSON(data);
							  $('#calendar').fullCalendar( 'changeView', 'agendaDay');
							  $('#calendar').fullCalendar( 'removeEvents');
						     // $('#calendar').fullCalendar( 'addEventSource', jsonArray);         
						      //$('#calendar').fullCalendar( 'refetchEvents' );
							  $('#calendar').fullCalendar('addEventSource', jsonArray);         
				              $('#calendar').fullCalendar('rerenderEvents' );
				              if($("#filterDate").val().length>0){
				              	$('#calendar').fullCalendar( 'gotoDate', $("#filterDate").val());
				              }
						   }
						});
				});
			});
</script>