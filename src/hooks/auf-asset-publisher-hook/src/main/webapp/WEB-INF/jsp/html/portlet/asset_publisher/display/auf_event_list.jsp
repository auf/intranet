<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<% 
preferences.setValue("showAssetTitle", "false");
%>
<% request.setAttribute(WebKeys.JOURNAL_TEMPLATE_ID, "AUFEVENEMENTAPERCU"); %>
<liferay-util:include page="/html/portlet/asset_publisher/display/full_content_custom_link.jsp"/>
