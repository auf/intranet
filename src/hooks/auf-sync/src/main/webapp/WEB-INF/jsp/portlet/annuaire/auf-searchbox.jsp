<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />

<div class="my-class">
	         
    <portlet:renderURL var="searchUrl">
        <portlet:param name="action" value="searchPerson" />
        <portlet:param name="search" value="default" />
    </portlet:renderURL>
    <portlet:renderURL var="tousUrl">
        <portlet:param name="action" value="searchPerson" />
        <portlet:param name="search" value="tous" />
    </portlet:renderURL>
    <aui:form action="${searchUrl}" method="post" name="fm">
 		<div>      
        Recherche <aui:input type="text" name="name" label="" placeholder="Recherche une personne par nom"/> <liferay-ui:icon image="search" cssClass="searchIcon"  url="#" />
        <a href="#" onclick="$('#auf-annuaire-advanced-search').toggle();">Avancée</a>
        
        <aui:button value="Rechercher" name="search" type="submit" style="display:none;" />
	           
	    <aui:button value="Tous" name="search" type="submit" onclick="form.action='${tousUrl}';"/>
		
        </div>
        
        <div class="box_centre" id="auf-annuaire-advanced-search" style="display:none;">
	    	<div class="rechercher">
	    		<aui:select name="implantation" label="Implantation:" id="id_implantation" showEmptyOption="<%= true %>" >
			        <c:forEach items="${implantations}" var="implantation">
			            <aui:option value="${implantation.getId()}" label="${implantation.getName()}" selected="${selectedImplantation == implantation}"/>
			        </c:forEach>
			    </aui:select>
	    	</div>
	
			<div class="rechercher">
				<aui:select name="type" label="Type d'implantation:" id="id_type" showEmptyOption="<%= true %>" >
			        <c:forEach items="${types}" var="type">
			  			<aui:option value="${type}" label="${type}" selected="${selectedType == type}"/>
			        </c:forEach>
			    </aui:select>
			</div>
			
			<div class="rechercher">
				<aui:select name="city" label="Ville:" id="id_ville" showEmptyOption="<%= true %>" >
			        <c:forEach items="${cities}" var="city">
			  			<aui:option value="${city}" label="${city}" selected="${selectedCity == city}"/>
			        </c:forEach>
			    </aui:select>
			</div>
			
			<div class="rechercher">
				<aui:select name="country" label="Pays:" id="id_pays" showEmptyOption="<%= true %>" >
					<c:forEach items="${countries}" var="country">
						<aui:option value="${country.getCode()}" label="${country.getName()}" selected="${selectedCountry == country}"/>
					</c:forEach>
				</aui:select>
			</div>
			
			<div class="rechercher">
				<aui:select name="region" label="Régions:" id="id_region" showEmptyOption="<%= true %>" >
					<c:forEach items="${regions}" var="region">
						<aui:option value="${region.getId()}" label="${region.getName()}" selected="${selectedRegion == region}"/>
					</c:forEach>
				</aui:select>
			</div>
	       
	       <div class="boutonEnvoi">
	           <aui:button value="Rechercher" name="search" type="submit" />
	           <aui:button value="Réinitialiser" type="reset"/>
	       </div>
	       
		</div>
    </aui:form>     
    
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#p_p_id<portlet:namespace /> .searchIcon').click(function(e) {
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            $("[name='<portlet:namespace />fm']")[0].submit();
            return false;
        });
    });
</script>

   