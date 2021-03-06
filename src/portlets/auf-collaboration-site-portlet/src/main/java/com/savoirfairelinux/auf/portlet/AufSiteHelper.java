package com.savoirfairelinux.auf.portlet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class AufSiteHelper {
	
	private Group site;
	
	private final long SITE_ADMIN_ROLE_ID = 10169;
	private final long SITE_OWNER_ROLE_ID = 10171;

	public AufSiteHelper(Group site) {
		this.site = site;
	}
	
	public Group getSite() {
		return site;
	}
	
	public int getMembersCount() {
		try {
			return UserLocalServiceUtil.getGroupUsersCount(site.getGroupId());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String getEmailAddresses() {
		List<UserGroupRole> siteAdmins = null;
		List<UserGroupRole> siteOwners = null;
		
		Set<String> emailAddresses = new HashSet<String>();
		StringBuilder emailAddressesString = new StringBuilder("mailto:");
		try {
			siteAdmins = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(site.getGroupId(), SITE_ADMIN_ROLE_ID);
		} catch (SystemException e) {
		}
		try {
			siteOwners = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(site.getGroupId(), SITE_OWNER_ROLE_ID);
		} catch (SystemException e) {
		}
		
		if (siteAdmins == null && siteOwners==null) {
			return "#";
		}
		
		if (siteAdmins != null) {
			for(UserGroupRole ugr : siteAdmins) {
				try {
					User u = UserLocalServiceUtil.getUser(ugr.getUserId());
					emailAddresses.add(u.getEmailAddress());
				} catch (PortalException e) {
				} catch (SystemException e) {
				}
				
			}
		}
		if (siteAdmins != null) {
			for(UserGroupRole ugr : siteOwners) {
				try {
					User u = UserLocalServiceUtil.getUser(ugr.getUserId());
					emailAddresses.add(u.getEmailAddress());
				} catch (PortalException e) {
				} catch (SystemException e) {
				}
				
			}
		}
		
		for(String ea : emailAddresses) {
			emailAddressesString.append(ea);
			emailAddressesString.append(";");
		}
		
		try {
			emailAddressesString.append("?subject=Je souhaiterais rejoindre le espace collaboratif&body=Bonjour, je souhaiterais rejoindre le espace collaboratif suivant: " + site.getDescriptiveName() + ". Merci.");
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return emailAddressesString.toString();
	}

}
