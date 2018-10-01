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

<%@ page import="com.sapienter.jbilling.common.Constants" %>

<html>
<head>
    <meta name="layout" content="configuration" />
    <g:preferenceEquals preferenceId="${Constants.PREFERENCE_USE_JQGRID}" value="1">
        <link type="text/css" href="${resource(file: '/css/ui.jqgrid.css')}" rel="stylesheet" media="screen, projection" />
        <g:javascript src="jquery.jqGrid.min.js"  />
        <g:javascript src="jqGrid/i18n/grid.locale-${session.locale.language}.js"  />
    </g:preferenceEquals>
</head>
<body>
    <!-- selected configuration menu item -->
    <content tag="menu.item">orderStatus</content>

    <content tag="column1">
        <g:render template="orderStatusTemplate" />
    </content>

    <content tag="column2">
        <g:if test="${selected}">
            <!-- show selected role -->
            <g:render template="show" model="[ selected: orderStatusList]"/>
        </g:if>
        <g:elseif test="${orderStatusWS}">
            <!-- show new or editing role (used when new period fails on validation ) -->
            <g:render template="edit" model="[ orderStatusWS: orderStatusWS]"/>
        </g:elseif>
    </content>
</body>
</html>
