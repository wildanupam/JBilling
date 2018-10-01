  %{--
   jBilling - The Enterprise Open Source Billing System
   Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

   This file is part of jbilling.
   
   jbilling is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   jbilling is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.
   
   You should have received a copy of the GNU Affero General Public License
   along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 
  --}%

<%@ page import="com.sapienter.jbilling.server.metafields.EntityType; com.sapienter.jbilling.server.metafields.MetaFieldBL" %>
<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO" %>
<%@ page import="com.sapienter.jbilling.server.util.Constants" %>

<%--
  _atf

  @author Khurram Cheema
  @since  23-04-2014
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>
    
    <g:if test="${MetaFieldBL.getAllAvailableFieldsList(session['company_id'], EntityType.ACCOUNT_TYPE).toList().size() > 0}">
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg" style="float:left;">
                    <g:set var="company" value="${CompanyDTO.get(session['company_id'])}"/>
                    <g:accountTypeMetaFields filter="${filter}" />
                </div>
                <div class="input-bg">
                    <g:textField name="filters.${filter.name}.stringValue" value="${filter.stringValue}" class="{validate:{ maxlength: 50 }}"/>
                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.value.label"/></label>
            </div>
        </fieldset>
    </div>
    </g:if>
    <g:else>
    	<fieldset>
    		<div class="slide">
		    	<div class="input-row">
		    		<p>Fields Not Available</p>
		    	</div>
	    	</div>
    	</fieldset>
    </g:else>
</div>

