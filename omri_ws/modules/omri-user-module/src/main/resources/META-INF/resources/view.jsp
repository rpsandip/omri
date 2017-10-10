<%@ include file="/init.jsp" %>

<portlet:renderURL var="createUserURL">
        <portlet:param name="mvcRenderCommandName" value="/create-user" />
</portlet:renderURL>
<liferay-ui:success key="user.added.successfully" message="user.added.successfully"/>
<aui:button name="createUser" value="Create User" href="${createUserURL}" cssClass="btn btn-primary"/>
<br/>
<br/>
<section class="content">
 <div class="row">
 	 <div class="col-xs-12">
     	<div class="box">
            <div class="box-body">
	<table id="users" class="display table table-bordered table-hover table-striped" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email Address</th>
                <th>Role</th>
                <th>Entity</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${childUserBeanList }" var="userBean">
            <tr>
                <td>${userBean.user.firstName }</td>
                <td>${userBean.user.lastName }</td>
                <td>${userBean.user.emailAddress }</td>
                <td>${userBean.roleName }</td>
                <td>${userBean.entity }</td> 
            </tr>
           </c:forEach>
         </tbody>  
	</table>
</div>
 </div>
</div> 	
</div>
</section>

<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#users').DataTable({
            		 "order": []
            	 });
            });
        })(jQuery);
    </script>