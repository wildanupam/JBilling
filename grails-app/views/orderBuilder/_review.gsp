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

<%@ page import="com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.common.Constants" %>

<%--
  Renders an OrderWS as a quick preview of the order being built. This view also allows
  individual order lines to be edited and removed from the order.

  @author Brian Cowdery
  @since 23-Jan-2011
--%>

<div id="review-box">

    <!-- error messages -->
    <div id="review-messages">
        <g:if test="${params.errorMessages}">
            <div class="msg-box error">
                <ul>
                    %{--flatten is used if in case only string is return then convert to list--}%
                    <g:each var="message" in="${[params.errorMessages]?.flatten().findAll { it != null }}">
                        <li>${message}</li>
                    </g:each>
                </ul>
            </div>

            <ul></ul>
        </g:if>

        <g:if test="${repeatedProductError}">
            <div class="msg-box error">
                <ul>
                    <g:each var="message" in="${repeatedProductError}">
                        <li>${message}</li>
                    </g:each>
                </ul>
            </div>
        </g:if>
         <g:if test="${notProratingMessage}">
            <div class="msg-box error">
                <ul>
                    <g:each var="message" in="${notProratingMessage}">
                        <li>${message}</li>
                    </g:each>
                </ul>
            </div>
        </g:if>
        <g:if test="${params.message}">
            <div class="msg-box successfully">
                ${params.message}
            </div>
        </g:if>
    </div>

    <!-- order header -->
    <div class="header">
        <div class="column">
            <h1><g:message code="order.review.id" args="${[order?.id > 0 ? order.id : '']}"/></h1>
            <h3>
                ${user.userName}
            </h3>
        </div>

        <div class="column">
            <h2 class="right capitalize">
                <g:set var="period" value="${orderPeriods.find{ it.id == order.period }}"/>
                ${period?.getDescription(session['language_id'])},

                <g:set var="orderBillingType" value="${orderBillingTypes.find{ it.id == order.billingTypeId }}"/>
                ${orderBillingType?.getDescription(session['language_id'])}
            </h2>

            <h3 class="right capitalize">
                <g:set var="activeSince" value="${formatDate(date: order.activeSince ?: order.createDate, formatName: 'date.pretty.format')}"/>
                <g:set var="activeUntil" value="${formatDate(date: order.activeUntil, formatName: 'date.pretty.format')}"/>

                <g:if test="${order.activeUntil}">
                    <g:message code="order.review.active.date.range" args="[activeSince, activeUntil]"/>
                </g:if>
                <g:else>
                    <g:message code="order.review.active.since" args="[activeSince]"/>
                </g:else>
            </h3>
        </div>

        <div style="clear: both;"></div>
    </div>

    <hr/>

    <g:set var="currency" value="${CurrencyDTO.get(order.currencyId)}"/>
    <!-- list of order lines -->
    <ul id="review-lines">
        <g:each var="line" status="index" in="${order.orderLines}">
            <g:if test="${line.deleted == 0}">
                <g:render template="orderLine" model="[ order: order, line: line, index: index, user: user ]"/>
            </g:if>
        </g:each>

        <g:if test="${!order.orderLines}">
            <li><em><g:message code="order.review.no.order.lines"/></em></li>
        </g:if>
    </ul>

    <hr/>

            <!-- order total -->
            <div class="total">
                <g:message code="order.review.total" args="[formatNumber(number: order.getTotalAsDecimal() ? order.getTotalAsDecimal(): new BigDecimal('0.00'), type: 'currency', currencySymbol: currency.symbol, maxFractionDigits: 4)]" encodeAs="None"/>
            </div>

    <!-- order notes -->
    <g:if test="${order.notes}">
        <div class="box-text">
            <ul>
                <li><p>${order.notes}</p></li>
            </ul>
        </div>
    </g:if>

    <div class="btn-box order-btn-box">
        <g:link class="submit save" action="edit" params="[_eventId: 'save']"  onClick="disableButtons()">
            <span><g:message code="button.save"/></span>
            <g:hiddenField name="saveInProgress" value="false"/>
        </g:link>

        <g:link class="submit cancel" action="edit" params="[_eventId: 'cancel']">
            <span><g:message code="button.cancel"/></span>
        </g:link>
    </div>

    <script type="text/javascript">
        $('#review-lines li.line').click(function() {
            var id = $(this).attr('id');
            $('#' + id).toggleClass('active');
            $('#' + id + '-editor').toggle('blind');
        });
        function disableButtons() {
            $('.cancel').bind('click', false);
            $('.save').bind('click', false);
        }
    </script>

</div>

<script type="text/javascript">
    showTabWithoutClickIfNeeded('ui-tabs-review');
</script>
