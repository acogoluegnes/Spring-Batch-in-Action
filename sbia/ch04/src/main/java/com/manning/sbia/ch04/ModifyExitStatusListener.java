/**
 * 
 */
package com.manning.sbia.ch04;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

/**
 * @author acogoluegnes
 *
 */
public class ModifyExitStatusListener extends StepExecutionListenerSupport {

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		JobParameters jobParameters = stepExecution.getJobExecution().getJobInstance().getJobParameters();
		String exitStatus = jobParameters.getString("exit.status");
		if(exitStatus != null) {
			return new ExitStatus(exitStatus);
		}
		return super.afterStep(stepExecution);
	}
	
}
