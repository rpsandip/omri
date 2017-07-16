<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} - ${company_name}</title>

	<meta content="initial-scale=1.0, width=device-width" name="viewport" />

	<@liferay_util["include"] page=top_head_include />
	
	<meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="favicon/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
   	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
   	
   	<!-- Bootstrap 3.3.6 -->
  	<link rel="stylesheet" href="${css_folder}/bootstrap.min.css">
  	<!-- Font Awesome -->
  	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  	<!-- Ionicons -->
  	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  	<!-- Theme style -->
  	<link rel="stylesheet" href="${css_folder}/custom-theme.css">
  	<link rel="stylesheet" href="${css_folder}/custom-skins.css">
  	<!-- DataTables -->
  	
  	<link rel="stylesheet" href="${css_folder}/bootstrap-timepicker.min.css">
    <link rel="stylesheet" href="${css_folder}/dataTables.bootstrap.css">
    <link rel="stylesheet" href="${css_folder}/blue.css">
    <link rel="stylesheet" href="${css_folder}/datepicker3.css">
    <link rel="stylesheet" href="${css_folder}/fullcalendar.min.css">
    <link rel="stylesheet" href="${css_folder}/fullcalendar.print.css" media="print">
   	<style>
   	.portlet-title-default, .portlet-title-text {
		display: none;
	}
	</style>
	
	<script>
	    define._amd = define.amd;
	    define.amd = false;
	</script>

	<!-- jQuery 2.2.3 -->
	<script src="${javascript_folder}/jquery-2.2.3.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${javascript_folder}/bootstrap.min.js"></script>
	 <script src="${javascript_folder}/moment.min.js"></script>
	<!-- DataTables -->
	<script src="${javascript_folder}/jquery.dataTables.min.js"></script>
	<script src="${javascript_folder}/dataTables.bootstrap.min.js"></script>
    <script src="${javascript_folder}/fullcalendar.min.js"></script>
    <script src="${javascript_folder}/bootstrap-datepicker.js"></script>
    <script src="${javascript_folder}/bootstrap-timepicker.min.js"></script>
	<script>
		define.amd = define._amd;
	`</script>
	
</head>

<body class="${css_class} default about_page hold-transition skin-blue layout-top-nav">
<div class="wrapper">
<@liferay_ui["quick-access"] contentId="#main-content" />
<@liferay_util["include"] page=body_top_include />
	<#if is_omni_admin>
		<@liferay.control_menu />
		<br/>
		<br/>
		<br/>
	</#if>
		
	<#if has_navigation && is_setup_complete>
		<#include "${full_templates_path}/navigation.ftl" />
	</#if>
	<div class="content-wrapper">
	    <div class="container">
			<section id="content" class="row">
				<#if selectable>
					<@liferay_util["include"] page=content_include />
				<#else>
				${portletDisplay.recycle()}
		
				${portletDisplay.setTitle(the_title)}
		
				<@liferay_theme["wrap-portlet"] page="portlet.ftl">
					<@liferay_util["include"] page=content_include />
				</@>
				</#if>
			</section>	
		</div>
   </div>		
</div>
<@liferay_util["include"] page=body_bottom_include />
<@liferay_util["include"] page=bottom_include />

<footer class="main-footer">
    <div class="container">
      <strong>Copyright &copy; 2017 <a href="index.html">OMRI</a>.</strong> All rights
      reserved.
    </div>
</footer>
<script>
	    define._amd = define.amd;
	    define.amd = false;
</script>
<script src="${javascript_folder}/custom.js"></script>
<script>
	define.amd = define._amd;
</script>
</div>

</body>
</html>