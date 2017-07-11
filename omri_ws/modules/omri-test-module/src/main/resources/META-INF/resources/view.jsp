<%@ include file="/init.jsp" %>

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
                            	 <aui:input name="firstName" label="First Name" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="lastName" label="Last Name" cssClass="form-control">
									<aui:validator name="required" />
								 </aui:input>
                            </div>
                          </div>
                          <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="emailAddress" label="Email Address" cssClass="form-control">
									<aui:validator name="required" />
									<aui:validator name="email" />
								</aui:input>
                            </div>
                          </div>
				   		  <div class="col-sm-6">
                         	<div class="input-group">
                            	 <aui:input name="phoneNumber" label="Phone" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
						  <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="addressLine1" label="Address1" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>	
						 <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="addressLine2" label="Addrses2" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                          </div>
						<div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="city" label="City" cssClass="form-control">
									<aui:validator name="required" />
								</aui:input>
                            </div>
                        </div>
                        <div class="col-sm-6">
                         	<div class="input-group">
                            	<aui:input name="state" label="State" cssClass="form-control">
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
	                         	<aui:select name="Clinic" label="Clinic" cssClass="patient_select">
									<aui:option value="0">Select Clinic</aui:option>
								    <aui:option value="1">Clinic1</aui:option>
								    <aui:option value="2">Clinic2</aui:option>
								    <aui:option value="3">Clinic3</aui:option>
								</aui:select>
	                         </div>
	                    </div>            
					<div class="row m0">
						<aui:button type="button" value="Add User"  cssClass="createUserBtn"/>
					</div>	
				</aui:form>
            </div>
        </div>
   </div>
</div>


-----------------------------------------------------------------------------------------------------------

<h3>Data table</h3>

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
            <tr>
                <td>Sandip</td>
                <td>Patel</td>
                <td>sandip.patel@azilne.com</td>
            </tr>
            <tr>
                <td>Ankit</td>
                <td>kothari</td>
                <td>ankit.kothari@azilne.com</td>
            </tr>
	</table>
</div>

<script type="text/javascript">
        jQuery.noConflict();
        (function($) {
            $(function() {  
            	 $('#users').DataTable();
            	//console.log("example->" + $("#example").val());
            });
        })(jQuery);
    </script>