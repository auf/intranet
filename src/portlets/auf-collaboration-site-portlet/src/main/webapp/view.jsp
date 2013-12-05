<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<portlet:defineObjects />


<h2>Voici la liste des espaces collaboratifs auxquelles vous appartenez</h2>

<c:forEach var="site" items="${userSites}" >
<div>
	<h3><a href="/web${site.getFriendlyURL()}">${site.getDescriptiveName()}</a></h3>
	<p>${site.getDescription()}</p>
</div>	
</c:forEach>

<h2>Voici la liste des espaces qui existent vous puvez vous abonnez en envoyant un mail au responsable</h2>

<c:forEach var="site" items="${availableSites}" >
<div>
	<h3><a href="${site.getEmailAddresses()}">${site.getSite().getDescriptiveName()}</a></h3>
	<p>${site.getSite().getDescription()}</p>
</div>	
</c:forEach>