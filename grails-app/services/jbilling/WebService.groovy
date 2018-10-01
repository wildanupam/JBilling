package jbilling

import com.sapienter.jbilling.common.*
import com.sapienter.jbilling.server.item.*
import com.sapienter.jbilling.server.item.db.*
import com.sapienter.jbilling.server.util.db.*
import com.sapienter.jbilling.server.payment.*
import com.sapienter.jbilling.server.util.*
import grails.plugin.springsecurity.SpringSecurityService

import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

import com.sapienter.jbilling.server.payment.*

import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.SQLException
import java.util.ArrayList
import java.util.Arrays
import java.util.Calendar
import java.util.Collection
import java.util.Collections
import java.util.Comparator
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.Iterator
import java.util.LinkedHashSet
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Set

import javax.naming.NamingException
import javax.sql.rowset.CachedRowSet

import com.sapienter.jbilling.common.*
import com.sapienter.jbilling.server.item.*
import com.sapienter.jbilling.server.item.db.*
import com.sapienter.jbilling.server.util.credentials.EmailResetPasswordService
import com.sapienter.jbilling.server.util.db.*
import com.sapienter.jbilling.server.order.*
import com.sapienter.jbilling.server.order.db.*

import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.hibernate.LockMode
import org.joda.time.DateMidnight
import org.joda.time.DateTime
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import com.sapienter.jbilling.CustomerNoteDAS
import com.sapienter.jbilling.client.authentication.CompanyUserDetails
import com.sapienter.jbilling.common.Util
import com.sapienter.jbilling.server.security.JBCrypto
import com.sapienter.jbilling.server.account.AccountInformationTypeBL
import com.sapienter.jbilling.server.account.AccountTypeBL
import com.sapienter.jbilling.server.discount.DiscountBL
import com.sapienter.jbilling.server.discount.DiscountLineWS
import com.sapienter.jbilling.server.discount.DiscountWS
import com.sapienter.jbilling.server.discount.db.DiscountDAS
import com.sapienter.jbilling.server.discount.db.DiscountDTO
import com.sapienter.jbilling.server.discount.db.DiscountLineDTO
import com.sapienter.jbilling.server.entity.InvoiceLineDTO
import com.sapienter.jbilling.server.invoice.IInvoiceSessionBean
import com.sapienter.jbilling.server.invoice.InvoiceBL
import com.sapienter.jbilling.server.invoice.InvoiceWS
import com.sapienter.jbilling.server.invoice.NewInvoiceContext
import com.sapienter.jbilling.server.invoice.db.InvoiceDAS
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO
import com.sapienter.jbilling.server.invoice.db.InvoiceLineTypeDTO
import com.sapienter.jbilling.server.invoice.db.InvoiceStatusDAS
import com.sapienter.jbilling.server.invoice.db.InvoiceStatusDTO
import com.sapienter.jbilling.server.item.AssetBL
import com.sapienter.jbilling.server.item.AssetSearchResult
import com.sapienter.jbilling.server.item.AssetStatusBL
import com.sapienter.jbilling.server.item.AssetStatusDTOEx
import com.sapienter.jbilling.server.item.AssetTransitionBL
import com.sapienter.jbilling.server.item.AssetTransitionDTOEx
import com.sapienter.jbilling.server.item.AssetWS
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.IItemSessionBean
import com.sapienter.jbilling.server.item.ItemBL
import com.sapienter.jbilling.server.item.ItemDTOEx
import com.sapienter.jbilling.server.item.ItemDependencyDTOEx
import com.sapienter.jbilling.server.item.ItemDependencyType
import com.sapienter.jbilling.server.item.ItemTypeBL
import com.sapienter.jbilling.server.item.ItemTypeWS
import com.sapienter.jbilling.server.item.PricingField
import com.sapienter.jbilling.server.item.batch.AssetImportConstants
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.IItemSessionBean
import com.sapienter.jbilling.server.item.ItemBL
import com.sapienter.jbilling.server.item.ItemDTOEx
import com.sapienter.jbilling.server.item.ItemTypeBL
import com.sapienter.jbilling.server.item.ItemTypeWS
import com.sapienter.jbilling.server.item.PricingField
import com.sapienter.jbilling.server.item.db.ItemDAS
import com.sapienter.jbilling.server.item.db.ItemDTO
import com.sapienter.jbilling.server.item.db.ItemTypeDAS
import com.sapienter.jbilling.server.item.db.ItemTypeDTO
import com.sapienter.jbilling.server.metafields.EntityType
import com.sapienter.jbilling.server.metafields.MetaFieldBL
import com.sapienter.jbilling.server.metafields.MetaFieldGroupBL
import com.sapienter.jbilling.server.metafields.MetaFieldGroupWS
import com.sapienter.jbilling.server.metafields.MetaFieldHelper
import com.sapienter.jbilling.server.metafields.MetaFieldType
import com.sapienter.jbilling.server.metafields.MetaFieldValueWS
import com.sapienter.jbilling.server.metafields.MetaFieldWS
import com.sapienter.jbilling.server.metafields.DataType
import com.sapienter.jbilling.server.metafields.db.MetaField
import com.sapienter.jbilling.server.metafields.db.MetaFieldDAS
import com.sapienter.jbilling.server.metafields.db.MetaFieldGroup
import com.sapienter.jbilling.server.metafields.MetaFieldType
import com.sapienter.jbilling.server.notification.INotificationSessionBean
import com.sapienter.jbilling.server.notification.MessageDTO
import com.sapienter.jbilling.server.notification.NotificationBL
import com.sapienter.jbilling.server.order.IOrderSessionBean
import com.sapienter.jbilling.server.order.OrderBL
import com.sapienter.jbilling.server.order.OrderChangeBL
import com.sapienter.jbilling.server.order.OrderChangeStatusBL
import com.sapienter.jbilling.server.order.OrderChangeStatusWS
import com.sapienter.jbilling.server.order.OrderChangeTypeBL
import com.sapienter.jbilling.server.order.OrderChangeTypeWS
import com.sapienter.jbilling.server.order.OrderChangeWS
import com.sapienter.jbilling.server.order.OrderHelper
import com.sapienter.jbilling.server.order.OrderLineBL
import com.sapienter.jbilling.server.order.OrderLineWS
import com.sapienter.jbilling.server.order.OrderPeriodWS
import com.sapienter.jbilling.server.order.OrderProcessWS
import com.sapienter.jbilling.server.order.OrderStatusFlag
import com.sapienter.jbilling.server.order.OrderStatusWS
import com.sapienter.jbilling.server.order.OrderWS
import com.sapienter.jbilling.server.order.SwapMethod
import com.sapienter.jbilling.server.order.TimePeriod
import com.sapienter.jbilling.server.order.db.OrderChangeDAS
import com.sapienter.jbilling.server.order.db.OrderChangeDTO
import com.sapienter.jbilling.server.order.db.OrderChangeStatusDAS
import com.sapienter.jbilling.server.order.db.OrderChangeStatusDTO
import com.sapienter.jbilling.server.order.db.OrderChangeTypeDAS
import com.sapienter.jbilling.server.order.db.OrderChangeTypeDTO
import com.sapienter.jbilling.server.order.db.OrderDAS
import com.sapienter.jbilling.server.order.db.OrderDTO
import com.sapienter.jbilling.server.order.db.OrderLineDTO
import com.sapienter.jbilling.server.order.db.OrderPeriodDAS
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO
import com.sapienter.jbilling.server.order.db.OrderProcessDAS
import com.sapienter.jbilling.server.order.db.OrderProcessDTO
import com.sapienter.jbilling.server.order.db.OrderStatusBL
import com.sapienter.jbilling.server.order.db.OrderStatusDAS
import com.sapienter.jbilling.server.order.event.OrderPreAuthorizedEvent
import com.sapienter.jbilling.server.order.validator.IsNotEmptyOrDeletedValidator
import com.sapienter.jbilling.server.order.validator.OrderHierarchyValidator
import com.sapienter.jbilling.server.payment.db.PaymentDAS
import com.sapienter.jbilling.server.payment.db.PaymentDTO
import com.sapienter.jbilling.server.payment.db.PaymentInformationDAS
import com.sapienter.jbilling.server.payment.db.PaymentInformationDTO
import com.sapienter.jbilling.server.payment.db.PaymentInvoiceMapDTO
import com.sapienter.jbilling.server.payment.db.PaymentMethodDAS
import com.sapienter.jbilling.server.payment.db.PaymentMethodTemplateDAS
import com.sapienter.jbilling.server.payment.db.PaymentMethodTemplateDTO
import com.sapienter.jbilling.server.payment.db.PaymentMethodTypeDAS
import com.sapienter.jbilling.server.payment.db.PaymentMethodTypeDTO
import com.sapienter.jbilling.server.pluggableTask.TaskException
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDAS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDTO
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryWS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeDAS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeDTO
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeWS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskWS
import com.sapienter.jbilling.server.process.AgeingBL
import com.sapienter.jbilling.server.process.AgeingDTOEx
import com.sapienter.jbilling.server.process.AgeingWS
import com.sapienter.jbilling.server.process.BillingProcessBL
import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS
import com.sapienter.jbilling.server.process.BillingProcessDTOEx
import com.sapienter.jbilling.server.process.BillingProcessWS
import com.sapienter.jbilling.server.process.ConfigurationBL
import com.sapienter.jbilling.server.process.IBillingProcessSessionBean
import com.sapienter.jbilling.server.process.ProcessStatusWS
import com.sapienter.jbilling.server.process.db.BillingProcessConfigurationDAS
import com.sapienter.jbilling.server.process.db.BillingProcessConfigurationDTO
import com.sapienter.jbilling.server.process.db.BillingProcessDTO
import com.sapienter.jbilling.server.process.db.ProratingType
import com.sapienter.jbilling.server.process.task.IScheduledTask
import com.sapienter.jbilling.server.system.event.EventManager
import com.sapienter.jbilling.server.user.AccountInformationTypeWS
import com.sapienter.jbilling.server.user.AccountTypeWS
import com.sapienter.jbilling.server.user.CompanyWS
import com.sapienter.jbilling.server.user.ContactBL
import com.sapienter.jbilling.server.user.ContactDTOEx
import com.sapienter.jbilling.server.user.ContactWS
import com.sapienter.jbilling.server.user.CreateResponseWS
import com.sapienter.jbilling.server.user.CustomerNoteWS
import com.sapienter.jbilling.server.user.EntityBL
import com.sapienter.jbilling.server.user.IUserSessionBean
import com.sapienter.jbilling.server.user.UserBL
import com.sapienter.jbilling.server.user.UserCodeWS
import com.sapienter.jbilling.server.user.UserDTOEx
import com.sapienter.jbilling.server.user.UserTransitionResponseWS
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.ValidatePurchaseWS
import com.sapienter.jbilling.server.user.db.AccountInformationTypeDAS
import com.sapienter.jbilling.server.user.db.AccountInformationTypeDTO
import com.sapienter.jbilling.server.user.db.AccountTypeDAS
import com.sapienter.jbilling.server.user.db.AccountTypeDTO
import com.sapienter.jbilling.server.user.db.AccountTypePriceBL
import com.sapienter.jbilling.server.user.db.AccountTypePriceDTO
import com.sapienter.jbilling.server.user.db.CompanyDAS
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.db.CustomerDAS
import com.sapienter.jbilling.server.user.db.CustomerDTO
import com.sapienter.jbilling.server.user.db.CustomerNoteDTO
import com.sapienter.jbilling.server.user.db.MainSubscriptionDTO
import com.sapienter.jbilling.server.user.db.UserCodeDTO
import com.sapienter.jbilling.server.user.db.UserCodeObjectType
import com.sapienter.jbilling.server.user.db.UserDAS
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.user.partner.CommissionProcessConfigurationBL
import com.sapienter.jbilling.server.user.partner.CommissionProcessConfigurationWS
import com.sapienter.jbilling.server.user.partner.CommissionProcessRunWS
import com.sapienter.jbilling.server.user.partner.CommissionWS
import com.sapienter.jbilling.server.user.partner.PartnerBL
import com.sapienter.jbilling.server.user.partner.PartnerCommissionType
import com.sapienter.jbilling.server.user.partner.PartnerWS
import com.sapienter.jbilling.server.user.partner.db.CommissionDAS
import com.sapienter.jbilling.server.user.partner.db.CommissionDTO
import com.sapienter.jbilling.server.user.partner.db.CommissionProcessConfigurationDTO
import com.sapienter.jbilling.server.user.partner.db.CommissionProcessRunDAS
import com.sapienter.jbilling.server.user.partner.db.CommissionProcessRunDTO
import com.sapienter.jbilling.server.user.partner.db.PartnerDAS
import com.sapienter.jbilling.server.user.partner.db.PartnerDTO
import com.sapienter.jbilling.server.util.audit.EventLogger
import com.sapienter.jbilling.server.util.csv.CsvExporter
import com.sapienter.jbilling.server.util.csv.Exportable
import com.sapienter.jbilling.server.util.csv.ExportableMap
import com.sapienter.jbilling.server.util.db.CurrencyDAS
import com.sapienter.jbilling.server.util.db.CurrencyDTO
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDAS
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDTO
import com.sapienter.jbilling.server.util.db.JbillingTable
import com.sapienter.jbilling.server.util.db.JbillingTableDAS
import com.sapienter.jbilling.server.util.db.LanguageDAS
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.util.db.PreferenceDTO
import com.sapienter.jbilling.server.util.db.PreferenceTypeDAS
import com.sapienter.jbilling.server.util.db.PreferenceTypeDTO
import com.sapienter.jbilling.server.util.search.SearchCriteria
import com.sapienter.jbilling.server.util.search.SearchResultString
import com.sapienter.jbilling.tools.JArrays
import javax.jws.WebMethod
import javax.jws.WebResult
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean
/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

/**
 * Created by marcomanzi on 3/18/14.
 * This services will be used from the migration tools.
 */
 
 
 

public class WebService extends WebServicesSessionSpringBean implements IWebServicesSessionBean{



    static transactional = true

    static expose = ['cxfjax']

	@Override
	@WebResult
	@WebMethod
	public Integer getCallerId() {
		// TODO Auto-generated method stub
		return super.getCallerId()
	}

	@Override
@WebResult
@WebMethod
	public Integer getCallerCompanyId() {
		// TODO Auto-generated method stub
		return super.getCallerCompanyId()
	}

	@Override
@WebResult
@WebMethod
	public Integer getCallerLanguageId() {
		// TODO Auto-generated method stub
		return super.getCallerLanguageId()
	}

	@Override
@WebResult
@WebMethod
	public Integer getCallerCurrencyId() {
		// TODO Auto-generated method stub
		return super.getCallerCurrencyId()
	}

	@Override
@WebResult
@WebMethod
	public InvoiceWS getInvoiceWS(Integer invoiceId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getInvoiceWS(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public InvoiceWS[] getAllInvoicesForUser(Integer userId) {
		// TODO Auto-generated method stub
		return super.getAllInvoicesForUser(userId)
	}

	

	@Override
@WebResult
@WebMethod
	public boolean notifyInvoiceByEmail(Integer invoiceId) {
		// TODO Auto-generated method stub
		return super.notifyInvoiceByEmail(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public boolean notifyPaymentByEmail(Integer paymentId) {
		// TODO Auto-generated method stub
		return super.notifyPaymentByEmail(paymentId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getAllInvoices(Integer userId) {
		// TODO Auto-generated method stub
		return super.getAllInvoices(userId)
	}

	@Override
@WebResult
@WebMethod
	public InvoiceWS getLatestInvoice(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLatestInvoice(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastInvoices(Integer userId, Integer number) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastInvoices(userId, number)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getInvoicesByDate(String since, String until) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getInvoicesByDate(since, until)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getUserInvoicesByDate(Integer userId, String since, String until) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserInvoicesByDate(userId, since, until)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getUnpaidInvoices(Integer userId) {
		// TODO Auto-generated method stub
		return super.getUnpaidInvoices(userId)
	}

	@Override
@WebResult
@WebMethod
	public InvoiceWS[] getUserInvoicesPage(Integer userId, Integer limit, Integer offset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserInvoicesPage(userId, limit, offset)
	}

	@Override
@WebResult
@WebMethod
	public byte[] getPaperInvoicePDF(Integer invoiceId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getPaperInvoicePDF(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public void removePaymentLink(Integer invoiceId, Integer paymentId) {
		// TODO Auto-generated method stub
		super.removePaymentLink(invoiceId, paymentId)
	}

	@Override
@WebResult
@WebMethod
	public void createPaymentLink(Integer invoiceId, Integer paymentId) {
		// TODO Auto-generated method stub
		super.createPaymentLink(invoiceId, paymentId)
	}

	@Override
@WebResult
@WebMethod
	public void removeAllPaymentLinks(Integer paymentId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.removeAllPaymentLinks(paymentId)
	}

	@Override
@WebResult
@WebMethod
	public void deleteInvoice(Integer invoiceId) {
		// TODO Auto-generated method stub
		super.deleteInvoice(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public Integer saveLegacyInvoice(InvoiceWS invoiceWS) {
		// TODO Auto-generated method stub
		return super.saveLegacyInvoice(invoiceWS)
	}

	@Override
@WebResult
@WebMethod
	public Integer saveLegacyPayment(PaymentWS paymentWS) {
		// TODO Auto-generated method stub
		return super.saveLegacyPayment(paymentWS)
	}

	@Override
@WebResult
@WebMethod
	public Integer saveLegacyOrder(OrderWS orderWS) {
		// TODO Auto-generated method stub
		return super.saveLegacyOrder(orderWS)
	}

	@Override
@WebResult
@WebMethod
	public void deleteItem(Integer itemId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteItem(itemId)
	}

	@Override
@WebResult
@WebMethod
	public void deleteItemCategory(Integer itemCategoryId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteItemCategory(itemCategoryId)
	}

	@Override
@WebResult
@WebMethod
	public ItemTypeWS[] getAllItemCategoriesByEntityId(Integer entityId) {
		// TODO Auto-generated method stub
		return super.getAllItemCategoriesByEntityId(entityId)
	}

	@Override
@WebResult
@WebMethod
	public ItemDTOEx[] getAllItemsByEntityId(Integer entityId) {
		// TODO Auto-generated method stub
		return super.getAllItemsByEntityId(entityId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] createInvoice(Integer userId, boolean onlyRecurring) {
		// TODO Auto-generated method stub
		return super.createInvoice(userId, onlyRecurring)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] createInvoiceWithDate(Integer userId, Date billingDate, Integer dueDatePeriodId,
			Integer dueDatePeriodValue, boolean onlyRecurring) {
		// TODO Auto-generated method stub
		return super.createInvoiceWithDate(userId, billingDate, dueDatePeriodId, dueDatePeriodValue, onlyRecurring)
	}

	@Override
@WebResult
@WebMethod
	public Integer applyOrderToInvoice(Integer orderId, InvoiceWS invoiceWs) {
		// TODO Auto-generated method stub
		return super.applyOrderToInvoice(orderId, invoiceWs)
	}

	@Override
@WebResult
@WebMethod
	public Integer createInvoiceFromOrder(Integer orderId, Integer invoiceId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createInvoiceFromOrder(orderId, invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUser(UserWS newUser) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUser(newUser)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUserWithCompanyId(UserWS newUser, Integer companyId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUserWithCompanyId(newUser, companyId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createReseller(UserWS newUser, Integer parentId) {
		// TODO Auto-generated method stub
		return super.createReseller(newUser, parentId)
	}

	@Override
@WebResult
@WebMethod
	public void deleteUser(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteUser(userId)
	}

	@Override
@WebResult
@WebMethod
	public boolean userExistsWithName(String userName) {
		// TODO Auto-generated method stub
		return super.userExistsWithName(userName)
	}

	@Override
@WebResult
@WebMethod
	public boolean userExistsWithId(Integer userId) {
		// TODO Auto-generated method stub
		return super.userExistsWithId(userId)
	}

	@Override
@WebResult
@WebMethod
	public void updateUserContact(Integer userId, ContactWS contact) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateUserContact(userId, contact)
	}

	@Override
@WebResult
@WebMethod
	public void updateUser(UserWS user) {
		// TODO Auto-generated method stub
		super.updateUser(user)
	}

	@Override
@WebResult
@WebMethod
	public void updateUserWithCompanyId(UserWS user, Integer entityId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateUserWithCompanyId(user, entityId)
	}

	@Override
@WebResult
@WebMethod
	public UserWS getUserWS(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserWS(userId)
	}

	@Override
@WebResult
@WebMethod
	public ContactWS[] getUserContactsWS(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserContactsWS(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer getUserId(String username) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserId(username)
	}

	@Override
@WebResult
@WebMethod
	public Integer getUserIdByEmail(String email) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserIdByEmail(email)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getUsersInStatus(Integer statusId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUsersInStatus(statusId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getUsersNotInStatus(Integer statusId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUsersNotInStatus(statusId)
	}



	@Override
@WebResult
@WebMethod
	public CreateResponseWS create(UserWS user, OrderWS order, OrderChangeWS[] orderChanges)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.create(user, order, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public PartnerWS getPartner(Integer partnerId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getPartner(partnerId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createPartner(UserWS newUser, PartnerWS partner) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createPartner(newUser, partner)
	}

	@Override
@WebResult
@WebMethod
	public void updatePartner(UserWS user, PartnerWS partner) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updatePartner(user, partner)
	}

	@Override
@WebResult
@WebMethod
	public void deletePartner(Integer partnerId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deletePartner(partnerId)
	}

	@Override
@WebResult
@WebMethod
	public UserCodeWS[] getUserCodesForUser(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserCodesForUser(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUserCode(UserCodeWS userCode) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUserCode(userCode)
	}

	@Override
@WebResult
@WebMethod
	public void updateUserCode(UserCodeWS userCode) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateUserCode(userCode)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getCustomersByUserCode(String userCode) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getCustomersByUserCode(userCode)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getOrdersByUserCode(String userCode) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrdersByUserCode(userCode)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getCustomersLinkedToUser(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getCustomersLinkedToUser(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getOrdersLinkedToUser(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrdersLinkedToUser(userId)
	}

	@Override
@WebResult
@WebMethod
	public ItemTypeWS[] getItemCategoriesByPartner(String partner, boolean parentCategoriesOnly) {
		// TODO Auto-generated method stub
		return super.getItemCategoriesByPartner(partner, parentCategoriesOnly)
	}

	@Override
@WebResult
@WebMethod
	public ItemTypeWS[] getChildItemCategories(Integer itemTypeId) {
		// TODO Auto-generated method stub
		return super.getChildItemCategories(itemTypeId)
	}

	@Override
@WebResult
@WebMethod
	public ItemDTOEx[] getAddonItems(Integer itemId) {
		// TODO Auto-generated method stub
		return super.getAddonItems(itemId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.payInvoice(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order, OrderChangeWS[] orderChanges)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createOrderPreAuthorize(order, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS[] getLinkedOrders(Integer primaryOrderId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLinkedOrders(primaryOrderId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createOrder(OrderWS order, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createOrder(order, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateOrder(OrderWS order, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUpdateOrder(order, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS rateOrder(OrderWS order, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.rateOrder(order, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS[] rateOrders(OrderWS[] orders, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.rateOrders(orders, orderChanges)
	}



	@Override
@WebResult
@WebMethod
	public void updateItem(ItemDTOEx item, boolean isPlan) {
		// TODO Auto-generated method stub
		super.updateItem(item, isPlan)
	}

	@Override
@WebResult
@WebMethod
	public Integer createOrderAndInvoice(OrderWS order, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createOrderAndInvoice(order, orderChanges)
	}



	@Override
@WebResult
@WebMethod
	public void updateOrder(OrderWS order, OrderChangeWS[] orderChanges, Integer entityId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateOrder(order, orderChanges, entityId)
	}

	@Override
@WebResult
@WebMethod
	public void updateOrders(OrderWS[] orders, OrderChangeWS[] orderChanges) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateOrders(orders, orderChanges)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS getOrder(Integer orderId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrder(orderId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getOrderByPeriod(Integer userId, Integer periodId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrderByPeriod(userId, periodId)
	}

	@Override
@WebResult
@WebMethod
	public OrderLineWS getOrderLine(Integer orderLineId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrderLine(orderLineId)
	}

	@Override
@WebResult
@WebMethod
	public void updateOrderLine(OrderLineWS line) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateOrderLine(line)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS getLatestOrder(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLatestOrder(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastOrders(Integer userId, Integer number) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastOrders(userId, number)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS[] getUserOrdersPage(Integer user, Integer limit, Integer offset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserOrdersPage(user, limit, offset)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastOrdersPage(Integer userId, Integer limit, Integer offset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastOrdersPage(userId, limit, offset)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getOrdersByDate(Integer userId, Date since, Date until) {
		// TODO Auto-generated method stub
		return super.getOrdersByDate(userId, since, until)
	}

	@Override
@WebResult
@WebMethod
	public String deleteOrder(Integer id) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deleteOrder(id)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS getCurrentOrder(Integer userId, Date date) {
		// TODO Auto-generated method stub
		return super.getCurrentOrder(userId, date)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS updateCurrentOrder(Integer userId, OrderLineWS[] lines, String pricing, Date eventDate,
			String eventDescription) {
		// TODO Auto-generated method stub
		return super.updateCurrentOrder(userId, lines, pricing, eventDate, eventDescription)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS[] getUserSubscriptions(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserSubscriptions(userId)
	}

	@Override
@WebResult
@WebMethod
	public boolean updateOrderPeriods(OrderPeriodWS[] orderPeriods) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.updateOrderPeriods(orderPeriods)
	}

	@Override
@WebResult
@WebMethod
	public boolean updateOrCreateOrderPeriod(OrderPeriodWS orderPeriod) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.updateOrCreateOrderPeriod(orderPeriod)
	}

	@Override
@WebResult
@WebMethod
	public boolean deleteOrderPeriod(Integer periodId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deleteOrderPeriod(periodId)
	}

	@Override
@WebResult
@WebMethod
	public boolean updateAccountType(AccountTypeWS accountType) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.updateAccountType(accountType)
	}

	@Override
@WebResult
@WebMethod
	public Integer createAccountType(AccountTypeWS accountType) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createAccountType(accountType)
	}

	@Override
@WebResult
@WebMethod
	public boolean deleteAccountType(Integer accountTypeId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deleteAccountType(accountTypeId)
	}

	@Override
@WebResult
@WebMethod
	public AccountTypeWS getAccountType(Integer accountTypeId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getAccountType(accountTypeId)
	}

	@Override
@WebResult
@WebMethod
	public AccountTypeWS[] getAllAccountTypes() throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getAllAccountTypes()
	}

	@Override
@WebResult
@WebMethod
	public AccountInformationTypeWS[] getInformationTypesForAccountType(Integer accountTypeId) {
		// TODO Auto-generated method stub
		return super.getInformationTypesForAccountType(accountTypeId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createAccountInformationType(AccountInformationTypeWS accountInformationType) {
		// TODO Auto-generated method stub
		return super.createAccountInformationType(accountInformationType)
	}

	@Override
@WebResult
@WebMethod
	public void updateAccountInformationType(AccountInformationTypeWS accountInformationType) {
		// TODO Auto-generated method stub
		super.updateAccountInformationType(accountInformationType)
	}

	@Override
@WebResult
@WebMethod
	public boolean deleteAccountInformationType(Integer accountInformationTypeId) {
		// TODO Auto-generated method stub
		return super.deleteAccountInformationType(accountInformationTypeId)
	}

	@Override
@WebResult
@WebMethod
	public AccountInformationTypeWS getAccountInformationType(Integer accountInformationTypeId) {
		// TODO Auto-generated method stub
		return super.getAccountInformationType(accountInformationTypeId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createPayment(PaymentWS payment) {
		// TODO Auto-generated method stub
		return super.createPayment(payment)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] createPayments(PaymentWS[] payments) {
		// TODO Auto-generated method stub
		return super.createPayments(payments)
	}

	@Override
@WebResult
@WebMethod
	public void updatePayment(PaymentWS payment) {
		// TODO Auto-generated method stub
		super.updatePayment(payment)
	}

	@Override
@WebResult
@WebMethod
	public void deletePayment(Integer paymentId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deletePayment(paymentId)
	}

	@Override
@WebResult
@WebMethod
	public Integer applyPayment(PaymentWS payment, Integer invoiceId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.applyPayment(payment, invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentAuthorizationDTOEx processPayment(PaymentWS payment, Integer invoiceId) {
		// TODO Auto-generated method stub
		return super.processPayment(payment, invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentAuthorizationDTOEx[] processPayments(PaymentWS[] payments, Integer invoiceId) {
		// TODO Auto-generated method stub
		return super.processPayments(payments, invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentWS getPayment(Integer paymentId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getPayment(paymentId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentWS getLatestPayment(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLatestPayment(userId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastPayments(Integer userId, Integer number) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastPayments(userId, number)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastPaymentsPage(Integer userId, Integer limit, Integer offset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastPaymentsPage(userId, limit, offset)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getPaymentsByDate(Integer userId, Date since, Date until) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getPaymentsByDate(userId, since, until)
	}



	@Override
@WebResult
@WebMethod
	public PaymentWS getUserPaymentInstrument(Integer userId, Integer entityId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserPaymentInstrument(userId, entityId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentWS[] getUserPaymentsPage(Integer userId, Integer limit, Integer offset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserPaymentsPage(userId, limit, offset)
	}

	@Override
@WebResult
@WebMethod
	public BigDecimal getTotalRevenueByUser(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getTotalRevenueByUser(userId)
	}



	@Override
@WebResult
@WebMethod
	public Integer createItem(ItemDTOEx item, boolean isPlan) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createItem(item, isPlan)
	}

	@Override
@WebResult
@WebMethod
	public ItemDTOEx[] getAllItems() throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getAllItems()
	}

	@Override
@WebResult
@WebMethod
	public UserTransitionResponseWS[] getUserTransitions(Date from, Date to) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserTransitions(from, to)
	}

	@Override
@WebResult
@WebMethod
	public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getUserTransitionsAfterId(id)
	}

	@Override
@WebResult
@WebMethod
	public ItemDTOEx getItem(Integer itemId, Integer userId, String pricing) {
		// TODO Auto-generated method stub
		return super.getItem(itemId, userId, pricing)
	}

	@Override
@WebResult
@WebMethod
	public DiscountWS getDiscountWS(Integer discountId) {
		// TODO Auto-generated method stub
		return super.getDiscountWS(discountId)
	}

	@Override
@WebResult
@WebMethod
	public void deleteDiscount(Integer discountId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteDiscount(discountId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createItemCategory(ItemTypeWS itemType) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createItemCategory(itemType)
	}

	@Override
@WebResult
@WebMethod
	public void updateItemCategory(ItemTypeWS itemType) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateItemCategory(itemType)
	}



	@Override
@WebResult
@WebMethod
	public InvoiceWS getLatestInvoiceByItemType(Integer userId, Integer itemTypeId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLatestInvoiceByItemType(userId, itemTypeId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastInvoicesByItemType(Integer userId, Integer itemTypeId, Integer number)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastInvoicesByItemType(userId, itemTypeId, number)
	}

	@Override
@WebResult
@WebMethod
	public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLatestOrderByItemType(userId, itemTypeId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getLastOrdersByItemType(Integer userId, Integer itemTypeId, Integer number)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastOrdersByItemType(userId, itemTypeId, number)
	}

	@Override
@WebResult
@WebMethod
	public String isUserSubscribedTo(Integer userId, Integer itemId) {
		// TODO Auto-generated method stub
		return super.isUserSubscribedTo(userId, itemId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getUserItemsByCategory(Integer userId, Integer categoryId) {
		// TODO Auto-generated method stub
		return super.getUserItemsByCategory(userId, categoryId)
	}

	@Override
@WebResult
@WebMethod
	public ItemDTOEx[] getItemByCategory(Integer itemTypeId) {
		// TODO Auto-generated method stub
		return super.getItemByCategory(itemTypeId)
	}

	@Override
@WebResult
@WebMethod
	public ItemTypeWS getItemCategoryById(Integer id) {
		// TODO Auto-generated method stub
		return super.getItemCategoryById(id)
	}

	@Override
@WebResult
@WebMethod
	public ItemTypeWS[] getAllItemCategories() {
		// TODO Auto-generated method stub
		return super.getAllItemCategories()
	}

	@Override
@WebResult
@WebMethod
	public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId, String fields) {
		// TODO Auto-generated method stub
		return super.validatePurchase(userId, itemId, fields)
	}

	@Override
@WebResult
@WebMethod
	public ValidatePurchaseWS validateMultiPurchase(Integer userId, Integer[] itemIds, String[] fields) {
		// TODO Auto-generated method stub
		return super.validateMultiPurchase(userId, itemIds, fields)
	}

	@Override
@WebResult
@WebMethod
	public Integer getItemID(String productCode) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getItemID(productCode)
	}

	@Override
@WebResult
@WebMethod
	public Integer getAuthPaymentType(Integer userId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getAuthPaymentType(userId)
	}

	@Override
@WebResult
@WebMethod
	public void setAuthPaymentType(Integer userId, Integer autoPaymentType, boolean use) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.setAuthPaymentType(userId, autoPaymentType, use)
	}

	@Override
@WebResult
@WebMethod
	public AgeingWS[] getAgeingConfiguration(Integer languageId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getAgeingConfiguration(languageId)
	}

	@Override
@WebResult
@WebMethod
	public void saveAgeingConfiguration(AgeingWS[] steps, Integer languageId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.saveAgeingConfiguration(steps, languageId)
	}

	@Override
@WebResult
@WebMethod
	public boolean isBillingRunning(Integer entityId) {
		// TODO Auto-generated method stub
		return super.isBillingRunning(entityId)
	}

	@Override
@WebResult
@WebMethod
	public void triggerBillingAsync(Date runDate) {
		// TODO Auto-generated method stub
		super.triggerBillingAsync(runDate)
	}

	@Override
@WebResult
@WebMethod
	public void triggerCollectionsAsync(Date runDate) {
		// TODO Auto-generated method stub
		super.triggerCollectionsAsync(runDate)
	}

	@Override
@WebResult
@WebMethod
	public boolean triggerBilling(Date runDate) {
		// TODO Auto-generated method stub
		return super.triggerBilling(runDate)
	}

	@Override
@WebResult
@WebMethod
	public void triggerAgeing(Date runDate) {
		// TODO Auto-generated method stub
		super.triggerAgeing(runDate)
	}

	@Override
@WebResult
@WebMethod
	public boolean isAgeingProcessRunning() {
		// TODO Auto-generated method stub
		return super.isAgeingProcessRunning()
	}

	@Override
@WebResult
@WebMethod
	public ProcessStatusWS getBillingProcessStatus() {
		// TODO Auto-generated method stub
		return super.getBillingProcessStatus()
	}

	@Override
@WebResult
@WebMethod
	public ProcessStatusWS getAgeingProcessStatus() {
		// TODO Auto-generated method stub
		return super.getAgeingProcessStatus()
	}

	@Override
@WebResult
@WebMethod
	public BillingProcessConfigurationWS getBillingProcessConfiguration() throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getBillingProcessConfiguration()
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateBillingProcessConfiguration(BillingProcessConfigurationWS ws)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUpdateBillingProcessConfiguration(ws)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateCommissionProcessConfiguration(CommissionProcessConfigurationWS ws)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUpdateCommissionProcessConfiguration(ws)
	}

	@Override
@WebResult
@WebMethod
	public void calculatePartnerCommissions() {
		// TODO Auto-generated method stub
		super.calculatePartnerCommissions()
	}

	@Override
@WebResult
@WebMethod
	public void calculatePartnerCommissionsAsync() {
		// TODO Auto-generated method stub
		super.calculatePartnerCommissionsAsync()
	}

	@Override
@WebResult
@WebMethod
	public boolean isPartnerCommissionRunning() {
		// TODO Auto-generated method stub
		return super.isPartnerCommissionRunning()
	}

	@Override
@WebResult
@WebMethod
	public CommissionProcessRunWS[] getAllCommissionRuns() {
		// TODO Auto-generated method stub
		return super.getAllCommissionRuns()
	}

	@Override
@WebResult
@WebMethod
	public CommissionWS[] getCommissionsByProcessRunId(Integer processRunId) {
		// TODO Auto-generated method stub
		return super.getCommissionsByProcessRunId(processRunId)
	}

	@Override
@WebResult
@WebMethod
	public BillingProcessWS getBillingProcess(Integer processId) {
		// TODO Auto-generated method stub
		return super.getBillingProcess(processId)
	}

	@Override
@WebResult
@WebMethod
	public Integer getLastBillingProcess() throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getLastBillingProcess()
	}

	@Override
@WebResult
@WebMethod
	public OrderProcessWS[] getOrderProcesses(Integer orderId) {
		// TODO Auto-generated method stub
		return super.getOrderProcesses(orderId)
	}

	@Override
@WebResult
@WebMethod
	public OrderProcessWS[] getOrderProcessesByInvoice(Integer invoiceId) {
		// TODO Auto-generated method stub
		return super.getOrderProcessesByInvoice(invoiceId)
	}

	@Override
@WebResult
@WebMethod
	public BillingProcessWS getReviewBillingProcess() {
		// TODO Auto-generated method stub
		return super.getReviewBillingProcess()
	}

	@Override
@WebResult
@WebMethod
	public BillingProcessConfigurationWS setReviewApproval(Boolean flag) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.setReviewApproval(flag)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getBillingProcessGeneratedInvoices(Integer processId) {
		// TODO Auto-generated method stub
		return super.getBillingProcessGeneratedInvoices(processId)
	}

	@Override
@WebResult
@WebMethod
	public void updatePreferences(PreferenceWS[] prefList) {
		// TODO Auto-generated method stub
		super.updatePreferences(prefList)
	}

	@Override
@WebResult
@WebMethod
	public void updatePreference(PreferenceWS preference) {
		// TODO Auto-generated method stub
		super.updatePreference(preference)
	}

	@Override
@WebResult
@WebMethod
	public PreferenceWS getPreference(Integer preferenceTypeId) {
		// TODO Auto-generated method stub
		return super.getPreference(preferenceTypeId)
	}

	@Override
@WebResult
@WebMethod
	public CurrencyWS[] getCurrencies() {
		// TODO Auto-generated method stub
		return super.getCurrencies()
	}

	@Override
@WebResult
@WebMethod
	public void updateCurrencies(CurrencyWS[] currencies) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateCurrencies(currencies)
	}

	@Override
@WebResult
@WebMethod
	public void updateCurrency(CurrencyWS ws) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateCurrency(ws)
	}

	@Override
@WebResult
@WebMethod
	public Integer createCurrency(CurrencyWS ws) {
		// TODO Auto-generated method stub
		return super.createCurrency(ws)
	}

	@Override
@WebResult
@WebMethod
	public boolean deleteCurrency(Integer currencyId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deleteCurrency(currencyId)
	}

	@Override
@WebResult
@WebMethod
	public CompanyWS getCompany() {
		// TODO Auto-generated method stub
		return super.getCompany()
	}

	@Override
@WebResult
@WebMethod
	public void updateCompany(CompanyWS companyWS) {
		// TODO Auto-generated method stub
		super.updateCompany(companyWS)
	}

	@Override
@WebResult
@WebMethod
	public void createUpdateNotification(Integer messageId, MessageDTO dto) {
		// TODO Auto-generated method stub
		super.createUpdateNotification(messageId, dto)
	}

	@Override
@WebResult
@WebMethod
	public void createCustomerNote(CustomerNoteWS note) {
		// TODO Auto-generated method stub
		super.createCustomerNote(note)
	}

	@Override
@WebResult
@WebMethod
	public PluggableTaskWS getPluginWS(Integer pluginId) {
		// TODO Auto-generated method stub
		return super.getPluginWS(pluginId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createPlugin(PluggableTaskWS plugin) {
		// TODO Auto-generated method stub
		return super.createPlugin(plugin)
	}

	@Override
@WebResult
@WebMethod
	public void updatePlugin(PluggableTaskWS plugin) {
		// TODO Auto-generated method stub
		super.updatePlugin(plugin)
	}

	@Override
@WebResult
@WebMethod
	public void deletePlugin(Integer id) {
		// TODO Auto-generated method stub
		super.deletePlugin(id)
	}

	@Override
@WebResult
@WebMethod
	public void rescheduleScheduledPlugin(Integer pluginId) {
		// TODO Auto-generated method stub
		super.rescheduleScheduledPlugin(pluginId)
	}

	@Override
@WebResult
@WebMethod
	public void unscheduleScheduledPlugin(Integer pluginId) {
		// TODO Auto-generated method stub
		super.unscheduleScheduledPlugin(pluginId)
	}



	@Override
@WebResult
@WebMethod
	public Integer createAsset(AssetWS asset) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createAsset(asset)
	}

	@Override
@WebResult
@WebMethod
	public void updateAsset(AssetWS asset) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateAsset(asset)
	}

	@Override
@WebResult
@WebMethod
	public void validateAsset(AssetWS asset) {
		// TODO Auto-generated method stub
		super.validateAsset(asset)
	}



	@Override
@WebResult
@WebMethod
	public AssetWS getAsset(Integer assetId) {
		// TODO Auto-generated method stub
		return super.getAsset(assetId)
	}

	@Override
@WebResult
@WebMethod
	public AssetWS getAssetByIdentifier(String assetIdentifier) {
		// TODO Auto-generated method stub
		return super.getAssetByIdentifier(assetIdentifier)
	}

	@Override
@WebResult
@WebMethod
	public void deleteAsset(Integer assetId) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteAsset(assetId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getAssetsForCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return super.getAssetsForCategory(categoryId)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] getAssetsForItem(Integer itemId) {
		// TODO Auto-generated method stub
		return super.getAssetsForItem(itemId)
	}

	@Override
@WebResult
@WebMethod
	public Long startImportAssetJob(int itemId, String identifierColumnName, String notesColumnName,
			String globalColumnName, String entitiesColumnName, String sourceFilePath, String errorFilePath)
			throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.startImportAssetJob(itemId, identifierColumnName, notesColumnName, globalColumnName, entitiesColumnName,
				sourceFilePath, errorFilePath)
	}

	@Override
@WebResult
@WebMethod
	public AssetTransitionDTOEx[] getAssetTransitions(Integer assetId) {
		// TODO Auto-generated method stub
		return super.getAssetTransitions(assetId)
	}

	@Override
@WebResult
@WebMethod
	public AssetSearchResult findAssets(int productId, SearchCriteria criteria) {
		// TODO Auto-generated method stub
		return super.findAssets(productId, criteria)
	}

	@Override
@WebResult
@WebMethod
	public AssetAssignmentWS[] getAssetAssignmentsForAsset(Integer assetId) {
		// TODO Auto-generated method stub
		return super.getAssetAssignmentsForAsset(assetId)
	}

	@Override
@WebResult
@WebMethod
	public AssetAssignmentWS[] getAssetAssignmentsForOrder(Integer orderId) {
		// TODO Auto-generated method stub
		return super.getAssetAssignmentsForOrder(orderId)
	}

	@Override
@WebResult
@WebMethod
	public Integer findOrderForAsset(Integer assetId, Date date) {
		// TODO Auto-generated method stub
		return super.findOrderForAsset(assetId, date)
	}

	@Override
@WebResult
@WebMethod
	public Integer[] findOrdersForAssetAndDateRange(Integer assetId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return super.findOrdersForAssetAndDateRange(assetId, startDate, endDate)
	}

	@Override
@WebResult
@WebMethod
	public Integer createMetaFieldGroup(MetaFieldGroupWS metafieldGroupWs) {
		// TODO Auto-generated method stub
		return super.createMetaFieldGroup(metafieldGroupWs)
	}

	@Override
@WebResult
@WebMethod
	public void updateMetaFieldGroup(MetaFieldGroupWS metafieldGroupWs) {
		// TODO Auto-generated method stub
		super.updateMetaFieldGroup(metafieldGroupWs)
	}

	@Override
@WebResult
@WebMethod
	public void deleteMetaFieldGroup(Integer metafieldGroupId) {
		// TODO Auto-generated method stub
		super.deleteMetaFieldGroup(metafieldGroupId)
	}

	@Override
@WebResult
@WebMethod
	public MetaFieldGroupWS getMetaFieldGroup(Integer metafieldGroupId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getMetaFieldGroup(metafieldGroupId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createMetaField(MetaFieldWS metafieldWs) {
		// TODO Auto-generated method stub
		return super.createMetaField(metafieldWs)
	}

	@Override
@WebResult
@WebMethod
	public void updateMetaField(MetaFieldWS metafieldWs) {
		// TODO Auto-generated method stub
		super.updateMetaField(metafieldWs)
	}

	@Override
@WebResult
@WebMethod
	public void deleteMetaField(Integer metafieldId) {
		// TODO Auto-generated method stub
		super.deleteMetaField(metafieldId)
	}

	@Override
@WebResult
@WebMethod
	public MetaFieldWS getMetaField(Integer metafieldId) {
		// TODO Auto-generated method stub
		return super.getMetaField(metafieldId)
	}

	@Override
@WebResult
@WebMethod
	public MetaFieldWS[] getMetaFieldsForEntity(String entityType) {
		// TODO Auto-generated method stub
		return super.getMetaFieldsForEntity(entityType)
	}

	@Override
@WebResult
@WebMethod
	public MetaFieldGroupWS[] getMetaFieldGroupsForEntity(String entityType) {
		// TODO Auto-generated method stub
		return super.getMetaFieldGroupsForEntity(entityType)
	}

	@Override
@WebResult
@WebMethod
	public Integer createOrUpdateDiscount(DiscountWS discount) {
		// TODO Auto-generated method stub
		return super.createOrUpdateDiscount(discount)
	}

	@Override
@WebResult
@WebMethod
	public OrderPeriodWS[] getOrderPeriods() throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getOrderPeriods()
	}

	@Override
@WebResult
@WebMethod
	public OrderChangeStatusWS[] getOrderChangeStatusesForCompany() {
		// TODO Auto-generated method stub
		return super.getOrderChangeStatusesForCompany()
	}

	@Override
@WebResult
@WebMethod
	public Integer createOrderChangeStatus(OrderChangeStatusWS orderChangeStatusWS) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createOrderChangeStatus(orderChangeStatusWS)
	}

	@Override
@WebResult
@WebMethod
	public void updateOrderChangeStatus(OrderChangeStatusWS orderChangeStatusWS) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.updateOrderChangeStatus(orderChangeStatusWS)
	}

	@Override
@WebResult
@WebMethod
	public void deleteOrderChangeStatus(Integer id) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteOrderChangeStatus(id)
	}

	@Override
@WebResult
@WebMethod
	public void saveOrderChangeStatuses(OrderChangeStatusWS[] orderChangeStatuses) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.saveOrderChangeStatuses(orderChangeStatuses)
	}

	@Override
@WebResult
@WebMethod
	public OrderChangeTypeWS[] getOrderChangeTypesForCompany() {
		// TODO Auto-generated method stub
		return super.getOrderChangeTypesForCompany()
	}

	@Override
@WebResult
@WebMethod
	public OrderChangeTypeWS getOrderChangeTypeByName(String name) {
		// TODO Auto-generated method stub
		return super.getOrderChangeTypeByName(name)
	}

	@Override
@WebResult
@WebMethod
	public OrderChangeTypeWS getOrderChangeTypeById(Integer orderChangeTypeId) {
		// TODO Auto-generated method stub
		return super.getOrderChangeTypeById(orderChangeTypeId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateOrderChangeType(OrderChangeTypeWS orderChangeTypeWS) {
		// TODO Auto-generated method stub
		return super.createUpdateOrderChangeType(orderChangeTypeWS)
	}

	@Override
@WebResult
@WebMethod
	public void deleteOrderChangeType(Integer orderChangeTypeId) {
		// TODO Auto-generated method stub
		super.deleteOrderChangeType(orderChangeTypeId)
	}

	@Override
@WebResult
@WebMethod
	public OrderChangeWS[] getOrderChanges(Integer orderId) {
		// TODO Auto-generated method stub
		return super.getOrderChanges(orderId)
	}

	@Override
@WebResult
@WebMethod
	public PaymentMethodTemplateWS getPaymentMethodTemplate(Integer templateId) {
		// TODO Auto-generated method stub
		return super.getPaymentMethodTemplate(templateId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createPaymentMethodType(PaymentMethodTypeWS paymentMethodType) {
		// TODO Auto-generated method stub
		return super.createPaymentMethodType(paymentMethodType)
	}

	@Override
@WebResult
@WebMethod
	public void updatePaymentMethodType(PaymentMethodTypeWS paymentMethodType) {
		// TODO Auto-generated method stub
		super.updatePaymentMethodType(paymentMethodType)
	}

	@Override
@WebResult
@WebMethod
	public PaymentMethodTypeWS getPaymentMethodType(Integer paymentMethodTypeId) {
		// TODO Auto-generated method stub
		return super.getPaymentMethodType(paymentMethodTypeId)
	}

	@Override
@WebResult
@WebMethod
	public boolean deletePaymentMethodType(Integer paymentMethodTypeId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deletePaymentMethodType(paymentMethodTypeId)
	}

	@Override
@WebResult
@WebMethod
	public boolean removePaymentInstrument(Integer instrumentId) {
		// TODO Auto-generated method stub
		return super.removePaymentInstrument(instrumentId)
	}

	@Override
@WebResult
@WebMethod
	public void deleteOrderStatus(OrderStatusWS orderStatus) throws SessionInternalError {
		// TODO Auto-generated method stub
		super.deleteOrderStatus(orderStatus)
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateOrderStatus(OrderStatusWS orderStatusWS) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUpdateOrderStatus(orderStatusWS)
	}

	@Override
@WebResult
@WebMethod
	public OrderStatusWS findOrderStatusById(Integer orderStatusId) {
		// TODO Auto-generated method stub
		return super.findOrderStatusById(orderStatusId)
	}

	@Override
@WebResult
@WebMethod
	public int getDefaultOrderStatusId(OrderStatusFlag flag, Integer entityId) {
		// TODO Auto-generated method stub
		return super.getDefaultOrderStatusId(flag, entityId)
	}

	@Override
@WebResult
@WebMethod
	public Integer reserveAsset(Integer assetId, Integer userId) {
		// TODO Auto-generated method stub
		return super.reserveAsset(assetId, userId)
	}

	@Override
@WebResult
@WebMethod
	public void releaseAsset(Integer assetId, Integer userId) {
		// TODO Auto-generated method stub
		super.releaseAsset(assetId, userId)
	}

	@Override
@WebResult
@WebMethod
	public PluggableTaskTypeWS getPluginTypeWS(Integer id) {
		// TODO Auto-generated method stub
		return super.getPluginTypeWS(id)
	}

	@Override
@WebResult
@WebMethod
	public PluggableTaskTypeWS getPluginTypeWSByClassName(String className) {
		// TODO Auto-generated method stub
		return super.getPluginTypeWSByClassName(className)
	}

	@Override
@WebResult
@WebMethod
	public PluggableTaskTypeCategoryWS getPluginTypeCategory(Integer id) {
		// TODO Auto-generated method stub
		return super.getPluginTypeCategory(id)
	}

	@Override
@WebResult
@WebMethod
	public PluggableTaskTypeCategoryWS getPluginTypeCategoryByInterfaceName(String interfaceName) {
		// TODO Auto-generated method stub
		return super.getPluginTypeCategoryByInterfaceName(interfaceName)
	}



	@Override
@WebResult
@WebMethod
	public Integer createOrEditLanguage(LanguageWS languageWS) {
		// TODO Auto-generated method stub
		return super.createOrEditLanguage(languageWS)
	}

	@Override
@WebResult
@WebMethod
	public AssetWS[] findAssetsByProductCode(String productCode) {
		// TODO Auto-generated method stub
		return super.findAssetsByProductCode(productCode)
	}

	@Override
@WebResult
@WebMethod
	public AssetStatusDTOEx[] findAssetStatuses(String identifier) {
		// TODO Auto-generated method stub
		return super.findAssetStatuses(identifier)
	}

	@Override
@WebResult
@WebMethod
	public AssetWS findAssetByProductCodeAndIdentifier(String productCode, String identifier) {
		// TODO Auto-generated method stub
		return super.findAssetByProductCodeAndIdentifier(productCode, identifier)
	}

	@Override
@WebResult
@WebMethod
	public AssetWS[] findAssetsByProductCodeAndStatus(String productCode, Integer assetStatusId) {
		// TODO Auto-generated method stub
		return super.findAssetsByProductCodeAndStatus(productCode, assetStatusId)
	}

	@Override
@WebResult
@WebMethod
	public OrderPeriodWS getOrderPeriodWS(Integer orderPeriodId) {
		// TODO Auto-generated method stub
		return super.getOrderPeriodWS(orderPeriodId)
	}

	@Override
@WebResult
@WebMethod
	public Integer createOrderPeriod(OrderPeriodWS orderPeriod) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createOrderPeriod(orderPeriod)
	}

	@Override
@WebResult
@WebMethod
	public EnumerationWS getEnumeration(Integer enumerationId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getEnumeration(enumerationId)
	}

	@Override
@WebResult
@WebMethod
	public EnumerationWS getEnumerationByName(String name) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.getEnumerationByName(name)
	}


	@Override
@WebResult
@WebMethod
	public Long getAllEnumerationsCount() {
		// TODO Auto-generated method stub
		return super.getAllEnumerationsCount()
	}

	@Override
@WebResult
@WebMethod
	public Integer createUpdateEnumeration(EnumerationWS enumeration) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.createUpdateEnumeration(enumeration)
	}

	@Override
@WebResult
@WebMethod
	public boolean deleteEnumeration(Integer enumerationId) throws SessionInternalError {
		// TODO Auto-generated method stub
		return super.deleteEnumeration(enumerationId)
	}



	@Override
@WebResult
@WebMethod
	public void resetPassword(int userId) {
		// TODO Auto-generated method stub
		super.resetPassword(userId)
	}

}
 
 
 