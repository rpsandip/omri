<%@ include file="/init.jsp" %>
<portlet:renderURL var="createClinicURL">
        <portlet:param name="mvcRenderCommandName" value="/create-clinic" />
</portlet:renderURL>

<aui:button name="createClinic" value="Create Clinic" href="${createClinicURL}"/>