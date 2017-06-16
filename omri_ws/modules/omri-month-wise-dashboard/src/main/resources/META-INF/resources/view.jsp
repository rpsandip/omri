<%@page import="javax.portlet.RenderRequest"%>
<%@ include file="/init.jsp" %>

 <div class="row">
	<div class="col-xs-12">
    	<div class="box">
       		<div class="box-body">
				<div class="form-group col-md-6">
				<aui:select name="year" label="Select Year">
				<%
					int startYear = (Integer)renderRequest.getAttribute("startYear");
					int endYear = (Integer)renderRequest.getAttribute("endYear");
					for(int i=0; i<=(endYear-startYear); i++){
				%>	
					<aui:option value="<%=startYear+i%>"><%=startYear+i %></aui:option>	
				<% 		
					}
				%>
				</aui:select>
				</div>
				<div class="form-group col-md-6">
				<aui:select name="month" label="Select Month">
					<aui:option value="0">January</aui:option>	
					<aui:option value="1">February</aui:option>	
					<aui:option value="2">March</aui:option>	
					<aui:option value="3">April</aui:option>	
					<aui:option value="4">May</aui:option>	
					<aui:option value="5">June</aui:option>	
					<aui:option value="6">July</aui:option>	
					<aui:option value="7">August</aui:option>	
					<aui:option value="8">September</aui:option>	
					<aui:option value="9">October</aui:option>	
					<aui:option value="10">November</aui:option>	
					<aui:option value="11">December</aui:option>	
				</aui:select>
				</div>
				<br/>
				<a href="${shcedulePatientURL }" class="btn btn-primary search-patient">Schedule</a>
			</div>
		</div>
	</div>
</div>			
<!-- <a class="cal-month" data-month="0">January</a>
<a class="cal-month" data-month="1">February</a>
<a class="cal-month" data-month="2">March</a>
<a class="cal-month" data-month="3">April</a>
<br/> <br/> <br/>
<a class="cal-month" data-month="4">May</a>
<a class="cal-month" data-month="5">June</a>
<a class="cal-month" data-month="6">July</a>
<a class="cal-month" data-month="7">August</a>
<br/> <br/> <br/>
<a class="cal-month" data-month="8">September</a>
<a class="cal-month" data-month="9">October</a>
<a class="cal-month" data-month="10">November</a>
<a class="cal-month" data-month="11">December</a>
 -->
						
<aui:script>
var pns = '<portlet:namespace/>'
AUI().use('aui-io-request' ,'aui-base','liferay-portlet-url', function(A) {
	/* A.all('.cal-month').each(function() {
		this.on('click', function(){
				var year = A.one("#"+pns+"year").val();
				var createTaskRenderURL = Liferay.PortletURL.createRenderURL();
				   createTaskRenderURL.setParameter("month",this.getData('month'));
				   createTaskRenderURL.setParameter("year",year);
				   createTaskRenderURL.setParameter("mvcRenderCommandName","/monthwise-patients");
				   createTaskRenderURL.setPortletId('com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet');
			   window.location.href= createTaskRenderURL.toString();
		});
	}); */

	A.one(".search-patient").on('click', function(){
		var year = A.one("#"+pns+"year").val();
		var month =A.one("#"+pns+"month").val();
		var createTaskRenderURL = Liferay.PortletURL.createRenderURL();
		   createTaskRenderURL.setParameter("month",month);
		   createTaskRenderURL.setParameter("year",year);
		   createTaskRenderURL.setParameter("mvcRenderCommandName","/monthwise-patients");
		   createTaskRenderURL.setPortletId('com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet');
	   window.location.href= createTaskRenderURL.toString();
});
});


</aui:script>

