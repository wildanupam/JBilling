package com.sapienter.jbilling.batch.billing;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.SkipListener;
import org.springframework.beans.factory.InitializingBean;

import com.sapienter.jbilling.common.FormatLogger;
import com.sapienter.jbilling.server.process.IBillingProcessSessionBean;
import com.sapienter.jbilling.server.process.db.ProcessRunUserDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Context;

public class BillingProcessSkipListener extends JobContextHandler implements SkipListener<Object, Object>,InitializingBean{
	
	private static final FormatLogger logger = new FormatLogger(Logger.getLogger(BillingProcessSkipListener.class));
		
	private IBillingProcessSessionBean local;
	
	/**
	 * Called for every object that was skipped. 
	 * Increments total users failed value in context and adds user id to failed user list in context.
	 */
	@Override
	public void onSkipInProcess(Object obj, Throwable th) {
		Integer userId = (Integer) obj;
		
		Integer billingProcessId = this.getIntegerFromContext(Constants.JOBCONTEXT_BILLING_PROCESS_ID_KEY);
		
		logger.debug("BillingProcessId # " + billingProcessId + " || UserId # " + userId + " +++ Entering onSkipProcess()");
		logger.info("BillingProcessId # " + billingProcessId + " || UserId # " + userId + " +++ Skipped due to Exception # " + th);		
		
		Integer failed = this.getIntegerFromContext(Constants.JOBCONTEXT_TOTAL_USERS_FAILED_KEY);
		this.addIntegerToContext(Constants.JOBCONTEXT_TOTAL_USERS_FAILED_KEY, failed + 1);
		List<Integer> list = this.getIntegerListFromContext(Constants.JOBCONTEXT_FAILED_USERS_LIST_KEY);
		synchronized (list) {
			list.add(userId);
			logger.debug("BillingProcessId # " + billingProcessId + " || UserId # " + userId + " +++ Failed users list size # " + list.size());
			this.addIntegerListToContext(Constants.JOBCONTEXT_FAILED_USERS_LIST_KEY, list);
		}
		
		local.addProcessRunUser(billingProcessId, userId, ProcessRunUserDTO.STATUS_FAILED);
		logger.debug("BillingProcessId # " + billingProcessId + " || UserId # " + userId + " +++ Leaving onSkipProcess()");
	}

	@Override
	public void onSkipInRead(Throwable th) {
	}

	@Override
	public void onSkipInWrite(Object obj, Throwable th) {	
	}

	/**
	 * This method is called when this bean is initialized to get BillingProcessSessionBean bean from context.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		local = (IBillingProcessSessionBean) Context.getBean(Context.Name.BILLING_PROCESS_SESSION);
	}
}
