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
        <div class="box_centre">
         	<div class="news">
                <div class="photo2"><img src="${userPortraitUrl}"></div>
                <div class="desc">
                    <h1>Identification</h1>
                    <h2>${userAnnuaire.getFullName()}</h2>
                    <p class="nomarg">
                        ${userAnnuaire.getPostDesc()}<br>
                        ${userAnnuaire.getServiceName()}<br>
                        ${userAnnuaire.getImplantationName()}<br>
                        ${userAnnuaire.getImplantationCity()}<br>
                        ${userAnnuaire.getImplantationRegionName()}
                    </p>
                 </div>
            </div>
        </div>

            <!-- tableau -->
		<div class="ui-tabs ui-widget ui-widget-content ui-corner-all" id="tabs">
            <ul role="tablist" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                <li aria-selected="true" aria-labelledby="ui-id-1" aria-controls="tabs-1" tabindex="0" role="tab" class="ui-state-default ui-corner-top ui-tabs-active ui-state-active"><a id="ui-id-1" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-1">COORDONN&Eacute;ES</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-2" aria-controls="tabs-2" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-2" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-2">PROJETS</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-3" aria-controls="tabs-3" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-3" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-3">ESPACES COLLABORATIFS</a></li>
                <li aria-selected="false" aria-labelledby="ui-id-4" aria-controls="tabs-4" tabindex="-1" role="tab" class="ui-state-default ui-corner-top"><a id="ui-id-4" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-4">CENTRES D'INT&Eacute;R&Ecirc;TS</a></li>
            </ul>
            <div aria-hidden="false" aria-expanded="true" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-1" id="tabs-1">
                <div class="news">
                	<ul>
                    	<li>Courriel : <a href="mailto:${userAnnuaire.getEmail()}">${userAnnuaire.getEmail()}</a></li>
                        <li>T&eacute;l&eacute;phone IP : ${userAnnuaire.getTelIp()}</li>
                        <li>T&eacute;l&eacute;phone IP Nomade : ${userAnnuaire.getTelIpNomade()}</li>
                        <li>T&eacute;l&eacute;phone : ${userAnnuaire.getTelephone()}</li>
                        <li>T&eacute;l&eacute;copie : ${userAnnuaire.getTelecopier()}</li>
                        <li>
                            Adresse postale : ${userAnnuaire.getPostalAddress()}
                        </li>
                    </ul>
                </div>
            </div>
            <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-2" id="tabs-2">
            	<div class="news">
                	${userProjects}
                </div>
            </div>
            <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-3" id="tabs-3">
            	<div class="news">
                	<p>${userAnnuaire.getFullName()} participe aux espaces collaboratifs suivants :</p>
                	<ul>
                	<c:forEach var="site" items="${userSites}">
						<li>
                            <a href="/web${site.getFriendlyURL()}">
                                ${site.getDescriptiveName()}
                            </a>&nbsp;:&nbsp;${site.getDescription()}
                        </li>
					</c:forEach>
                    </ul>
                    <p>et aux p&ocirc;les/r&eacute;gions suivants:</p>
                	<ul>
                	<c:forEach var="pole" items="${userPoles}">
						<li>
                            <a href="/web${pole.getFriendlyURL()}">
                                ${pole.getDescriptiveName()}
                            </a>&nbsp;:&nbsp;${pole.getDescription()}
                        </li>
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

           <p>Pour toute question ou remarque au sujet de cette fiche, vous pouvez prendre contact avec <a href="mailto:intranet@auf.org">l'administrateur de l'intranet</a></p>

	</c:when>
	<c:otherwise>
		<jsp:include page="auf-searchbox.jsp"/>

		<c:if test="${displaySearch}">

			<h2>R&eacute;sultats: <span>${users.size()}</span> employ&eacute;s correspondent &agrave; votre recherche</h2>

            <div class="box_centre">
	            <c:forEach var="user" items="${users}">
					<div class="news">
                        <div class="photo"><img src="${user.getPortraitUrl()}" width="50"></div>
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
							<p class="date">
								<c:if test="${not empty user.getEmploye().getTelIp()}">
									T&eacute;l&eacute;phone IP : ${user.getEmploye().getTelIp()}
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</c:if> 
								
								<c:if test="${not empty user.getEmploye().getTelIp()}">
									T&eacute;l&eacute;phone : ${user.getEmploye().getTelephone()}
								</c:if> 
							</p>
                    	</div>
                    	<div class="desc">
                            <!-- <p class="date">${user.getEmploye().getTelIp()}</p>
                            <p class="date">${user.getEmploye().getTelIpNomade()}</p> -->
                    	</div>
                    </div>
				</c:forEach>
            </div>

		</c:if>

	</c:otherwise>
</c:choose>

