package com.savoirfairelinux.auf.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class PolesPortlet extends MVCPortlet {
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long organizationId = themeDisplay.getParentGroup().getOrganizationId();
		Organization currentOrganization = null;
		List<Group> availablePoles = new ArrayList<Group>();
		
		try {
			currentOrganization = OrganizationLocalServiceUtil.getOrganization(organizationId);
			for (Organization subOrg : currentOrganization.getSuborganizations()) {
				availablePoles.add(subOrg.getGroup());
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		
		renderRequest.setAttribute("availablePoles", availablePoles);
		super.doView(renderRequest, renderResponse);
	}

}
