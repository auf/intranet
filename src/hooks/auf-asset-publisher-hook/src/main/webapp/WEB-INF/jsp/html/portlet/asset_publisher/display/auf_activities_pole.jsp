<%@ include file="/html/common/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.DateUtil" %>
<%@ page import="java.util.Date" %>
<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
List results = (List)request.getAttribute("view.jsp-results");

int assetEntryIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRendererFactory assetRendererFactory = (AssetRendererFactory)request.getAttribute("view.jsp-assetRendererFactory");
AssetRenderer assetRenderer = (AssetRenderer)request.getAttribute("view.jsp-assetRenderer");

boolean show = ((Boolean)request.getAttribute("view.jsp-show")).booleanValue();

request.setAttribute("view.jsp-showIconLabel", true);

String title = (String)request.getAttribute("view.jsp-title");

if (Validator.isNull(title)) {
	title = assetRenderer.getTitle(locale);
}

/* **** Added to overwrite the link target ****** */
long newPlid = plid;
String portletDisplayId = portletDisplay.getId();
final long groupId = 10180;

final String AUF_DISPLAY_PAGE = "/auf-display-page"; 
Layout l = null;
try {
	l = LayoutLocalServiceUtil.getFriendlyURLLayout(assetEntry.getGroupId(), false, AUF_DISPLAY_PAGE);
} catch (com.liferay.portal.NoSuchLayoutException e) {
	//the group does not have a proper display page
	_log.error("No local layout found for group " + assetEntry.getGroupId());
	try {
		l = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, false, AUF_DISPLAY_PAGE);
	} catch (com.liferay.portal.NoSuchLayoutException se) {
		_log.error(AUF_DISPLAY_PAGE + " not found! Was looking in group " + groupId);
		se.printStackTrace();
	} catch (Exception se) {
		_log.error(AUF_DISPLAY_PAGE + " not found! Was looking in group " + groupId);
		se.printStackTrace();
	}
} catch (Exception e) {
	_log.error(AUF_DISPLAY_PAGE + " not found! Was looking in group " + assetEntry.getGroupId());
	e.printStackTrace();
}

if (l != null) {

	try {
		plid = l.getPlid();
		newPlid = plid;
		List<com.liferay.portal.model.PortletPreferences> portlets = PortletPreferencesLocalServiceUtil.getPortletPreferences(0, 3, plid);
		for (com.liferay.portal.model.PortletPreferences p : portlets) {
			if (p.getPortletId().startsWith("101") 
					&& (!p.getPortletId().equals("101_INSTANCE_VORUZ3haQRVY")) 
					&& (!p.getPortletId().equals("101_INSTANCE_eFzcOQLPj6zh"))) {
				portletDisplayId = p.getPortletId();
				break;
			}
		}
	} catch (SystemException e) {
		//log.error("Preferences for the events could not be retrieved!");
		//e.printStackTrace();
	}
}

PortletURL viewFullContentURL = liferayPortletResponse.createLiferayPortletURL(newPlid, portletDisplayId, PortletRequest.RENDER_PHASE, false);
/* ****************** END ********************** */
//PortletURL viewFullContentURL = liferayPortletResponse.createLiferayPortletURL(plid, portletDisplay.getId(), PortletRequest.RENDER_PHASE, false);

viewFullContentURL.setParameter("struts_action", "/asset_publisher/view_content");
viewFullContentURL.setParameter("assetEntryId", String.valueOf(assetEntry.getEntryId()));
viewFullContentURL.setParameter("type", assetRendererFactory.getType());

if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
	if (assetRenderer.getGroupId() != scopeGroupId) {
		viewFullContentURL.setParameter("groupId", String.valueOf(assetRenderer.getGroupId()));
	}

	viewFullContentURL.setParameter("urlTitle", assetRenderer.getUrlTitle());
}

String summary = StringUtil.shorten(assetRenderer.getSummary(locale), abstractLength);

//new values
String userName = assetRenderer.getUserName();
boolean isModified = (DateUtil.equals(assetEntry.getModifiedDate(), assetEntry.getCreateDate())) ? false : true;


String viewURL = null;

if (viewInContext) {
	String viewFullContentURLString = viewFullContentURL.toString();

	viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect", currentURL);

	viewURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse, viewFullContentURLString);
}
else {
	viewURL = viewFullContentURL.toString();
}

if (Validator.isNull(viewURL)) {
	viewURL = viewFullContentURL.toString();
}

String viewURLMessage = viewInContext ? assetRenderer.getViewInContextMessage() : "read-more-x-about-x";

viewURL = _checkViewURL(assetEntry, viewInContext, viewURL, currentURL, themeDisplay);
%>

<c:if test="<%= show %>">
	<div class="news2 asset-activities">
		<h4 class="asset-title">
			<c:choose>
				<c:when test="<%= Validator.isNotNull(viewURL) %>">
					<a href="<%= viewURL %>"><img alt="" src="<%= assetRenderer.getIconPath(renderRequest) %>" />
						<%= HtmlUtil.escape(title) %>
					</a>
				</c:when>
				<c:otherwise>
					<img alt="" src="<%= assetRenderer.getIconPath(renderRequest) %>" /> <%= HtmlUtil.escape(title) %>
				</c:otherwise>
			</c:choose>
		</h4>
		<p class="date2">
			<c:choose>
				<c:when test="<%= isModified %>">
					<liferay-ui:message key="auf-activties-modified" />
				</c:when>
				<c:otherwise>
					<liferay-ui:message key="auf-activties-created" />
				</c:otherwise>
			</c:choose>
			<liferay-ui:message
				arguments="<%= new String[] {LanguageUtil.getTimeDescription(pageContext, (System.currentTimeMillis() - assetEntry.getModifiedDate().getTime()), true), userName} %>" key="x-ago-by-x"
			/>
		</p>
	</div>
</c:if>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.asset_publisher.auf_activities_pole_jsp");
%>