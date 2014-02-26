package com.savoirfairelinux.auf.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class CollaborationSitePortlet extends MVCPortlet {
	
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		List<AufSiteHelper> userSites = new ArrayList<AufSiteHelper>();
		List<AufSiteHelper> availableSites = new ArrayList<AufSiteHelper>();
		
		User liferayUser;
		try {
			liferayUser = UserLocalServiceUtil.getUser(PortalUtil.getUserId(renderRequest));
			for (Group site : liferayUser.getGroups()) {
			  if (site.isRegularSite()) {
				  userSites.add(new AufSiteHelper(site));
			  }
			}
			
			for (Group site : GroupLocalServiceUtil.getGroups(QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {
			  if (site.isRegularSite() 
					  && (!GroupLocalServiceUtil.hasUserGroup(liferayUser.getUserId(), site.getGroupId()))
					  && (!site.isControlPanel())
					  && (!site.isGuest())) {
				availableSites.add(new AufSiteHelper(site));
			  }
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		renderRequest.setAttribute("userSites", userSites);
		renderRequest.setAttribute("availableSites", availableSites);
		super.doView(renderRequest, renderResponse);  
	}
}
