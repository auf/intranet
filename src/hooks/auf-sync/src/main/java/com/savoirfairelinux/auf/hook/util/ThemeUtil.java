package com.savoirfairelinux.auf.hook.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PortalClassInvoker;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;

public class ThemeUtil {
	private static final Log _log = LogFactoryUtil.getLog(ThemeUtil.class);

	
	public static void setupSitesFromSiteTemplate(long groupId, long publicSiteTemplateId,
            long privateSiteTemplateId) throws PortalException, SystemException {
		Group group = GroupLocalServiceUtil.getGroup(groupId);
		if (publicSiteTemplateId != 0) setSiteTemplate(group, publicSiteTemplateId, false);
		if (privateSiteTemplateId != 0) setSiteTemplate(group, privateSiteTemplateId, true);
	}

	public static void setSiteTemplate(Group group, long siteTemplateId, boolean isPrivateLayout)
		throws PortalException, SystemException {
		
		long groupId = group.getGroupId();
		LayoutSetPrototype prototype = LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(siteTemplateId);
		boolean layoutSetPrototypeLinkEnabled = true;
		LayoutSetLocalServiceUtil.updateLayoutSetPrototypeLinkEnabled(groupId, isPrivateLayout,
		layoutSetPrototypeLinkEnabled, prototype.getUuid());
		try {
			LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(groupId, isPrivateLayout);
			mergeLayoutSetProtypeLayouts(group, layoutSet);
		} catch (Exception e) {
			if (_log.isWarnEnabled()) {
				String privatePublic = isPrivateLayout ? "private" : "public";
				_log.warn(String.format("Could not merge %s layouts for group[%d] from template[%d]", privatePublic,
				groupId, siteTemplateId));
				e.printStackTrace();
			}
		}
	}

	public static void mergeLayoutSetProtypeLayouts(Group group, LayoutSet layoutSet) throws Exception {
	
		MethodKey key = SitesUtilMethodKey("mergeLayoutSetProtypeLayouts", Group.class, LayoutSet.class);
		invokePortalClassMethod(key, group, layoutSet);
	}
	/*
	* copied from
	* http://www.liferay.com/community/forums/-/message_boards/view_message /10488983#_19_message_10488983 
	* post by Jelmer Kuperus
	*
	* key: key of method to be called, e.g. com.liferay.portlet.sites.util.SitesUtil
	* arguments:  arguments to be passed to the invoked method
	* returns: result of the invoked method
	*/
	private static Object invokePortalClassMethod(MethodKey key, Object... arguments) throws PortalException {
		try {
			// noinspection unchecked
			return PortalClassInvoker.invoke(false, key, arguments);
		} catch (PortalException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static final String SITES_UTIL_CLASS_NAME = "com.liferay.portlet.sites.util.SitesUtil";
	private static MethodKey SitesUtilMethodKey(String methodName, Class<?>... parameterTypes) {
		return new MethodKey(SITES_UTIL_CLASS_NAME, methodName, parameterTypes);
	}
}
