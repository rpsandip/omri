package com.omri.portlet.resourcecommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.mail.internet.InternetAddress;
import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.mail.MailEngine;
@Component(
	    property = {
	    		"javax.portlet.name=com_omri_portlet_OmriBillingModulemvcportletPortlet",
	        "mvc.command.name=/send-mail"
	    },
	    service = MVCResourceCommand.class
)
public class SendMailResourceCommand  implements MVCResourceCommand{

	private static String FROM_EMAIL = "no-reply@omri.com";
	private static Log LOG = LogFactoryUtil.getLog(SendMailResourceCommand.class.getName());
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		return true;
	}
}
