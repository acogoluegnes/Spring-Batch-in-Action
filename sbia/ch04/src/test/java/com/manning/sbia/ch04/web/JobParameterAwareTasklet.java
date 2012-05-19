/**
 * 
 */
package com.manning.sbia.ch04.web;

import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author acogoluegnes
 *
 */
public class JobParameterAwareTasklet implements Tasklet {
	
	private Map<String, String> params;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
		params.clear();
		for(Map.Entry<String, Object> entry : jobParameters.entrySet()) {
			params.put(entry.getKey(), entry.getValue().toString());
		}
		return RepeatStatus.FINISHED;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}
