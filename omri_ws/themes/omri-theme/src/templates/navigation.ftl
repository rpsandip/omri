<header class="main-header">
<nav class="navbar navbar-static-top" id="navigation" role="navigation">
	<div class="container">
		 <div class="navbar-header clearfix">
            <a class="navbar-brand" href="${site_default_url}">
              <b>OMRI</b>
            </a>
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
	            <i class="fa fa-bars"></i>
	        </button>
        </div>
		 <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
			<ul aria-label="<@liferay.language key="site-pages"/>" class="nav navbar-nav" role="menubar">
				<#list nav_items as nav_item>
		                <li>
		                    <a aria-labelledby="layout_${nav_item.getLayoutId()}" href="${nav_item.getURL()}" role="menuitem">
		                    	<#if nav_item.getLayout().getFriendlyURL()=='/dashboard'>
		                    		<i class="fa fa-fw fa-home"></i> ${nav_item.getName()}
		                    	<#elseif nav_item.getLayout().getFriendlyURL()=='/users'>
		                    		<i class="fa fa-fw fa-user"></i> ${nav_item.getName()}
		                    	<#elseif nav_item.getLayout().getFriendlyURL()=='/clinic'>
		                    		<i class="fa fa-fw fa-hospital-o"></i> ${nav_item.getName()}
		                    	<#elseif nav_item.getLayout().getFriendlyURL()=='/patient'>
		                    		<i class="fa fa-fw fa-wheelchair"></i> ${nav_item.getName()}
		                    	<#elseif nav_item.getLayout().getFriendlyURL()=='/patient-list'>
		                    		<i class="fa fa-fw fa-wheelchair"></i> ${nav_item.getName()}
			                    <#elseif nav_item.getLayout().getFriendlyURL()=='/appointment'>
		                    		<i class="fa fa-fw fa-fax"></i> ${nav_item.getName()}
		                    	<#elseif nav_item.getLayout().getFriendlyURL()=='/billing'>
		                    		<i class="fa fa-fw fa-file-text"></i> ${nav_item.getName()}			
		                    	<#else>
		                    		${nav_item.getName()}	
		                    	</#if>
		                    </a>
		                </li>
				</#list>
				<#if is_signed_in>
					<li class="dropdown">
						<a class="link-3" href="/c/portal/logout">Log out</a>
					</li>
				</#if>
			</ul>
	    </div>
	</div>
</nav>
 </header>