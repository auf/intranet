package com.savoirfairelinux.auf.theme.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;

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

}
