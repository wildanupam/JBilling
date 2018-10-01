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

<%@page import="com.sapienter.jbilling.server.process.db.AgeingEntityStepDTO" %>

<div class="form-edit">

    <div class="heading">
        <strong><g:message code="configuration.title.collections"/></strong>
    </div>

    <g:form name="save-aging-form" action="saveAging">
        <g:render template="/config/aging/steps" model="[ageingSteps:ageingSteps]"/>
    </g:form>
    <g:settingEnabled property="collections.run.ui">
        <g:form name="run-collections-form" action="runCollectionsForDate">
            <g:render template="/config/aging/run"/>
        </g:form>
    </g:settingEnabled>
</div>
