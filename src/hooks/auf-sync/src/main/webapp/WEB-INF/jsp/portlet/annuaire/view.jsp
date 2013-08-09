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
        <h5>Rechercher une personne de l'organisation.</h5>
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
	<p>
	The list size: ${size}
	</p>
	
	<table>
		<thead>
		<tr>
			<th>Login</th>
			<th>Surname</th>
			<th>Prenom</th>
			<th>Courriel</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="u" items="${users}">
		<tr>
			<td>
				<portlet:renderURL var="url">
	                <portlet:param name="action" value="viewPerson" />
	                <portlet:param name="idEmploye" value="${u.email}" />
	            </portlet:renderURL>
	            <a href="${url}">${u.login}</a>
	        </td>
			<td>${u.lastName}</td>
			<td>${u.firstName}</td>
			<td>${u.email}</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>

</c:if>

<c:if test="${displayProfile}">                 

	<table>
		<thead>
				<tr>
			<th>Champ</th>
			<th>Valeur</th>

		</tr>
		</thead>
		<tbody>
		<tr>
			<td>Login</td>
			<td>${user.login}</td>
		</tr>
		<tr>
			<td>Prenom</td>
			<td>${user.firstName}</td>
		</tr>
		<tr>
			<td>Surnom</td>
			<td>${user.lastName}</td>
		</tr>
		<tr>
			<td>Courriel</td>
			<td>${user.email}</td>
		</tr>
		<tr>
			<td>Gender</td>
			<td>${user.gender}</td>
		</tr>
		<tr>
			<td>Titre du poste</td>
			<td>${user.postDesc}</td>
		</tr>
		<tr>
			<td>Tel Poste</td>
			<td>${user.telPost}</td>
		</tr>
		<tr>
			<td>Tel IP</td>
			<td>${user.telIp}</td>
		</tr>
		<tr>
			<td>Tel IP Nomade</td>
			<td>${user.telIpNomade}</td>
		</tr>
		</tbody>
	</table>

</c:if>