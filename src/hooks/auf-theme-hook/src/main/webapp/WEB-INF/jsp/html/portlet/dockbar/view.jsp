<%@ include file="/html/portlet/dockbar/init.jsp" %>
<%
	themeDisplay.setURLSignOut("https://id.auf.org/logout");
%>   

<liferay-util:buffer var="html">
	<liferay-util:include page="/html/portlet/dockbar/view.portal.jsp" />
</liferay-util:buffer>  

<%=html%>