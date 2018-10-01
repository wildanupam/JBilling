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

<%@page import="com.sapienter.jbilling.server.order.db.OrderLineDTO"%>

<g:each var = "charge" in = "${OrderLineDTO.get(lineId)?.orderChangesSortedByStartDate}">
<tr>
    <td class = "left">${formatDate(date: charge.startDate,        formatName: 'date.format')}</td>
    <td class = "left">${formatDate(date: charge.endDate,          formatName: 'date.format')}</td>
    <td class = "left">${formatDate(date: charge.createDatetime,   formatName: 'date.format')}</td>
    <td class = "left">${formatDate(date: charge.nextBillableDate, formatName: 'date.format')}</td>
    <td class = "qty">${formatNumber(number: charge.quantity,      formatName: 'decimal.format')}</td>
    <td class = "money">${formatNumber(number: charge.price,       formatName: 'price.format')}</td>
</tr>
</g:each>
