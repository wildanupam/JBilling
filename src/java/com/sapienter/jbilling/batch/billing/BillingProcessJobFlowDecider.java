package com.sapienter.jbilling.batch.billing;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

import com.sapienter.jbilling.common.FormatLogger;
import com.sapienter.jbilling.server.util.Constants;

public class BillingProcessJobFlowDecider implements JobExecutionDecider {

	private static final FormatLogger logger = new FormatLogger(Logger.getLogger(BillingProcessJobFlowDecider.class));
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		ExecutionContext jobContext = jobExecution.getExecutionContext();
        Integer usersFailed = 0; 
        if(jobContext.containsKey(Constants.JOBCONTEXT_TOTAL_USERS_FAILED_KEY)) {
        	usersFailed = jobContext.getInt(Constants.JOBCONTEXT_TOTAL_USERS_FAILED_KEY);
        }
        
        if(usersFailed == 0) {
        	logger.debug("Returning exit status of step SUCCESS");
        	return new FlowExecutionStatus("SUCCESS");
        }
        logger.debug("Returning exit status of step FAILURE");
        return new FlowExecutionStatus("FAILURE");
	}

}
