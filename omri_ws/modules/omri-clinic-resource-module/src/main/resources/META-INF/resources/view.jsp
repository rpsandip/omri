<%@ include file="/init.jsp" %>

<div>
  <h3>Resources</h3>
</div>

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
            	 $('#resources').DataTable();
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>