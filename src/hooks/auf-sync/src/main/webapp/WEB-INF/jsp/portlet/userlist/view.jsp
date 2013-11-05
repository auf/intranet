<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.model.User" %>
<%@ page import="com.liferay.portal.security.auth.CompanyThreadLocal" %>
<%@ page import="com.liferay.portal.service.UserLocalServiceUtil" %>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>


<portlet:defineObjects />

<div class="box_centre">
	<c:forEach var="user" items="${users}">
		<div class="news">
			<div class="photo"><img src="${user.getPortraitUrl()}" width="50" height="50"></div>
			<div class="desc">
				<portlet:renderURL var="url">
					<portlet:param name="action" value="viewPerson" />
					<portlet:param name="idEmploye" value="${user.getEmploye().getEmail()}" />
				</portlet:renderURL>
				<h4><a href="${url}">${user.getEmploye().getFullName()} (${user.getEmploye().getEmail()})</a></h4>
				<p class="date">${user.getEmploye().getPostDesc()}</p>
				<p class="date">
					${user.getEmploye().getImplantationName()} - ${user.getEmploye().getImplantationCity()}
				</p>
			</div>
			<div class="desc">
				<!-- <p class="date">${user.getEmploye().getTelIp()}</p>
				<p class="date">${user.getEmploye().getTelIpNomade()}</p> -->
			</div>
		</div>
	</c:forEach>
</div>