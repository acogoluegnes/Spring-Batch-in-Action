/**
 * 
 */
package com.manning.sbia.ch09.transaction;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TaskletTransactionTest {
	
	@Autowired
	private Job noTransactionJob;
	
	@Autowired
	private Job transactionalJob;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Tasklet tasklet;
	
	@Test public void differentTransactionsOnRepetitiveInvocations() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		
		when(tasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
			.thenReturn(RepeatStatus.CONTINUABLE)
			.thenReturn(RepeatStatus.CONTINUABLE)
			.thenReturn(RepeatStatus.FINISHED);
		JobExecution exec = jobLauncher.run(transactionalJob, jobParameters);
		
		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		Assert.assertEquals(3, stepExec.getCommitCount());
	}
	
	@Test public void noTransactionInTasklet() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		
		when(tasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
			.thenAnswer(new Answer<RepeatStatus>() {
				@Override
				public RepeatStatus answer(InvocationOnMock invocation)
						throws Throwable {
					noActiveTransaction();
					return RepeatStatus.FINISHED;
				}
			});
		
		JobExecution exec = jobLauncher.run(noTransactionJob, jobParameters);
		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		Assert.assertEquals(1, stepExec.getCommitCount());
	}
	
	protected void noActiveTransaction() {
		Assert.assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
	}

	private void isTransactionActive() {
		Assert.assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
	}
	
	private class MyTasklet implements Tasklet {
		
		@Override
		public RepeatStatus execute(StepContribution contribution,
				ChunkContext chunkContext) throws Exception {
			
			return RepeatStatus.FINISHED;
		}
		
	}
}
