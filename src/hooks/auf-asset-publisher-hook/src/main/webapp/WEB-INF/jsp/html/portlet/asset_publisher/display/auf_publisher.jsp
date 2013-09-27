<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<% 
preferences.setValue("showAssetTitle", "false");
%>
<liferay-util:include page="/html/portlet/asset_publisher/display/full_content.jsp"/>
