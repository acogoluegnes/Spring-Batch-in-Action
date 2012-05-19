/**
 * 
 */
package com.manning.sbia.ch10.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @author acogoluegnes
 *
 */
public class SkippedItemsStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) { }
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if(!ExitStatus.FAILED.equals(stepExecution.getExitStatus()) &&
				stepExecution.getSkipCount() > 0) {
			return new ExitStatus("COMPLETED WITH SKIPS");
		} else {
			return stepExecution.getExitStatus();
		}
	}
	
}
