<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<portlet:defineObjects />

<h2 class="first-h2">Liste des espaces collaboratifs auxquelles vous appartenez</h2>

<div class="container clearfix">
	<c:forEach var="site" items="${userSites}" >
	<div class="site set-block">
		<div class="auf-site-image"><a href="/web${site.getSite().getFriendlyURL()}"><img src="/image/company_logo?img_id=${site.getSite().getPublicLayoutSet().getLogoId()}" /></a></div>
		<div class="auf-site-description">
			<div><span class="auf-site-highlight">Titre : </span><a href="/web${site.getSite().getFriendlyURL()}">${site.getSite().getDescriptiveName()}</a></div>
			<div><span class="auf-site-highlight">Description : </span>${site.getSite().getDescription()}</div>
			<div><span class="auf-site-highlight">Nombre de membres : </span>${site.getMembersCount()}</div>
		</div>
	</div>	
	</c:forEach>
</div>

<h2>Liste des espaces qui existent vous pouvez vous abonnez en envoyant un mail au responsable</h2>

<div class="container clearfix">
	<c:forEach var="site" items="${availableSites}" >
	<div class="set-block">
		<div class="auf-site-image"><a href="${site.getEmailAddresses()}"><img src="/image/company_logo?img_id=${site.getSite().getPublicLayoutSet().getLogoId()}" /></a></div>
		<div class="auf-site-description">
			<div><span class="auf-site-highlight">Titre : </span><a href="${site.getEmailAddresses()}">${site.getSite().getDescriptiveName()}</a></div>
			<div><span class="auf-site-highlight">Description : </span>${site.getSite().getDescription()}</div>
			<div><span class="auf-site-highlight">Nombre de membres : </span>${site.getMembersCount()}</div>
		</div>
	</div>	
	</c:forEach>
</div>