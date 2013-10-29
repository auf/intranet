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

<%@ include file="/init.jsp" %>

<form name="<portlet:namespace />fm" onSubmit="submitForm(document.<portlet:namespace />fm, 'http://openweathermap.org/find?q=', false); return false;" target="_blank">
<%
for (String zip : zips) {
	Weather weather = WeatherUtil.getWeather(zip);

	if (weather != null) {
%>
	<div class="rect">
		<div class="photo2">
			<img alt="" src="<%= weather.getIconURL() %>" />
		</div>
		<div class="desc">
			<h4><%= HtmlUtil.escape(weather.getZip()) %></h4>
			<p class="poste">
				<c:if test="<%= fahrenheit %>">
					<%= weather.getCurrentTemp() %> &deg;F
				</c:if>

				<c:if test="<%= !fahrenheit %>">
					<%= Math.round((.5555555555 * (weather.getCurrentTemp() + 459.67)) - 273.15) %> &deg;C
				</c:if>
			</p>
		</div>
	</div>
<%
	}
}
%>
</form>

<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
	<aui:script>
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.q);
	</aui:script>
</c:if>