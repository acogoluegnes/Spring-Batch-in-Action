/**
 * 
 */
package com.manning.sbia.ch04.stop;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author acogoluegnes
 *
 */
public class ProcessItemsTasklet implements Tasklet {
	
	private boolean stop;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if(shouldStop()) {
			chunkContext.getStepContext().getStepExecution().setTerminateOnly();
		}
		processItem();
		if(moreItemsToProcess()) {
			return RepeatStatus.CONTINUABLE;
		} else {
			return RepeatStatus.FINISHED;
		}
		
	}	
	
	private boolean moreItemsToProcess() {
		return true;
	}

	private void processItem() { }

	private boolean shouldStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
