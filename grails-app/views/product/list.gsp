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
    <meta name="layout" content="panels" />
    <g:preferenceEquals preferenceId="${Constants.PREFERENCE_USE_JQGRID}" value="1">
        <link type="text/css" href="${resource(file: '/css/ui.jqgrid.css')}" rel="stylesheet" media="screen, projection" />
        <g:javascript src="jquery.jqGrid.min.js"  />
        <g:javascript src="jqGrid/i18n/grid.locale-${session.locale.language}.js"  />
    </g:preferenceEquals>
</head>
<body>
    <g:if test="${!selectedProduct}">
        <!-- show product categories and products -->
        <content tag="column1">
            <g:render template="categoriesTemplate" model="[categories: categories]"/>
        </content>

        <content tag="column2">
            <g:render template="productsTemplate" model="[products: products]"/>
        </content>
    </g:if>
    <g:else>
        <!-- show product list and selected product -->
        <content tag="column1">
            <g:render template="productsTemplate" model="[products: products]"/>
        </content>

        <content tag="column2">
            <g:render template="show" model="[selectedProduct: selectedProduct]"/>
        </content>
    </g:else>
</body>
</html>
