/**
 * 
 */
package com.manning.sbia.ch04.stop;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StopTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job readWriteJob;

	@Autowired
	private Job taskletJob;
	
	@Autowired
	private Job jobOperatorJob;

	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private StopListener stopListener;

	@Autowired
	private ProcessItemsTasklet tasklet;
	
	@Test public void stopReadWrite() throws Exception {
		JobExecution jobExecution = jobLauncher.run(readWriteJob, new JobParameters());
		Assert.assertTrue(jobExecution.getStatus().isLessThanOrEqualTo(BatchStatus.STARTED));
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(readWriteJob.getName());
		Assert.assertEquals(1,runningExecutions.size());
		stopListener.setStop(true);
		waitForTermination(readWriteJob);
		runningExecutions = jobOperator.getRunningExecutions(readWriteJob.getName());
		Assert.assertEquals(0,runningExecutions.size());
	}
	
	@Test public void stopTasklet() throws Exception {
		JobExecution jobExecution = jobLauncher.run(taskletJob, new JobParameters());
		Assert.assertTrue(jobExecution.getStatus().isLessThanOrEqualTo(BatchStatus.STARTED));
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(taskletJob.getName());
		Assert.assertEquals(1,runningExecutions.size());
		tasklet.setStop(true);
		waitForTermination(taskletJob);
		runningExecutions = jobOperator.getRunningExecutions(taskletJob.getName());
		Assert.assertEquals(0,runningExecutions.size());
	}
	
	@Test public void stopWithJobOperator() throws Exception {
		JobExecution jobExecution = jobLauncher.run(jobOperatorJob, new JobParameters());
		Assert.assertTrue(jobExecution.getStatus().isLessThanOrEqualTo(BatchStatus.STARTED));
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(jobOperatorJob.getName());
		Assert.assertEquals(1,runningExecutions.size());
		Long executionId = runningExecutions.iterator().next();
		boolean stopMessageSent = jobOperator.stop(executionId);
		Assert.assertTrue(stopMessageSent);
		waitForTermination(jobOperatorJob);
		runningExecutions = jobOperator.getRunningExecutions(jobOperatorJob.getName());
		Assert.assertEquals(0,runningExecutions.size());
	}

	private void waitForTermination(Job job) throws NoSuchJobException,
			InterruptedException {
		int timeout = 10000;
		int current = 0;
		while (jobOperator.getRunningExecutions(job.getName()).size() > 0
				&& current < timeout) {
			Thread.sleep(100);
			current += 100;
		}
		if(jobOperator.getRunningExecutions(job.getName()).size() > 0) {
			throw new IllegalStateException("the execution hasn't stopped " +
					"in the expected period (timeout = "+timeout+" ms)." +
					"Consider increasing the timeout before checking if it's a bug.");
		}
	}

}
