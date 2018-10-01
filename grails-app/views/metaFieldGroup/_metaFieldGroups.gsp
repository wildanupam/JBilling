<%@ page import="org.apache.commons.lang.StringEscapeUtils; org.apache.commons.lang.StringUtils" %>
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


<div class="table-box">
    <table id="roles" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th><g:message code="metaFieldGroup.th.name"/></th>
            </tr>
        </thead>

        <tbody>
            <g:each var="group" in="${groups}">

                <tr id="metaFieldGroup-${group.id}" class="${selected?.id == group.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${group.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${StringUtils.abbreviate(StringEscapeUtils.escapeHtml(group?.getDescription()), 50)}</strong>
                            <em><g:message code="table.id.format" args="[group.id]"/></em>
                        </g:remoteLink>
                    </td>
                </tr>

            </g:each>
        </tbody>
    </table>
</div>
<div class="pager-box">
    %{-- remote pager does not support "onSuccess" for panel rendering, take a guess at the update column --}%
    <g:set var="updateColumn" value="${listColumn?:'column1' }"/>

    <div class="row">
        <div class="results">
            <g:render template="pagerShowResults" id="${entityType}" params="[template:'list']" model="[steps: [10, 20, 50], update: updateColumn, id:entityType,template:'list' ]" />
        </div>
     </div>

    <div class="row">
        <util:remotePaginate controller="metaFieldGroup" id="${entityType }" params="[template:'list']" action="list" total="${groups?.totalCount ?: 0}" update="${updateColumn}"/>
    </div>
</div>

<div class="btn-box">
    <g:link action="edit" class="submit add" params="[entityType: params.id]">
        <span><g:message code="button.create"/></span>
    </g:link>
</div>
