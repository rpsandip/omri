<%@ include file="/init.jsp" %>

<portlet:renderURL var="createPatientURL">
        <portlet:param name="mvcRenderCommandName" value="/create-patient" />
</portlet:renderURL>

<aui:button name="createPatient" value="Create Patient" href="${createPatientURL}"/>
