<%@ include file="../init.jsp" %>
<portlet:resourceURL id="/search_user" var="lawyerSearchUserURL"><portlet:param name="adminType" value="lawyer"/></portlet:resourceURL>
<portlet:resourceURL id="/getRoles" var="getlawyerRolesURL"><portlet:param name="adminType" value="lawyer"/></portlet:resourceURL>
<portlet:resourceURL id="/search_user" var="doctorSearchUserURL"><portlet:param name="adminType" value="doctor"/></portlet:resourceURL>
<portlet:resourceURL id="/getRoles" var="getDoctorRolesURL"><portlet:param name="adminType" value="doctor"/></portlet:resourceURL>
<portlet:resourceURL id="/search_user" var="clinicSearchUserURL"><portlet:param name="adminType" value="clinic"/></portlet:resourceURL>
<portlet:resourceURL id="/getRoles" var="getClinicRolesURL"><portlet:param name="adminType" value="clinic"/></portlet:resourceURL>
<portlet:resourceURL id="/getClinicList" var="getClinicListURL"></portlet:resourceURL>
<portlet:actionURL var="createUserActionURL" name="/user/create_user">
</portlet:actionURL>
<div class="container">
	<div class="row ">
		 <div class="col-sm-8 contact_form_area">
		 	<h3 class="contact_section_title"><liferay-ui:message key="add.user"/></h3>
		 	<div class="contactForm row m0">
				<aui:form name="createUserForm" action="${createUserActionURL}" cssClass="row contact_form">
				    <div class="row m0">
				    	<div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="firstName" label="user.firstName" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="lastName" label="user.lastName" cssClass="form-control">
									<aui:validator name="required" />
								 </aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="emailAddress" label="user.emailAdddress" cssClass="form-control">
									<aui:validator name="required" />
									<aui:validator name="email" />
								</aui:input>
                            </div>
                          </div>
				   		  <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="phoneNumber" label="user.phone" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
						  <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="addressLine1" label="user.address1" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>	
						 <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="addressLine2" label="user.address2" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
						<div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="city" label="user.city" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                        </div>
                        <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="state" label="user.state" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                        </div>
						<div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="zip" label="user.zip" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                        </div>
					</div>
					<div class="row m0">
						<div class="col-sm-12">
	                         <div class="input-group">
	                         	<aui:select name="entity">
									<aui:option value="">Select Entity</aui:option>
								    <aui:option value="clinic">Clinic</aui:option>
								    <aui:option value="lawyer">Lawyer</aui:option>
								    <aui:option value="doctor">Doctor</aui:option>
								</aui:select>
	                         </div>
	                    </div>            
						<div class="col-sm-12">
	                         <div class="input-group">
	                         	<aui:select name="clinic">
									<aui:option value="">Select Clinic</aui:option>
								</aui:select>
	                         </div>
	                    </div>     
						<div class="col-sm-12">
	                         <div class="input-group">
	                         	<aui:select name="role" multiple="true" ></aui:select>
	                         </div>
	                    </div> 
	                    <div class="col-sm-12">
	                         <div class="input-group">
	                         	<aui:select name="respectiveParentUserId" label="Respective Admin"></aui:select>
	                         </div>
	                    </div> 
					</div>
					<div class="row m0">
						<aui:button type="button" value="Add User"  cssClass="createUserBtn"/>
					</div>	
					<aui:input name="isAdmin" type="hidden" value="${isAdmin }"/>
					<aui:input name="isLawyerAdmin" type="hidden" value="${isLawyerAdmin }"/>
					<aui:input name="isDoctorAdmin" type="hidden" value="${isDoctorAdmin }"/>
				</aui:form>
            </div>
        </div>
   </div>
</div>
            

<div id="myAutoComplete"></div>

<aui:script>
var userModuleNameSpace =  '<portlet:namespace/>';
AUI().use('aui-io-request', 'aui-autocomplete' ,'aui-base','aui-form-validator','autocomplete-list','autocomplete-filters','autocomplete-highlighters', function(A) {
	var createUserBtn= A.one(".createUserBtn");
	var entity = A.one("#"+userModuleNameSpace+"entity");
	A.one('#'+userModuleNameSpace + 'respectiveParentUserId').get('parentNode').hide(true);
	A.one('#'+userModuleNameSpace + 'role').get('parentNode').hide(true);
	A.one('#'+userModuleNameSpace + 'clinic').get('parentNode').hide(true);
	var clinicSelect = A.one("#"+ userModuleNameSpace + "clinic");
	createUserBtn.on('click', function(e) {
		var myFormValidator = Liferay.Form.get('<portlet:namespace />createUserForm').formValidator;
		myFormValidator.validate();
		if(!myFormValidator.hasErrors()){
			document.<portlet:namespace />createUserForm.submit();
		}
	});
	
	clinicSelect.on('change', function(e) {
		var clinicId = this.val();
		console.log("clinicId->" + clinicId);
		var clinicSearchUserURL='${clinicSearchUserURL}';
		A.io.request(clinicSearchUserURL,{
			dataType: 'json',
			method: 'GET',
			data :{
				'<portlet:namespace />clinicId':clinicId
			},
			on: {
			success: function() {
				A.all('#'+userModuleNameSpace + 'respectiveParentUserId option').remove();
				var userList=this.get('responseData');
				console.log("user Lisr ->" + userList.length);
				for(var i in userList){
				  A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='"+ userList[i].userId +"' >"+ userList[i].firstName +"</option> ");
				}
				A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='0' >No one</option> ");
			}
			}
		});
	});
	
	
	entity.on('change', function(e) {
		console.log("change type ->" + this.val());
		if(this.val()==='lawyer'){
			A.one('#'+userModuleNameSpace + 'respectiveParentUserId').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'role').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'clinic').get('parentNode').hide(true);
			var lawyerSearchURL='${lawyerSearchUserURL}';
			A.io.request(lawyerSearchURL.toString(),{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'respectiveParentUserId option').remove();
					var userList=this.get('responseData');
					for(var i in userList){
					  A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='"+ userList[i].userId +"' >"+ userList[i].firstName +"</option> ");
					}
					A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='0' >No one</option> ");
				}
			}
			});
			
			var getRoleLawyerURL='${getlawyerRolesURL}';
			A.io.request(getRoleLawyerURL.toString(),{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'role option').remove();
					var roleList=this.get('responseData');
					for(var i in roleList){
					  A.one('#<portlet:namespace />role').append("<option  value='"+ roleList[i].roleId +"' >"+ roleList[i].name +"</option> ");
					}
				}
			}
			});
			
			
		}else if(this.val()==='doctor'){
			A.one('#'+userModuleNameSpace + 'respectiveParentUserId').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'role').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'clinic').get('parentNode').hide(true);
			var doctorSearchURL='${doctorSearchUserURL}';
			A.io.request(doctorSearchURL,{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'respectiveParentUserId option').remove();
					var userList=this.get('responseData');
					console.log("user Lisr ->" + userList.length);
					for(var i in userList){
					  A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='"+ userList[i].userId +"' >"+ userList[i].firstName +"</option> ");
					}
					A.one('#<portlet:namespace />respectiveParentUserId').append("<option  value='0' >No one</option> ");
				}
				}
			});
			
			var getRoleDoctorURL='${getDoctorRolesURL}';
			A.io.request(getRoleDoctorURL.toString(),{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'role option').remove();
					var roleList=this.get('responseData');
					for(var i in roleList){
					  A.one('#<portlet:namespace />role').append("<option  value='"+ roleList[i].roleId +"' >"+ roleList[i].name +"</option> ");
					}
				}
			}
			});
		}else if(this.val()==='clinic'){
			console.log("clinic selected");
			A.one('#'+userModuleNameSpace + 'respectiveParentUserId').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'role').get('parentNode').show(true);
			A.one('#'+userModuleNameSpace + 'clinic').get('parentNode').show(true);
			var getClinicListURL='${getClinicListURL}';
			
			A.io.request(getClinicListURL.toString(),{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'clinic option').remove();
					var clinicList=this.get('responseData');
					A.one('#<portlet:namespace />clinic').append("<option  value='0' >"+ "Select Clinic" +"</option> ");
					for(var i in clinicList){
					  A.one('#<portlet:namespace />clinic').append("<option  value='"+ clinicList[i].clinicId +"' >"+ clinicList[i].clinicName +"</option> ");
					}
				}
			}
			});
			
			
			var getClinicRolesURL='${getClinicRolesURL}';
			A.io.request(getClinicRolesURL.toString(),{
				dataType: 'json',
				method: 'GET',
				on: {
				success: function() {
					A.all('#'+userModuleNameSpace + 'role option').remove();
					var roleList=this.get('responseData');
					for(var i in roleList){
					  A.one('#<portlet:namespace />role').append("<option  value='"+ roleList[i].roleId +"' >"+ roleList[i].name +"</option> ");
					}
				}
			}
			});
			
		}else{
			console.log("clinic selected");
			A.one('#'+userModuleNameSpace + 'respectiveParentUserId').get('parentNode').hide(true);
			A.one('#'+userModuleNameSpace + 'role').get('parentNode').hide(true);
		}
	});
	
});
</aui:script>