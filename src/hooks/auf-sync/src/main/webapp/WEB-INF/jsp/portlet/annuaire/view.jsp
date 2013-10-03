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


<c:choose>
	<c:when test="${displayProfile}">
		
		<div class="ariane"><a href="#">Accueil</a> &gt; <a href="#">Annuaire des employés</a> &gt; <a href="#">${user.getFullName()}</a></div>

            
            <div class="box_centre">
             	<div class="news">
                        <div class="photo2"><img src="${userPortraitUrl}"></div>
                        <div class="desc">
                            <h2>${user.getFullName()}</h2>
                            <p class="nomarg">${user.getPostDesc()}<br>
                            ${user.getServiceName()}<br>
                            ${user.getImplantationName()}<br>
                            ${user.getImplantationCity()}<br>
                            ${user.getImplantationRegionName()}</p>
                         </div>
                    </div>
            </div>
            
            <!-- tableau -->
			<div class="ui-tabs ui-widget ui-widget-content ui-corner-all" id="tabs">
                <ul role="tablist" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                <li aria-selected="true" aria-labelledby="ui-id-1" aria-controls="tabs-1" tabindex="0" role="tab" class="ui-state-default ui-corner-top ui-tabs-active ui-state-active"><a id="ui-id-1" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-1">COORDONNÉES</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-2" aria-controls="tabs-2" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-2" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-2">PROJETS</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-3" aria-controls="tabs-3" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-3" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-3">ESPACES COLLABORATIFS</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-4" aria-controls="tabs-4" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-4" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-4">CENTRES D'INTERETS</a></li>
                </ul>
                <div aria-hidden="false" aria-expanded="true" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-1" id="tabs-1">
                    <div class="news">
                    	<table>
                        	<tr><td>Courriel : </td><td><a href="mailto:${user.getEmail()}">${user.getEmail()}</a></td></tr>
                            <tr><td>Téléphone IP : </td><td>${user.getTelIp()}</td></tr>
                            <tr><td>Téléphone IP Nomade : </td><td>${user.getTelIpNomade()}</td></tr>
                            <tr><td>Téléphone : </td><td>${user.getTelephone()}</td></tr>
                            <tr><td>Télécopie : </td><td>${user.getTelecopier()}</td></tr>
                            <tr><td>Adresse postale : </td><td>${user.getPostalAddress()}</td></tr>
                        </table>
                    </div>
                </div>
                <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-2" id="tabs-2">
                	<div class="news">
                    	${userProjects}
                    </div>
                </div>
                <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-3" id="tabs-3">
                	<div class="news">
                    	<p>${user.getFullName()} participe aux espaces collaboratifs suivants:</p>
                    	<ul>
                    	<c:forEach var="site" items="${userSites}">
							<li><a href="${site.getFriendlyURL()}">${site.getName()}</a>${site.getDescription()}</li>
						</c:forEach>
                        </ul>
                    </div>
                </div>
                <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-4" id="tabs-4">
                	<div class="news">
                    	${userInterests}
                    </div>
                </div>
               </div>
               <!-- fin tableau -->
               
               <p>Pour toute question ou remarque au sujet de cette fiche, vous pouvez prendre contact avec <a href="mailto:odette@auf.org">Odette Tremblay</a></p>

	</c:when>
	<c:otherwise>
		<jsp:include page="auf-searchbox.jsp"/>
		
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

	</c:otherwise>
</c:choose>

