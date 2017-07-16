<%@ include file="/init.jsp" %>
<liferay-ui:success key="appointment-created-successfully" message="appointment-created-successfully"/>
<liferay-ui:error key="error-create-appointment" message="error-create-appointment"/>
<liferay-ui:error key="clinic-resource-error" message="clinic-resource-error"/>
<liferay-ui:error key="no-available-slot-appointment" message="no-available-slot-appointment"/>
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
 <div class="content-wrapper">
<section class="content-header calender-page-view">
   <h1>
     Appointment
   </h1>
 </section>
 <section class="content calender-page-view">
 	 <div class="row">
	        <div class="col-md-4">
	          <div class="box box-body box-solid">
	          	<div>
	          		First Name : </strong><span style="padding-left: 10px;">${patientBean.firstName }
	          	</div>
	          	<div>
	          		Last Name : </strong><span style="padding-left: 10px;">${patientBean.lastName }
	          	</div>
	          	<div>
	          		Phone No : </strong><span style="padding-left: 10px;"> ${patientBean.phoneNo }
	          	</div>
	          </div>
	          <div class="box box-solid">
	            <div class="box-header with-border">
	              <h4 class="box-title">Appointment List</h4>
	            </div>
	            <div class="box-body appointment-list">
	             <c:forEach items="${patientAppointmentList }" var="patientAppointmentBean">
						<div class="col-md-12">
	              			<p><b>Clinic : </b> ${patientAppointmentBean.clinicName }</p>
	              			<p><b>Date : </b> <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value="${patientAppointmentBean.appointmentDate}" /></p>
	              			<p><b>Resources : </b> ${patientAppointmentBean.resourceName }(${patientAppointmentBean.specificationName })</p>
	              			<hr />
	              		</div>	 
				  </c:forEach>
	            </div>
	            <!-- /.box-body -->
	          </div>
	          <div class="box box-solid">
	          	<div class="box-header with-border">
	              <h4 class="box-title">Create Appointment</h4>
	            </div>
	            <div class="box-body create-appointemetn">
	              <aui:form name="crateAppointmentFrm" action="${addAppointmentURL}" cssClass="row contact_form">
	                <div class="form-group col-md-12">
	                 	<aui:select name="appointmentClinic" label="Clinic">
							<c:forEach items="${clinicList }" var="clinicMaster">
								<option value="${clinicMaster.clinicId }"   <c:if test='${ clinicMaster.clinicId eq patientBean.patientClinic.clinicId }'>selected</c:if>>${clinicMaster.clinicName }</option>
							</c:forEach>	
						</aui:select>
	                </div>
	                <div class="form-group col-md-12">
	                	<aui:select name="resource" label="Resource">
							<c:forEach items="${patientBean.resourceBeanList }" var="resource">
								 <aui:option value="${resource.resourceId },${resource.specificationId },${ resource.occurnace}">${resource.resourceName }(${resource.specificationName }) : ${ resource.remainingOccurance}</aui:option>
							</c:forEach>
						</aui:select>
	                </div>
	                <div class="form-group col-md-6">
	                  <aui:input name="appointmentDate" label="Appointment Date" cssClass="form-control">
								<aui:validator name="required" />
					  </aui:input>
	                </div>
	                <div class="col-md-6  app-time">
	                <div class="bootstrap-timepicker">
		                <div class="form-group">
		                 <aui:input name="appointmentTime" label="Appointment Time" cssClass="form-control">
								<aui:validator name="required" />
						 </aui:input>
		                </div>
	                </div>
	                 <aui:input  type="hidden" name="patientId"  value="${ patientBean.patientId}"/>
          		     <aui:input  type="hidden" name="clinicId" value="${ patientBean.patientClinic.clinicId}"/>
	                </div>
	               	<div class="col-sm-12">
                       	<aui:button type="button" value="Create Appointment"  cssClass="createAppointmentBtn btn btn-primary"/>
                  	</div>
	              </aui:form>
	            </div>
	          </div>
	         </div>
	         
	         <div class="col-md-8">
	          <div class="box box-primary">
	            <div class="box-body no-padding">
	              <form id="searchcal">
	              	<div class="form-group col-md-3">
	                  <input type="text" class="form-control" name="filterDate" id="filterDate"/>
	                </div>
	              	<div class="form-group col-md-3">
	                  <select name="clinic" id="filterClinic" class="form-control">
						<option value="0">Select Clinic</option>
							<c:forEach items="${clinicList }" var="clinicMaster">
								<option value="${clinicMaster.clinicId }">${clinicMaster.clinicName }</option>
							</c:forEach>	
						</select>
	                </div>
	                <div class="form-group col-md-3">
	                  <select name="resource" id="filterResource" class="form-control">
						<option value="0">Select Resource</option>
						<c:forEach items="${resourceList }" var="resourceMaster">
							<option value="${resourceMaster.resourceId }">${resourceMaster.resourceName }</option>
						</c:forEach>	
					  </select>
	                </div>
	                <div class="form-group col-md-3">
	                  <input type=button class="btn btn-primary"  value="Search" id="filterSearch">
	                </div>
	              </form>
	              <!-- THE CALENDAR -->
	              <div id='calendar'></div>
	            </div>
	            <!-- /.box-body -->
	          </div>
	        </div> 
	     </div>    
 </section>
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
jQuery.noConflict();
(function($) {
    $(function() {
				var appountmentDate=$('#<portlet:namespace/>appointmentDate');
				var filterDate=$('#filterDate');
				
				var date_input=$('#<portlet:namespace/>appointmentDate');
				dd = date_input;
				var options={
				        format: 'mm/dd/yyyy',
				        todayHighlight: true,
				        autoclose: true,
				      };
				appountmentDate.datepicker(options).on('changeDate', function(ev) {
					  AUI().use('aui-base','aui-form-validator', function(A) {
							var myFormValidator = Liferay.Form.get('<portlet:namespace />crateAppointmentFrm').formValidator;
							myFormValidator.validateField('<portlet:namespace />appointmentDate');
						});
				  });
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
				
				$(".container").css("width","100%");
				
    });
})(jQuery);
</script>