<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<% request.setAttribute(WebKeys.JOURNAL_TEMPLATE_ID, "AUF_PUBLISHER"); %>
<% 
preferences.setValue("showAssetTitle", "false");
%>
<liferay-util:include page="/html/portlet/asset_publisher/display/full_content.jsp"/>
