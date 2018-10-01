/*
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
 */

package com.sapienter.jbilling.client.filters

import com.sapienter.jbilling.server.payment.db.PaymentMethodDAS
import jbilling.Filter
import jbilling.FilterConstraint
import jbilling.FilterType

/**
 * FilterFactory

 * @author Brian Cowdery
 * @since  03-12-2010
 */
class FilterFactory {

    static baseFilters = ([
        ALL: [
            new Filter(type: FilterType.ALL, constraintType: FilterConstraint.EQ, field: 'id', template: 'id', visible: true)
        ],
        INVOICE: [
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.DATE_BETWEEN, field: 'dueDate', template: 'date', visible: true),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.STATUS, field: 'invoiceStatus', template: 'invoice/status', visible: true),
			new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.DATE_BETWEEN, field: 'createDatetime', template: 'date', visible: false),
			new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'baseUser.id', template: 'id', visible: true),
			new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'isReview', template: 'invoice/review', visible: false),
			new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'billingProcess.id', template: 'id', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'currency.id', template: 'currency', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'contact.fields', template: 'invoice/ccf', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.IS_NOT_EMPTY, field: 'paymentMap', template: 'invoice/payments', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'total', template: 'range', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'balance', template: 'range', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'carriedBalance', template: 'range', visible: false),
            new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.LIKE, field: 'publicNumber', template: 'value', visible: false),
			new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.LIKE, field: 'u.company.description', template: 'company', visible: true),
        ],
        ORDER: [
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.EQ, field: 'changeStatus', template: 'order/changeStatus', visible: true),
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.EQ, field: 'u.id', template: 'id', visible: true),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.LIKE, field: 'u.userName', template: 'customer/login', visible: false),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'activeSince', template: 'date', visible: false),
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'activeUntil', template: 'date', visible: false),
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.EQ, field: 'contact.fields', template: 'order/ccf', visible: false),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'createDate', template: 'date', visible: true),
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'nextBillableDay', template: 'date', visible: false),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.STATUS, field: 'orderStatus', template: 'order/status', visible: false),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.STATUS, field: 'orderPeriod', template: 'order/period', visible: true),
            new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.EQ, field: 'userCodes.userCode.identifier', template: 'value', visible: false),
			new Filter(type: FilterType.ORDER, constraintType: FilterConstraint.LIKE, field: 'u.company.description', template: 'company', visible: true),
        ],
        PRODUCT: [
        	new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.EQ, field: 'contact.fields', template: 'product/ccf', visible: false),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.LIKE, field: 'internalNumber', template: 'product/internalNumber', visible: true),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.LIKE, field: 'description', template: 'product/description', visible: true),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.EQ, field: 'hasDecimals', template: 'product/decimals', visible: false),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.LIKE, field: 'glCode', template: 'product/glCode', visible: false),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'percentage', template: 'range', visible: false),
            new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'price.rate', template: 'range', visible: true),
			new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.EQ, field: 'global', template: 'isGlobal', visible: true),
			new Filter(type: FilterType.PRODUCT, constraintType: FilterConstraint.LIKE, field: 'u.company.description', template: 'company', visible: true),
        ],
        CUSTOMER: [
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.firstName', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.lastName', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.organizationName', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'contact.postalCode', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.phoneNumber', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.fields', template: 'customer/ccf', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'contact.email', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'createDatetime', template: 'date', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.STATUS, field: 'userStatus', template: 'customer/status', visible: true),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'userName', template: 'customer/login', visible: true),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'language.id', template: 'customer/language', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'currency.id', template: 'currency', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.IS_NOT_EMPTY, field: 'orders', template: 'customer/orders', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.IS_NOT_EMPTY, field: 'invoices', template: 'customer/invoices', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.IS_NOT_EMPTY, field: 'payments', template: 'customer/payments', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.IS_NOT_NULL, field: 'customer.parent', template: 'customer/child', visible: true),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'customer.isParent', template: 'trueOrFalse', visible: true),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'customer.partner.id', template: 'id', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'userCodes.userCode.identifier', template: 'value', visible: false),
            new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'deleted', template: 'customer/deleted', visible: true, integerValue: 0),
			new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'u.company.description', template: 'company', visible: true),
			new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.LIKE, field: 'accountTypeFields', template: 'customer/atf', visible: false),
			new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.DATE_BETWEEN, field: 'customer.nextInvoiceDate', template: 'date', visible: true),
		],
        PARTNER: [
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.LIKE, field: 'contact.firstName', template: 'value', visible: false),
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.LIKE, field: 'contact.lastName', template: 'value', visible: false),
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.LIKE, field: 'contact.email', template: 'value', visible: false),
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.STATUS, field: 'baseUser.userStatus', template: 'customer/status', visible: true),
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.LIKE, field: 'userName', template: 'customer/login', visible: true),
            new Filter(type: FilterType.PARTNER, constraintType: FilterConstraint.EQ, field: 'deleted', template: 'customer/deleted', visible: true, integerValue: 0),
        ],
        PAYMENT: [
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'u.id', template: 'id', visible: true),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.LIKE, field: 'u.userName', template: 'customer/login', visible: true),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.DATE_BETWEEN, field: 'createDatetime', template: 'date', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'isRefund', template: 'payment/refund', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'paymentMethod.id', template: 'payment/method', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'paymentResult.id', template: 'payment/result', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'i.invoiceEntity.id', template: 'id', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'amount', template: 'range', visible: true),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.NUMBER_BETWEEN, field: 'balance', template: 'range', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'contact.fields', template: 'payment/ccf', visible: false),
            new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.IN, field: 'paymentMethod.id', template: 'payment/credit', visible: true, stringValue: new PaymentMethodDAS().findAllValidMethods().collect {it.id}.join(',')),
			new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.LIKE, field: 'u.company.description', template: 'company', visible: true),
        ],
		BILLINGPROCESS: [
			new Filter(type: FilterType.BILLINGPROCESS, constraintType: FilterConstraint.DATE_BETWEEN, field: 'billingDate', template: 'date', visible: true),
            new Filter(type: FilterType.BILLINGPROCESS, constraintType: FilterConstraint.EQ, field: 'isReview', template: 'billing/review', visible: true)
		],
		LOGS:[
			new Filter(type: FilterType.LOGS, constraintType: FilterConstraint.DATE_BETWEEN, field: 'createDatetime', template: 'date', visible: true),
            new Filter(type: FilterType.LOGS, constraintType: FilterConstraint.LIKE, field: 'u.userName', template: 'customer/login', visible: false),
            new Filter(type: FilterType.LOGS, constraintType: FilterConstraint.EQ, field: 'u.id', template: 'id', visible: true),
            new Filter(type: FilterType.LOGS, constraintType: FilterConstraint.EQ, field: 'table.name', template: 'log/table', visible: true),
            new Filter(type: FilterType.LOGS, constraintType: FilterConstraint.EQ, field: 'foreignId', template: 'id', visible: true),
        ],

    ] as Map).asImmutable()

    /**
     * Returns a list of filters for the given type.
     *
     * @param type filter type
     * @return list of filters
     */
    static def Object getFilters(FilterType type) {
        def filters = []
        baseFilters.findAll{ it.key == FilterType.ALL.name() || it.key == type.name() }.each{ filters << it?.value }

        return filters.flatten().collect{new Filter((Filter)it)}
    }
}
