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


<div class="column-hold">
    <div class="heading">
        <strong>
            ${selected.getDescription()}
        </strong>
    </div>

    <div class="box">
        <div class="sub-box">
          <table class="dataTable" cellspacing="0" cellpadding="0">
              <tbody>
              <tr>
                  <td><g:message code="metaFieldGroup.label.id"/></td>
                  <td class="value">${selected.id}</td>
              </tr>
  
              <tr>
                  <td><g:message code="metaFieldGroup.label.entityType"/></td>
                  <td class="value">${selected.entityType.name()}</td>
              </tr>
             
              <tr>
                  <td><g:message code="metaField.label.displayOrder"/></td>
                  <td class="value">${selected.displayOrder}</td>
              </tr>

              </tbody>
          </table>
        </div>
    </div>

    <div class="heading">
        <strong><g:message code="tabs.head.selectedfields"/></strong>
    </div>
    <div class="box">
        <div class="sub-box">
            <table class="dataTable" cellspacing="0" cellpadding="0" width="100%" >
                <tbody>
                    <!-- group metafields -->
                    <g:render template="/metaFieldGroup/metafields" model="[model: selected]"/>
                </tbody>
            </table>
        </div>
    </div>



    <div class="btn-box">
        <div class="row">
            <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
            <g:link action="clone" id="${selected.id}" class="submit create"><span><g:message code="button.clone"/></span></g:link>
            <a onclick="showConfirm('delete-${selected.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
        </div>
    </div>

    <g:render template="/confirm"
              model="['message': 'metaField.delete.confirm',
                      'controller': 'metaFieldGroup',
                      'action': 'delete',
                      'id': selected.id,
                      'ajax': false
                     ]"/>
</div>