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