<%@ include file="../init.jsp" %>
<portlet:resourceURL id="/getSpecificationList" var="getSpecificationListURL" />
<portlet:actionURL var="addClinicURL" name="/clinic/add_clinic">
</portlet:actionURL>
<section class="content-header">
  <h1>
    <liferay-ui:message key="add.clinic"/>
  </h1>
</section>
<div class="row">
	<div class="col-xs-12">
    	<div class="box">
       		<div class="box-body">
				<aui:form name="addClinicForm" action="${addClinicURL}" cssClass="row contact_form">
					 <div class="box-body">
						<div class="form-group col-md-6">
                            	<aui:input name="clinicName" label="clinic.name" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="addressLine1" label="clinic.addressline1" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="addressLine2" label="clinic.addressline2" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="city" label="clinic.city" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="state" label="clinic.state" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
						  <div class="form-group col-md-6">
                            	<aui:input name="zip" label="clinic.zip" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="phone" label="clinic.phone" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                          </div>
                          <div class="form-group col-md-6">
                            	<aui:input name="fax" label="clinic.fax" cssClass="form-control">
                            		<aui:validator name="required" />
                            	</aui:input>	
                          </div>
					<div class="col-md-12 divider-line"><hr></div>
					<h3><liferay-ui:message key="resources"></liferay-ui:message></h3>
						<div id="resources">
							<div class="lfr-form-row lfr-form-row-inline">
								<div class="row-fields">
										<div class="form-group col-md-12">
								                <aui:select name="resource0" id="resource0" label="Resource" cssClass="resourceItem form-control patient_select">
							               			 <c:forEach items="${resourceList }" var="resource">
							                  			 <aui:option value="${resource.resourceId }">${resource.resourceName }</aui:option>
							               			 </c:forEach>
							             		</aui:select>
							             		<aui:select name="specification0" id="specification0" label="Specification" cssClass="form-control specificationItem patient_select"></aui:select>
								                <aui:input fieldParam='operationTime0' id='operationTime0' name="operationTime0" cssClass="form-control" label="Operation Time" placeholder="mins"/>
								                <aui:input fieldParam='price0' id='price0' name="price0" cssClass="form-control" label="Price" placeholder="Price"/>
								                <aui:input name="resourceCount" type="hidden"  />
						               </div> 
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
                            <div class="form-group col-md-12">
                                	<aui:button type="button" value="Add Clinic"  cssClass="createClinicBtn btn btn-primary"/>
                           </div>
                   </div>  
				</aui:form>
			</div>
		</div>
	</div>
</div>			
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