<%@ include file="/init.jsp" %>
<portlet:resourceURL id="/getSpecificationList" var="getSpecificationListURL" />
<portlet:actionURL var="addClinicURL" name="/clinic/add_clinic">
</portlet:actionURL>
<aui:form name="addClinicForm" action="${addClinicURL}">
	<aui:input name="clinicName" label="clinic.name">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="addressLine1" label="clinic.addressline1">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="addressLine2" label="clinic.addressline2">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="city" label="clinic.city">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="state" label="clinic.state">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="zip" label="clinic.zip">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="phone" label="clinic.phone">
		<aui:validator name="required" />
	</aui:input>
	<aui:input name="fax" label="clinic.fax">
	</aui:input>
	<hr/>
	<h3><liferay-ui:message key="clinic.admin"></liferay-ui:message></h3>
	
	<aui:input name="firstName" label="clinic.admin.firstName">
	</aui:input>
	<aui:input name="lastName" label="clinic.admin.lastName">
	</aui:input>
	<aui:input name="email" label="clinic.admin.emailaddress">
	</aui:input>
	<aui:input name="phone" label="clinic.admin.phone">
	</aui:input>
	
	<hr/>
	<h3><liferay-ui:message key="resources"></liferay-ui:message></h3>
	<div id="resources">
		<div class="lfr-form-row lfr-form-row-inline">
			<div class="row-fields">
	                <aui:select name="resource0" id="resource0" label="Resource" cssClass="resourceItem">
               			 <c:forEach items="${resourceList }" var="resource">
                  			 <aui:option value="${resource.resourceId }">${resource.resourceName }</aui:option>
               			 </c:forEach>
             		</aui:select>
             		<aui:select name="specification0" id="specification0" label="Specification" cssClass="specificationItem">
             		</aui:select>
	                <aui:input fieldParam='operationTime0' id='operationTime0' name="operationTime0" label="Operation Time" placeholder="mins"/>
	                
	                <aui:input name="resourceCount" type="hidden"  />
	                
	                <aui:script use="liferay-auto-fields">
   					 new Liferay.AutoFields({
   						 contentBox: '#resources',	
    					 fieldIndexes: '<portlet:namespace/>resourceIndexes',
    					 on :{
    					     'clone' : function(newField){
    					    	 
    					    	    var resourceItemList = A.all('.resourceItem');
    					    	    resourceItemList.each(function() {
    					    			var currentNode = this;
    					    			currentNode.on('change', function(e) {
    					    				var resourceId = currentNode.val();
    					    				A.io.request('${getSpecificationListURL}', {
    								               method: 'post',
    								               data: {
    								            	   '<portlet:namespace/>resourceId': resourceId
    								               },
    								               on: {
    								                    success: function(data) {
    								                      var specificationElement = currentNode.get('parentNode').next().one('.specificationItem');
    								                      specificationElement.all('option').remove();
    								                      var responseSpecificationArray = JSON.parse(this.get('responseData'));
    								                      for(var i=0;i<responseSpecificationArray.length;i++){
    								                    	  specificationElement.append("<option value="+responseSpecificationArray[i].specificationId+" >"+ responseSpecificationArray[i].specificationName +"</option> ");
    								                      }
    								                    }
    								               }
    								        });
    					    			});
    					    			
    					    		});
    					    	    resourceCount++;
    					    	    A.one("#"+clinicRegistrationModuleNs+"resourceCount").val(resourceCount);
    					     },
    					     'delete' : function(){
    					     console.log("delete");
    					     resourceCount--;
    					     A.one("#"+clinicRegistrationModuleNs+"resourceCount").val(resourceCount);
    					     }
    					 }
    				}).render();
    				
              </aui:script>
	        </div>
	    </div>
	   </div> 
	
	<aui:button type="button" value="Add Clinic"  cssClass="createClinicBtn"/>
</aui:form>

<aui:script>
var clinicRegistrationModuleNs =  '<portlet:namespace/>';
var allResourceEventInvoked=false;
var resourceCount=0;
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters', function(A) {
	var createClinicBtn= A.one(".createClinicBtn");
	 A.one("#"+clinicRegistrationModuleNs+"resourceCount").val(resourceCount);
	createClinicBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />addClinicForm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />addClinicForm.submit();
		}
	});
	 var resourceItemList = A.all('.resourceItem');
	 resourceItemList.each(function() {
			var currentNode = this;
			currentNode.on('change', function(e) {
				if(allResourceEventInvoked==false){
					var resourceId = currentNode.val();
					A.io.request('${getSpecificationListURL}', {
			               method: 'post',
			               data: {
			            	   '<portlet:namespace/>resourceId': resourceId
			               },
			               on: {
			                    success: function(data) {
			                      var specificationElement = currentNode.get('parentNode').next().one('.specificationItem');
			                      specificationElement.all('option').remove();
			                      var responseSpecificationArray = JSON.parse(this.get('responseData'));
			                      for(var i=0;i<responseSpecificationArray.length;i++){
			                    	  specificationElement.append("<option value="+responseSpecificationArray[i].specificationId+" >"+ responseSpecificationArray[i].specificationName +"</option> ");
			                      }
			                    }
			               }
			        });
				}
			});
 		});
	
});
</aui:script>