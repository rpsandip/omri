<%@ include file="/init.jsp" %>

<portlet:renderURL var="createUserURL">
        <portlet:param name="mvcRenderCommandName" value="/create-user" />
</portlet:renderURL>
<liferay-ui:success key="user.added.successfully" message="user.added.successfully"/>
<aui:button name="createUser" value="Create User" href="${createUserURL}"/>
<br/>
<br/>
<script>
    define._amd = define.amd;
    define.amd = false;
</script>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css" type="text/css">
</link>
<script>
    define.amd = define._amd;
</script>

<div>
	<table id="users" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email Address</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${childUserBeanList }" var="userBean">
            <tr>
                <td>${userBean.user.firstName }</td>
                <td>${userBean.user.lastName }</td>
                <td>${userBean.user.emailAddress }</td>
            </tr>
           </c:forEach>
	</table>
</div>

<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#users').DataTable({
            		 "order": []
            	 });
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>