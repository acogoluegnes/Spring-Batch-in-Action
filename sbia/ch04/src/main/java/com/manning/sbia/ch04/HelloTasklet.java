/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author acogoluegnes
 *
 */
public class HelloTasklet implements Tasklet {

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		if(jobParameters.isEmpty()) {
			System.out.println("No job parameters!");
		} else {
			System.out.println("Job parameters:");
			for(Map.Entry<String, JobParameter> param : jobParameters.getParameters().entrySet()) {
				System.out.println(String.format(
					"%s = %s (%s)",
					param.getKey(),param.getValue().getValue(),param.getValue().getType()
				));
			}
		}
		return RepeatStatus.FINISHED;
	}

}
