<%@ include file="/init.jsp" %>

<portlet:renderURL var="createUserURL">
        <portlet:param name="mvcRenderCommandName" value="/create-user" />
</portlet:renderURL>

<aui:button name="createUser" value="Create User" href="${createUserURL}"/>
