<%@ include file="/init.jsp" %>

<portlet:renderURL var="createUserURL">
        <portlet:param name="mvcRenderCommandName" value="/create-user" />
</portlet:renderURL>
<liferay-ui:success key="user.added.successfully" message="user.added.successfully"/>
<aui:button name="createUser" value="Create User" href="${createUserURL}"/>
