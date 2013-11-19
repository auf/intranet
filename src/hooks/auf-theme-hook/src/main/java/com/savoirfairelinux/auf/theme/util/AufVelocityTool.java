package com.savoirfairelinux.auf.theme.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class AufVelocityTool
{
	private static Log log = LogFactoryUtil.getLog(AufVelocityTool.class);
	private static AufVelocityTool instance;

	private AufVelocityTool() {
		String breadcrumbString = "<li></li><li><span>auf-display-page</li>";
		
		if (breadcrumbString.contains("/auf-display-page\"")) {
			int adpPos = breadcrumbString.indexOf("/auf-display-page\"");
			int liStart = breadcrumbString.lastIndexOf("<li", adpPos);
			int liEnd = breadcrumbString.indexOf("</li>", adpPos);
			breadcrumbString = breadcrumbString.substring(0, liStart) + breadcrumbString.substring(liEnd);
		}
	}

	public static AufVelocityTool getInstance()
	{
		if (instance == null)
			instance = new AufVelocityTool();
		return instance;
	}
	

	public List<Organization> getSubOrganizations(long organizationId)
	{
		try
		{
			Organization organization = OrganizationLocalServiceUtil.getOrganization(organizationId);
			return organization.getSuborganizations();
		}
		catch (Exception e)
		{
			log.error("Cannot get regions for organization " + organizationId);
			return new ArrayList<Organization>();
		}
	}
	
	public List<Group> getCollaborationSites(long userId)
	{
		try
		{
			User liferayUser = UserLocalServiceUtil.getUser(userId);
			
			List<Group> userSites = new ArrayList<Group>();
			for (Group site : liferayUser.getGroups()) {
			  if (site.isRegularSite()) {
				  userSites.add(site);
			  }
			}
			return userSites;
		} catch (PortalException e) {
			log.error("Cannot get sites for user " + userId);
			e.printStackTrace();
			return new ArrayList<Group>();
		} catch (SystemException e) {
			log.error("Cannot get sites for user " + userId);
			e.printStackTrace();
			return new ArrayList<Group>();
		}
	}
	
	public String respondTest(long randomId) {
		return "Test";
	}
	
	public boolean isNotUnder(long organisationId, long plid)
	{
		try {
			Layout l = LayoutLocalServiceUtil.getLayout(plid);
			if (l.getGroup().isGuest()) {
				return false;
			}
			if (l.getGroup().getOrganizationId() == 0) {
				return true;
			}
			Organization currentOrg = OrganizationLocalServiceUtil.getOrganization(l.getGroup().getOrganizationId());
			
			Organization possibleParentOrg = OrganizationLocalServiceUtil.getOrganization(organisationId);
			for (Organization ancestor : currentOrg.getAncestors()) {
				if (ancestor.equals(possibleParentOrg)) {
					return false;
				}
			}
		} catch (PortalException e) {
			log.error("Organisation or page not found : " + organisationId + " / " + plid);
			return false;
		} catch (SystemException e) {
			log.error("Organisation or page not found : " + organisationId + " / " + plid);
			return false;
		}
		return true;
	}
	
	public boolean isNotUnderFrontpage(long plid)
	{
		try {
			Layout l = LayoutLocalServiceUtil.getLayout(plid);
			if ((l.getGroup().isGuest()) && (l.getGroup().getDefaultPublicPlid() == plid)) {
				return false;
			}
		} catch (PortalException e) {
			log.error("page not found : " + plid);
			return false;
		} catch (SystemException e) {
			log.error("page not found : " + plid);
			return false;
		}
		return true;
	}
	
	public String getUserWeatherLocation(long userId) {
		User u = null;
		try {
			u = UserLocalServiceUtil.getUser(userId);
		} catch (PortalException e1) {
			//no user logged in or found
		} catch (SystemException e1) {
			//no user logged in or found
		}
		
		if (u==null) {
			return "Paris, France";
		}
		
		try {
			List<Address> addresses = u.getAddresses();
			if (addresses.size() > 0) {
				String city = addresses.get(0).getCity();
				String country = addresses.get(0).getCountry().getName();
				if (city!=null && city.length() > 0) {
					if (country!=null && country.length() > 0) {
						return city + ", " + country;
					}
					return city;
				}
			}
		} catch (SystemException e) {
			//no addresses were found
		}
		return "Paris, France";
	}
	
	public String getEventSetting(long groupId) {
		// template asset publisher configuration
		final String AUF_EVENT_TEMPLATE_PAGE = "/auf-event-template-page"; 
		Layout l = null;
		try {
			l = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, false, AUF_EVENT_TEMPLATE_PAGE);
		} catch (NoSuchLayoutException e) {
			log.error(AUF_EVENT_TEMPLATE_PAGE + " not found! Was looking in group " + groupId);
		} catch (Exception e) {
			log.error(AUF_EVENT_TEMPLATE_PAGE + " not found! Was looking in group " + groupId);
			e.printStackTrace();
		}
		
		if (l == null) return "";
		
		try {
			long plid = l.getPlid();
			List<PortletPreferences> portlets = PortletPreferencesLocalServiceUtil.getPortletPreferences(0, 3, plid);
			for (PortletPreferences p : portlets) {
				if (p.getPortletId().startsWith("101") 
						&& (!p.getPortletId().equals("101_INSTANCE_VORUZ3haQRVY")) 
						&& (!p.getPortletId().equals("101_INSTANCE_eFzcOQLPj6zh"))) {
					return p.getPreferences();
				}
			}
		} catch (SystemException e) {
			log.error("Preferences for the events could not be retrieved!");
			e.printStackTrace();
		}
		return "";
	}
	
	public String getActualitySetting(long groupId) {
		// template asset publisher configuration
		final String AUF_ACTUALITY_TEMPLATE_PAGE = "/auf-actuality-template-page"; 
		Layout l = null;
		try {
			l = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, false, AUF_ACTUALITY_TEMPLATE_PAGE);
		} catch (NoSuchLayoutException e) {
			log.error(AUF_ACTUALITY_TEMPLATE_PAGE + " not found! Was looking in group " + groupId);
		} catch (Exception e) {
			log.error(AUF_ACTUALITY_TEMPLATE_PAGE + " not found! Was looking in group " + groupId);
			e.printStackTrace();
		}
		
		if (l == null) return "";
		
		try {
			long plid = l.getPlid();
			List<PortletPreferences> portlets = PortletPreferencesLocalServiceUtil.getPortletPreferences(0, 3, plid);
			for (PortletPreferences p : portlets) {
				if (p.getPortletId().startsWith("101") 
						&& (!p.getPortletId().equals("101_INSTANCE_VORUZ3haQRVY")) 
						&& (!p.getPortletId().equals("101_INSTANCE_eFzcOQLPj6zh"))) {
					return p.getPreferences();
				}
			}
		} catch (SystemException e) {
			log.error("Preferences for the events could not be retrieved!");
			e.printStackTrace();
		}
		return "";
	}

}
