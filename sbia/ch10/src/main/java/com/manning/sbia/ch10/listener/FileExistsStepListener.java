/**
 * 
 */
package com.manning.sbia.ch10.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import com.manning.sbia.ch10.batch.BatchService;

/**
 * @author acogoluegnes
 *
 */
public class FileExistsStepListener implements StepExecutionListener {
	
	private BatchService batchService;
	
	private String targetFile;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) { }

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if(batchService.exists(targetFile)) {
			return new ExitStatus("FILE EXISTS");
		} else {
			return new ExitStatus("NO FILE");
		}
	}
	
	public void setBatchService(BatchService batchService) {
		this.batchService = batchService;
	}
	
	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}

}
