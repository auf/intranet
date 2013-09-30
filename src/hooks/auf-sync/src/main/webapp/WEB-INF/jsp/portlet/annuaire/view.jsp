<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

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



<div class="my-class">
    <portlet:renderURL var="searchURL">
        <portlet:param name="action" value="searchPerson" />
    </portlet:renderURL>        
    <aui:form action="${searchURL}" method="post" name="fm">
        <table>
            <td>
            <aui:input type="text" name="search" label="" placeholder="test" style="width:110%;"/>
            </td>
            <td style="padding-left:11%">
            <liferay-ui:icon image="search" cssClass="iconeRecherche"  url="#" />
            </td>
        </table>
    </aui:form>     
    <br /><br />
    <div>
        <h5><liferay-ui:message key="search-a-person-of-the-organisation" /></h5>
    </div>
    <br />
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#p_p_id<portlet:namespace /> .iconeRecherche').click(function(e) {
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            $("[name='<portlet:namespace />fm']")[0].submit();
            return false;
        });
    });
</script>


<c:if test="${displaySearch}">
	
	<table>
		<thead>
		<tr>
			<th><liferay-ui:message key="heading-login" /></th>
			<th><liferay-ui:message key="heading-firstname" /></th>
			<th><liferay-ui:message key="heading-lastname" /></th>
			<th><liferay-ui:message key="heading-email" /></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="user" items="${users}">
		<tr>
			<td>
				<portlet:renderURL var="url">
	                <portlet:param name="action" value="viewPerson" />
	                <portlet:param name="idEmploye" value="${user.email}" />
	            </portlet:renderURL>
	            <a href="${url}">${user.login}</a>
	        </td>
			<td>${user.lastName}</td>
			<td>${user.firstName}</td>
			<td>${user.email}</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>

</c:if>

<c:if test="${displayProfile}">

<%

%>           

	<table>
		<thead>
				<tr>
			<th><liferay-ui:message key="label-field" /></th>
			<th><liferay-ui:message key="label-value" /></th>

		</tr>
		</thead>
		<tbody>
		<tr>
			<td><liferay-ui:message key="heading-portrait" /></td>
			<td><img alt="${user.lastName}" class="user-logo" src="${userPortraitUrl}" /></td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-login" /></td>
			<td>${user.login}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-firstname" /></td>
			<td>${user.firstName}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-lastname" /></td>
			<td>${user.lastName}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-email" /></td>
			<td>${user.email}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-gender" /></td>
			<td>${user.gender}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-post-title" /></td>
			<td>${user.postDesc}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-post-telephone" /></td>
			<td>${user.telPost}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-tel-ip" /></td>
			<td>${user.telIp}</td>
		</tr>
		<tr>
			<td><liferay-ui:message key="heading-tel-ip-nomade" /></td>
			<td>${user.telIpNomade}</td>
		</tr>
		</tbody>
	</table>

</c:if>