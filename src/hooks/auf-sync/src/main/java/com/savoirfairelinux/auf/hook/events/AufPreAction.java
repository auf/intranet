package com.savoirfairelinux.auf.hook.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PortalClassInvoker;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.savoirfairelinux.auf.theme.util.AufSyncVelocityTool;

public class AufPreAction extends com.liferay.portal.kernel.events.Action
{
	private static final Log log = LogFactoryUtil.getLog(AufPreAction.class);
	
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException
	{	
		// theme
		@SuppressWarnings("unchecked")
		Map<String, Object> vmVariables = (Map<String, Object>) request.getAttribute(WebKeys.VM_VARIABLES);
		if (vmVariables == null) vmVariables = new HashMap<String, Object>();
		vmVariables.put("aufSyncTool", AufSyncVelocityTool.getInstance());
		request.setAttribute(WebKeys.VM_VARIABLES, vmVariables);
		
//		System.out.println("START GROUPS");
//		List<Group> groups = new ArrayList<Group>();
//		try {
//			groups = GroupLocalServiceUtil.getGroups(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
//		} catch (SystemException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		for (Group group : groups) {
//			System.out.println("GROUP : " + group.getName() + " - " + group.getGroupId());
//			try {
//				if (group.hasPublicLayouts()) {
//					if (group.getPublicLayoutSet().getLayoutSetPrototypeId() == 39917) {
//						//community 10338
//						//region 39917
//						//region nouvelle 45471
//						try {
//							log.warn("Setting site template");
//							setSiteTemplate(group.getGroupId(), 45471, false);
//							log.warn("Site template set");
//						} catch (PortalException e) {
//							 //TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (SystemException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			} catch (PortalException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SystemException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("GROUPS DONE");
		
	}
	
	public static void setSiteTemplate(long groupId, long siteTemplateId, boolean isPrivateLayout)
	        throws PortalException, SystemException {
		Group group = GroupLocalServiceUtil.getGroup(groupId);
	    LayoutSetPrototype prototype = LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(siteTemplateId);
	    boolean layoutSetPrototypeLinkEnabled = true;
	    LayoutSetLocalServiceUtil.updateLayoutSetPrototypeLinkEnabled(groupId, isPrivateLayout,
	            layoutSetPrototypeLinkEnabled, prototype.getUuid());
	    try {
	        LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(groupId, isPrivateLayout);
	        mergeLayoutSetProtypeLayouts(group, layoutSet);
	    } catch (Exception e) {
	        //if (log.isWarnEnabled()) {
	            String privatePublic = isPrivateLayout ? "private" : "public";
	            log.warn(String.format("Could not merge %s layouts for group[%d] from template[%d]", privatePublic,
	                    groupId, siteTemplateId));
	            e.printStackTrace();
	        //}
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
