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

<%@ page import="org.apache.commons.lang.StringEscapeUtils; com.sapienter.jbilling.server.user.contact.db.ContactDTO"%>

<%-- 
    Orders list template. 
    
    @author Vikas Bodani
    @since 20-Jan-2011
 --%>

<div class="table-box">
	<div class="table-scroll">
		<table id="orders" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th class="small"><g:remoteSort action="list" sort="id"
							update="column1">
							<g:message code="order.label.id" />
						</g:remoteSort></th>
					<th class="large"><g:remoteSort action="list"
							sort="contact.firstName, contact.lastName, contact.organizationName, u.userName"
							alias="[contact: 'baseUserByUserId.contact']" update="column1">
							<g:message code="order.label.customer" />
						</g:remoteSort></th>
					<g:isRoot>
						<th class="small"><g:remoteSort action="list"
								sort="company.description"
								alias="[company: 'baseUserByUserId.company']" update="column1">
								<g:message code="order.label.company" />
							</g:remoteSort></th>
					</g:isRoot>
					<th class="small"><g:remoteSort action="list"
							sort="createDate" update="column1">
							<g:message code="order.label.date" />
						</g:remoteSort></th>
					<th class="small"><g:message code="order.label.amount" /></th>
					<th class="tiny3"><g:message code="order.label.parent.child" />
					</th>
				</tr>
			</thead>
			<tbody>
				<g:each var="ordr" in="${orders}">
					<g:set var="contact"
						value="${ContactDTO.findByUserId(ordr?.baseUserByUserId?.id)}" />
					<tr id="order-${ordr.id}"
						class="${(order?.id == ordr?.id) ? 'active' : ''}">
						<td><g:remoteLink breadcrumb="id" class="cell" action="show"
								id="${ordr.id}" params="['template': 'show']"
								before="register(this);" onSuccess="render(data, next);">
								${ordr.id}
							</g:remoteLink></td>
						<td><g:remoteLink breadcrumb="id" class="double cell"
								action="show" id="${ordr.id}" params="['template': 'show']"
								before="register(this);" onSuccess="render(data, next);">
								<strong> <g:if
										test="${contact?.firstName || contact?.lastName}">
										${contact.firstName} &nbsp;${contact.lastName}
									</g:if> <g:else>
										${ordr?.baseUserByUserId?.userName}
									</g:else>
								</strong>
								<em>
									${contact?.organizationName}
								</em>
							</g:remoteLink></td>
						<g:isRoot>
							<td><g:remoteLink breadcrumb="id" class="cell" action="show"
									id="${ordr.id}" params="['template': 'show']"
									before="register(this);" onSuccess="render(data, next);">
									<strong>
										${ordr?.baseUserByUserId?.company.description}
									</strong>
								</g:remoteLink></td>
						</g:isRoot>
						<td><g:remoteLink breadcrumb="id" class="cell" action="show"
								id="${ordr.id}" params="['template': 'show']"
								before="register(this);" onSuccess="render(data, next);">
								<g:formatDate date="${ordr?.createDate}"
									formatName="date.pretty.format" />
							</g:remoteLink></td>
						<td><g:remoteLink breadcrumb="id" class="cell" action="show"
								id="${ordr.id}" params="['template': 'show']"
								before="register(this);" onSuccess="render(data, next);">
								<g:formatNumber number="${ordr?.total}" type="currency"
									currencySymbol="${ordr?.currency?.symbol}" />
							</g:remoteLink></td>
						<td class="center">
						<g:if test="${null != ordr}">
                                <g:set var="childrenSize" value="${null != children?.getAt(ordr?.id) ? children?.getAt(ordr?.id) : 0}"/>
                                <g:set var="isParent" value="${childrenSize > 0}"/>
                                <g:if test="${isParent && ordr?.parentOrder}">
                                    <%-- is a parent, but also a child of another order --%>
                                    <g:remoteLink action="suborders" id="${ordr.id}" before="register(this);" onSuccess="render(data, next);">
                                        <img src="${resource(dir:'images', file:'icon17.gif')}" alt="parent and child" />
                                        <span>${childrenSize}</span>
                                    </g:remoteLink>
                                </g:if>
                                <g:elseif test="${isParent && !ordr.parentOrder}">
                                    <%-- is a top level parent --%>
                                    <g:remoteLink action="suborders" id="${ordr.id}" before="register(this);" onSuccess="render(data, next);">
                                        <img src="${resource(dir:'images', file:'icon18.gif')}" alt="parent" />
                                        <span>${childrenSize}</span>
                                    </g:remoteLink>
                                </g:elseif>
                                <g:elseif test="${!isParent && ordr.parentOrder}">
                                   <%--  is a child order, but not a parent --%>
                                    <img src="${resource(dir:'images', file:'icon19.gif')}" alt="child" />
                                </g:elseif>
                            </g:if>
                        </td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</div>
</div>

<g:if test="${!parent}">
	<div class="pager-box">
		<div class="row">
			<div class="results">
				<g:render template="/layouts/includes/pagerShowResults"
					model="[steps: [10, 20, 50], update: 'column1', ids: ids]" />
			</div>
			<div class="download">
				<sec:access url="/order/csv">
					<g:link action="csv" id="${order?.id}">
						<g:message code="download.csv.link" />
					</g:link>
				</sec:access>
			</div>
		</div>

		<div class="row">
			<util:remotePaginate controller="order" action="list"
				params="${sortableParams(params: [partial: true])}"
				total="${orders?.totalCount ?: 0}" update="column1" />
		</div>
	</div>

</g:if>
