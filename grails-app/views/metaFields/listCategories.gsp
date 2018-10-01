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

    <r:script disposition="head">
        var selected;

        // todo: should be attached to the ajax "success" event.
        // row should only be highlighted when it really is selected.
        $(document).ready(function() {
            $('.table-box li').bind('click', function() {
                if (selected) selected.attr("class", "");
                selected = $(this);
                selected.attr("class", "active");
            })
        });
    </r:script>
</head>

<body>

<!-- selected configuration menu item -->
<content tag="menu.item">metaFields</content>

<g:if test="${!selected}">
    <content tag="column1">
        <g:render template="categoriesTemplate" model="['lst': lst]"/>
    </content>

    <g:if test="${selectedCategory}">
        <content tag="column2">
            <g:render template="listMetaFieldsTemplate" model="['lstByCategory': lstByCategory]"/>
        </content>
    </g:if>
</g:if>
<g:else>
    <content tag="column1">
        <g:render template="listMetaFieldsTemplate" model="['lstByCategory': lstByCategory]"/>
    </content>

    <content tag="column2">
        <g:render template="show" model="['selected': selected]"/>
    </content>

</g:else>
</body>
</html>