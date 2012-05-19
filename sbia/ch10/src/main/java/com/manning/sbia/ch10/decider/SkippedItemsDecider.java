/**
 * 
 */
package com.manning.sbia.ch10.decider;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * @author acogoluegnes
 *
 */
public class SkippedItemsDecider implements JobExecutionDecider {

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.job.flow.JobExecutionDecider#decide(org.springframework.batch.core.JobExecution, org.springframework.batch.core.StepExecution)
	 */
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution,
			StepExecution stepExecution) {
		if(!ExitStatus.FAILED.equals(stepExecution.getExitStatus()) &&
				stepExecution.getSkipCount() > 0) {
			return new FlowExecutionStatus("COMPLETED WITH SKIPS");
		} else {
			return new FlowExecutionStatus(jobExecution.getExitStatus().toString());
		}
	}

}
