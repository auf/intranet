package com.savoirfairelinux.auf.theme.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class AufVelocityTool
{
	private static Log log = LogFactoryUtil.getLog(AufVelocityTool.class);
	private static AufVelocityTool instance;

	private AufVelocityTool()
	{
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

}
