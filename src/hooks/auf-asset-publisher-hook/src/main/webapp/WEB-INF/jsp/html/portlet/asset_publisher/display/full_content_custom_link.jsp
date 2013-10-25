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
String redirect = ParamUtil.getString(request, "redirect");

List results = (List)request.getAttribute("view.jsp-results");


if (Validator.isNull(redirect) && results.isEmpty()) {
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/asset_publisher/view");

	redirect = portletURL.toString();
}

int assetEntryIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRendererFactory assetRendererFactory = (AssetRendererFactory)request.getAttribute("view.jsp-assetRendererFactory");
AssetRenderer assetRenderer = (AssetRenderer)request.getAttribute("view.jsp-assetRenderer");

String title = (String)request.getAttribute("view.jsp-title");

boolean show = ((Boolean)request.getAttribute("view.jsp-show")).booleanValue();
boolean print = ((Boolean)request.getAttribute("view.jsp-print")).booleanValue();

request.setAttribute(WebKeys.LAYOUT_ASSET_ENTRY, assetEntry);

request.setAttribute("view.jsp-fullContentRedirect", currentURL);
request.setAttribute("view.jsp-showIconLabel", true);
%>

<c:if test="<%= showAssetTitle %>">
	<liferay-ui:header
		backURL="<%= print ? null : redirect %>"
		localizeTitle="<%= false %>"
		title="<%= title %>"
	/>
</c:if>

<div class="asset-full-content <%= defaultAssetPublisher ? "default-asset-publisher" : StringPool.BLANK %> <%= showAssetTitle ? "show-asset-title" : "no-title" %>">
	<c:if test="<%= !print %>">
		<liferay-util:include page="/html/portlet/asset_publisher/asset_actions.jsp" />
	</c:if>

	<c:if test="<%= (enableConversions && assetRenderer.isConvertible()) || (enablePrint && assetRenderer.isPrintable()) || (showAvailableLocales && assetRenderer.isLocalizable()) %>">
		<div class="asset-user-actions">
			<c:if test="<%= enablePrint %>">
				<div class="print-action">
					<%@ include file="/html/portlet/asset_publisher/asset_print.jspf" %>
				</div>
			</c:if>

			<c:if test="<%= (enableConversions && assetRenderer.isConvertible()) && !print %>">

				<%
				PortletURL exportAssetURL = assetRenderer.getURLExport(liferayPortletRequest, liferayPortletResponse);
				%>

				<div class="export-actions">
					<%@ include file="/html/portlet/asset_publisher/asset_export.jspf" %>
				</div>
			</c:if>
			<c:if test="<%= (showAvailableLocales && assetRenderer.isLocalizable()) && !print %>">

				<%
				String[] availableLocales = assetRenderer.getAvailableLocales();
				%>

				<c:if test="<%= availableLocales.length > 1 %>">
					<c:if test="<%= enableConversions || enablePrint %>">
						<div class="locale-separator"> </div>
					</c:if>

					<div class="locale-actions">
						<liferay-ui:language displayStyle="<%= 0 %>" languageIds="<%= availableLocales %>" />
					</div>
				</c:if>
			</c:if>
		</div>
	</c:if>

	<%

	// Dynamically created asset entries are never persisted so incrementing the view counter breaks

	if (!assetEntry.isNew() && assetEntry.isVisible()) {
		AssetEntry incrementAssetEntry = null;

		if (assetEntryQuery.isEnablePermissions()) {
			incrementAssetEntry = AssetEntryServiceUtil.incrementViewCounter(assetEntry.getClassName(), assetEntry.getClassPK());
		}
		else {
			incrementAssetEntry = AssetEntryLocalServiceUtil.incrementViewCounter(user.getUserId(), assetEntry.getClassName(), assetEntry.getClassPK());
		}

		if (incrementAssetEntry != null) {
			assetEntry = incrementAssetEntry;
		}
	}

	if (showContextLink) {
		if (PortalUtil.getPlidFromPortletId(assetRenderer.getGroupId(), assetRendererFactory.getPortletId()) == 0) {
			showContextLink = false;
		}
	}

	/* **** Added to overwrite the link target ****** */
	long newPlid = plid;
	String portletDisplayId = portletDisplay.getId();
	final long groupId = 10180;

	final String AUF_DISPLAY_PAGE = "/auf-display-page"; 
	Layout l = null;
	try {
		l = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, false, AUF_DISPLAY_PAGE);
	} catch (com.liferay.portal.NoSuchLayoutException e) {
		//log.error(AUF_DISPLAY_PAGE + " not found! Was looking in group " + groupId);
	} catch (Exception e) {
		//log.error(AUF_DISPLAY_PAGE + " not found! Was looking in group " + groupId);
		//e.printStackTrace();
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

	//PortletURL viewFullContentURL = renderResponse.createRenderURL();

	viewFullContentURL.setParameter("struts_action", "/asset_publisher/view_content");
	viewFullContentURL.setParameter("type", assetRendererFactory.getType());

	if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
		if (assetRenderer.getGroupId() != scopeGroupId) {
			viewFullContentURL.setParameter("groupId", String.valueOf(assetRenderer.getGroupId()));
		}

		viewFullContentURL.setParameter("urlTitle", assetRenderer.getUrlTitle());
	}

	String viewFullContentURLString = viewFullContentURL.toString();

	viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect", currentURL);
	
	/* **** Added to overwrite the link target ****** */
	renderRequest.setAttribute("viewFullContentURLString", viewFullContentURLString);
    /* ****************** END ********************** */
	%>

	<div class="asset-content">
		<c:if test='<%= enableSocialBookmarks && socialBookmarksDisplayPosition.equals("top") %>'>
			<liferay-ui:social-bookmarks
				displayStyle="<%= socialBookmarksDisplayStyle %>"
				target="_blank"
				title="<%= assetEntry.getTitle(locale) %>"
				url="<%= PortalUtil.getCanonicalURL(viewFullContentURL.toString(), themeDisplay, layout) %>"
			/>
		</c:if>

		<%
		String path = assetRenderer.render(renderRequest, renderResponse, AssetRenderer.TEMPLATE_FULL_CONTENT);

		request.setAttribute(WebKeys.ASSET_RENDERER_FACTORY, assetRendererFactory);
		request.setAttribute(WebKeys.ASSET_RENDERER, assetRenderer);
		%>

		<liferay-util:include page="<%= path %>" portletId="<%= assetRendererFactory.getPortletId() %>" />

		<c:if test="<%= enableFlags %>">
			<div class="asset-flag">
				<liferay-ui:flags
					className="<%= assetEntry.getClassName() %>"
					classPK="<%= assetEntry.getClassPK() %>"
					contentTitle="<%= assetRenderer.getTitle(locale) %>"
					reportedUserId="<%= assetRenderer.getUserId() %>"
				/>
			</div>
		</c:if>

		<c:if test='<%= enableSocialBookmarks && socialBookmarksDisplayPosition.equals("bottom") %>'>
			<liferay-ui:social-bookmarks
				displayStyle="<%= socialBookmarksDisplayStyle %>"
				target="_blank"
				title="<%= assetEntry.getTitle(locale) %>"
				url="<%= PortalUtil.getCanonicalURL(viewFullContentURL.toString(), themeDisplay, layout) %>"
			/>
		</c:if>

		<c:if test="<%= enableRatings %>">
			<div class="asset-ratings">

				<%
				String assetEntryClassName = assetEntry.getClassName();

				String ratingsType = "stars";

				if (assetEntryClassName.equals(MBDiscussion.class.getName()) || assetEntryClassName.equals(MBMessage.class.getName())) {
					ratingsType = "thumbs";
				}
				%>

				<liferay-ui:ratings
					className="<%= assetEntry.getClassName() %>"
					classPK="<%= assetEntry.getClassPK() %>"
					type="<%= ratingsType %>"
				/>
			</div>
		</c:if>

		<c:if test="<%= showContextLink && !print && assetEntry.isVisible() %>">
			<div class="asset-more">
				<a href="<%= assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse, viewFullContentURLString) %>"><liferay-ui:message key="<%= assetRenderer.getViewInContextMessage() %>" /> &raquo;</a>
			</div>
		</c:if>

		<br />

		<c:if test="<%= enableRelatedAssets %>">
			<liferay-ui:asset-links
				assetEntryId="<%= assetEntry.getEntryId() %>"
			/>
		</c:if>

		<c:if test="<%= Validator.isNotNull(assetRenderer.getDiscussionPath()) && enableComments %>">
			<br />

			<portlet:actionURL var="discussionURL">
				<portlet:param name="struts_action" value='<%= "/asset_publisher/" + assetRenderer.getDiscussionPath() %>' />
			</portlet:actionURL>

			<liferay-ui:discussion
				className="<%= assetEntry.getClassName() %>"
				classPK="<%= assetEntry.getClassPK() %>"
				formAction="<%= discussionURL %>"
				formName='<%= "fm" + assetEntry.getClassPK() %>'
				ratingsEnabled="<%= enableCommentRatings %>"
				redirect="<%= currentURL %>"
				subject="<%= assetRenderer.getTitle(locale) %>"
				userId="<%= assetRenderer.getUserId() %>"
			/>
		</c:if>
	</div>

	<c:if test="<%= show %>">
		<div class="asset-metadata">
			<%@ include file="/html/portlet/asset_publisher/asset_metadata.jspf" %>
		</div>
	</c:if>
</div>

<c:choose>
	<c:when test="<%= !showAssetTitle && ((assetEntryIndex + 1) < results.size()) %>">
		<div class="separator"><!-- --></div>
	</c:when>
	<c:when test="<%= (assetEntryIndex + 1) == results.size() %>">
		<div class="final-separator"><!-- --></div>
	</c:when>
</c:choose>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.asset_publisher.display_full_content_jsp");
%>