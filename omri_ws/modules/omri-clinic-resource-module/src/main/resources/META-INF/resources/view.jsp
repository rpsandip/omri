<%@ include file="/init.jsp" %>

<div>
  <h3>Resources</h3>
</div>
<div>
	<table id="resources" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Resource Name</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${resourceList }" var="resource">
            <tr>
                <td>${resource.resourceName }</td>
            </tr>
           </c:forEach>
	</table>
</div>


<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#resources').DataTable({
            		 "order": []
            	 });
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>