<%@page import="javax.portlet.RenderRequest"%>
<%@ include file="/init.jsp" %>

<h6>Select Year</h6>
<aui:select name="year">
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


<a class="cal-month" data-month="0">January</a>
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

						
<aui:script>
var pns = '<portlet:namespace/>'
AUI().use('aui-io-request' ,'aui-base','liferay-portlet-url', function(A) {
	A.all('.cal-month').each(function() {
		this.on('click', function(){
				var year = A.one("#"+pns+"year").val();
				var createTaskRenderURL = Liferay.PortletURL.createRenderURL();
				   createTaskRenderURL.setParameter("month",this.getData('month'));
				   createTaskRenderURL.setParameter("year",year);
				   createTaskRenderURL.setParameter("mvcRenderCommandName","/monthwise-patients");
				   createTaskRenderURL.setPortletId('com_omri_portlet_OmriMonthWiseDashboardmvcportletPortlet');
			   window.location.href= createTaskRenderURL.toString();
		});
	});
});


</aui:script>

