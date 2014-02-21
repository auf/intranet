<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<c:forEach var="pole" items="${availablePoles}" >
<ul>
	<li><a href="/web${pole.getFriendlyURL()}">${pole.getDescriptiveName()}</a></li>
</ul>	
</c:forEach>