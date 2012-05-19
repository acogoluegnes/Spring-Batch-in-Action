/**
 * 
 */
package com.manning.sbia.ch04.incrementer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IncrementerTest {
	
	@Autowired
	private Job job;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JobOperator jobOperator;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private Tasklet tasklet;

	@Test @DirtiesContext public void jobLauncherThenIncrementer() throws Exception {
		doReturn(RepeatStatus.FINISHED)
			.doReturn(RepeatStatus.FINISHED)
			.doThrow(new RuntimeException())
			.doReturn(RepeatStatus.FINISHED)
			.when(tasklet).execute(any(StepContribution.class), any(ChunkContext.class));
		
		JobParameters jobParams = new JobParametersBuilder()
			.addString("input.file", "products.zip")
			.toJobParameters();
		
		JobExecution exec1 = jobLauncher.run(job, jobParams);
		Assert.assertEquals(ExitStatus.COMPLETED,exec1.getExitStatus());
		Assert.assertEquals(1,getJobInstancesCount());		
		
		Long idExec2 = jobOperator.startNextInstance(job.getName());
		Assert.assertEquals(ExitStatus.COMPLETED,getJobExecution(idExec2).getExitStatus());
		Assert.assertEquals(2,getJobInstancesCount());	
		
		Long idExec3 = jobOperator.startNextInstance(job.getName());
		Assert.assertEquals(ExitStatus.FAILED,getJobExecution(idExec3).getExitStatus());
		Assert.assertEquals(3,getJobInstancesCount());	
		
		Long idExec4 = jobOperator.startNextInstance(job.getName());
		Assert.assertEquals(ExitStatus.COMPLETED,getJobExecution(idExec4).getExitStatus());
		Assert.assertEquals(4,getJobInstancesCount());
	}
	
	private JobExecution getJobExecution(Long executionId) {
		return jobExplorer.getJobExecution(executionId);
	}
	
	private int getJobInstancesCount() {
		return jobExplorer.getJobInstances(job.getName(), 0, Integer.MAX_VALUE).size();
	}
	
}
