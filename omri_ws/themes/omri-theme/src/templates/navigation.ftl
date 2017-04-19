<nav class="navbar navbar-default navbar-static-top navbar2" id="navigation" role="navigation">
	<div class="container">
		 <div class="navbar-header clearfix">
            <a class="navbar-brand" href="${site_default_url}">
              <img src="${images_folder}/logo/3.png" alt="" class="" style="background:#fff">
            </a>
        </div>
		 <div class="collapse navbar-collapse" id="main_nav">
			<ul aria-label="<@liferay.language key="site-pages"/>" class="nav navbar-nav navbar-right" role="menubar">
				<#list nav_items as nav_item>
		                <li class="dropdown">
		                    <a class="link-3" aria-labelledby="layout_${nav_item.getLayoutId()}" href="${nav_item.getURL()}" role="menuitem">
		                    	<span><@liferay_theme["layout-icon"] layout=nav_item_layout /> ${nav_item.getName()}</span>
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