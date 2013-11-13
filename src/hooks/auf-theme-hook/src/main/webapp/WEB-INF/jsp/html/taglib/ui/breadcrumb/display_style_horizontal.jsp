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

<%@ include file="/html/taglib/ui/breadcrumb/init.jsp" %>

<%
StringBundler sb = new StringBundler();

if (showGuestGroup) {
	_buildGuestGroupBreadcrumb(themeDisplay, sb);
}

if (showParentGroups) {
	_buildParentGroupsBreadcrumb(selLayout.getLayoutSet(), portletURL, themeDisplay, sb);
}

if (showLayout) {
	_buildLayoutBreadcrumb(selLayout, selLayoutParam, true, portletURL, themeDisplay, sb);
}

if (showPortletBreadcrumb) {
	_buildPortletBreadcrumb(request, showCurrentGroup, showCurrentPortlet, themeDisplay, sb);
}

String breadcrumbString = sb.toString();

if (Validator.isNotNull(breadcrumbString)) {
	int x = breadcrumbString.indexOf("<li");
	int y = breadcrumbString.lastIndexOf("<li");

	if (x == y) {
		breadcrumbString = StringUtil.insert(breadcrumbString, " class=\"only\"", x + 3);
	}
	else {
		breadcrumbString = StringUtil.insert(breadcrumbString, " class=\"last\"", y + 3);
		breadcrumbString = StringUtil.insert(breadcrumbString, " class=\"first\"", x + 3);
	}
	
	if (breadcrumbString.contains("/auf-display-page\"")) {
		int adpPos = breadcrumbString.indexOf("/auf-display-page\"");
		int liStart = breadcrumbString.lastIndexOf("<li", adpPos);
		int liEnd = breadcrumbString.indexOf("</li>", adpPos);
		breadcrumbString = breadcrumbString.substring(0, liStart) + breadcrumbString.substring(liEnd);
	}
	
}
%>

<ul class="breadcrumbs breadcrumbs-horizontal lfr-component">
	<%= breadcrumbString %>
</ul>