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
    
    <!--Bootstrap and Other Vendors-->
    <link rel="stylesheet" href="${css_folder}/bootstrap.min.css">
    <link rel="stylesheet" href="${css_folder}/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${css_folder}/font-awesome.min.css">
    <link rel="stylesheet" href="${css_folder}/vendors/owl.carousel/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${css_folder}/vendors/owl.carousel/css/owl.theme.default.min.css">
    <link rel="stylesheet" type="text/css" href="${css_folder}/vendors/flexslider/flexslider.css" media="screen">
    <link rel="stylesheet" type="text/css" href="${css_folder}/vendors/bootstrap-datepicker/css/datepicker3.css" media="screen">

    <!--Fonts-->
    <link href='http://fonts.googleapis.com/css?family=Karla:400,400italic,700,700italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700,900,100italic,300italic,400italic,700italic,900italic' rel='stylesheet' type='text/css'>

    <!--Mechanic Styles-->
    <link rel="stylesheet" href="${css_folder}/default/style.css">
    <link rel="stylesheet" href="${css_folder}/responsive/responsive.css">
    <link rel="stylesheet" href="${css_folder}/checkbox.css">
    <link rel="stylesheet" href="${css_folder}/Multiselect/bootstrap-multiselect.css">
    <link rel="stylesheet" href="${css_folder}/custom.css">
    <!--[if lt IE 9]>
    <script>
	    define._amd = define.amd;
	    define.amd = false;
	</script>
	
    <script src="${javascript_folder}/html5shiv.min.js"></script>
    <script src="${javascript_folder}/respond.min.js"></script>
    <![endif]-->
    
    <script>
	    define.amd = define._amd;
	</script>
    
</head>

<body class="${css_class} default about_page">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />
	<#if is_omni_admin>
		<@liferay.control_menu />
		<br/>
		<br/>
		<br/>
		<br/>
	</#if>
		
		<#if has_navigation && is_setup_complete>
			<#include "${full_templates_path}/navigation.ftl" />
		</#if>
		<section id="content" class="row contact_form_row">
			<#if selectable>
				<@liferay_util["include"] page=content_include />
			<#else>
			${portletDisplay.recycle()}

			${portletDisplay.setTitle(the_title)}

			<@liferay_theme["wrap-portlet"] page="portlet.ftl">
				<@liferay_util["include"] page=content_include />
			</@>
			</#if>
</div>
<@liferay_util["include"] page=body_bottom_include />
<@liferay_util["include"] page=bottom_include />
</body>
</html>