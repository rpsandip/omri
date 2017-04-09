<#--
This file allows you to override and define new FreeMarker variables.
-->
<#assign is_omni_admin = themeDisplay.getPermissionChecker().isOmniadmin() />
<#assign page_friendly_url = themeDisplay.getLayout().getFriendlyURL() />