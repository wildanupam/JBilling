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

package com.sapienter.jbilling.server.payment;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import com.sapienter.jbilling.server.security.WSSecured;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


/**
 * @author Emil
 */
public class PaymentWS implements WSSecured, Serializable {

    @NotNull(message="validation.error.notnull")
    private Integer userId = null;

    private String method = null;
    private Integer invoiceIds[] = null;

    // refund specific fields
    private Integer paymentId = null; // this is the payment refunded / to refund
    private PaymentAuthorizationDTO authorization = null;

    //missing properties from PaymentDTO
    @NotEmpty(message="validation.error.notnull")
    @Digits(integer=12, fraction=10, message="validation.error.not.a.number")
    private String amount;
    @NotNull(message="validation.error.notnull")
    private Integer isRefund;
    //@NotNull(message="validation.error.apply.without.method")
    private Integer paymentMethodId;
    @NotNull(message="validation.error.notnull")
    private Date paymentDate;
    @NotNull(message="validation.error.notnull")
    private Integer currencyId;
    private int id;
    private Integer isPreauth;
    private Integer attempt;
    private String balance;
    private Date createDatetime;
    private Date updateDatetime;
    private int deleted;
    private Integer resultId;
    @Size(min = 0, max = 500, message = "validation.error.size,0,500")
    private String paymentNotes = null;
    private Integer paymentPeriod;

    @Valid
    private MetaFieldValueWS[] metaFields;

    private List<PaymentInformationWS> paymentInstruments = new ArrayList<PaymentInformationWS>(0);
    
    // those instruments that are not linked to any payment and user user uses them to make payments
    private List<PaymentInformationWS> userPaymentInstruments = new ArrayList<PaymentInformationWS>(0);
    
    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }
    
    public PaymentWS() {
        super();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer[] getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(Integer[] invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentAuthorizationDTO getAuthorizationId() {
        return authorization;
    }

    public void setAuthorization(PaymentAuthorizationDTO authorization) {
        this.authorization = authorization;
    }

    // required by CXF
    public void setAuthorizationId(PaymentAuthorizationDTO authorization) {
        this.authorization = authorization;
    }

    public String getAmount() {
        return amount;
    }

    public BigDecimal getAmountAsDecimal() {
        return Util.string2decimal(amount);
    }

    public void setAmountAsDecimal(BigDecimal amount) {
        setAmount(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = (amount != null ? amount.toString() : null);
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getMethodId() {
        return paymentMethodId;
    }

    public void setMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIsPreauth() {
        return isPreauth;
    }

    public void setIsPreauth(Integer isPreauth) {
        this.isPreauth = isPreauth;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public String getBalance() {
        return balance;
    }

    public BigDecimal getBalanceAsDecimal() {
        return Util.string2decimal(balance);
    }

    public void setBalanceAsDecimal(BigDecimal balance) {
        setBalance(balance);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = (balance != null ? balance.toString() : null);        
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public void setPaymentNotes(String paymentNotes){
        this.paymentNotes = paymentNotes;
    }
    
    public String getPaymentNotes(){
        return paymentNotes;
    }
    
    public void setPaymentPeriod(Integer paymentPeriod){
        this.paymentPeriod = paymentPeriod;
    }
    
    public Integer getPaymentPeriod(){
        return paymentPeriod;
    }

    public MetaFieldValueWS[] getMetaFields() {
        return metaFields;
    }

    public void setMetaFields(MetaFieldValueWS[] metaFields) {
        this.metaFields = metaFields;
    }

    public List<PaymentInformationWS> getPaymentInstruments() {
		return paymentInstruments;
	}

	public void setPaymentInstruments(List<PaymentInformationWS> paymentInstruments) {
		this.paymentInstruments = paymentInstruments;
	}
	
	public List<PaymentInformationWS> getUserPaymentInstruments() {
		return userPaymentInstruments;
	}

	public void setUserPaymentInstruments(List<PaymentInformationWS> paymentInstruments) {
		this.userPaymentInstruments = paymentInstruments;
	}

	/**
     * Unsupported, web-service security enforced using {@link #getOwningUserId()}
     * @return null
     */
    public Integer getOwningEntityId() {
        return null;
    }

    public Integer getOwningUserId() {
        return getUserId();
    }

    @Override
    public String toString() {
        return "PaymentWS{"
               + "id=" + id
               + ", userId=" + userId
               + ", paymentMethodId=" + paymentMethodId
               + ", method='" + method + '\''
               + ", amount='" + amount + '\''
               + ", balance='" + balance + '\''
               + ", isRefund=" + isRefund
               + ", isPreauth=" + isPreauth
               + ", paymentDate=" + paymentDate
               + ", deleted=" + deleted
               + ", paymentId=" + paymentId
               + ", currencyId=" + currencyId
               + '}';
    }
}
