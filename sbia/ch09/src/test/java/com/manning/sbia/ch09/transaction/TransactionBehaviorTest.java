/**
 * 
 */
package com.manning.sbia.ch09.transaction;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.manning.sbia.ch09.AbstractRobustnessTest;

/**
 * @author acogoluegnes
 *
 */
@ContextConfiguration
public class TransactionBehaviorTest extends AbstractRobustnessTest {
	
	@Autowired
	private Job noRollbackJob;
	
	@Autowired
	private Job notTransactionalReaderJob;
	
	@Autowired
	private Job transactionalReaderJob;
	
	@Test public void rollbackOnException() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailWriting.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		assertRead(read, exec);
		assertWrite(read-1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(6, exec);
		// rollback on first error, new attempt on the chunk,
		// tries item per item, rollback for single item on error
		assertRollback(2, exec);
	}

	@Test public void noRollbackOnException() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailWriting.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			noRollbackJob, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		assertRead(read, exec);
		assertWrite(read-1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(7, exec);
		// no rollback on first error, new attempt on the chunk,
		// tries item per item, rollback for single item on error
		assertRollback(1, exec);
	}
	
	@Test public void notTransactionalReader() throws Exception {
		int read = 12;
		when(service.reading())
			.thenReturn("1")
			.thenReturn("2")
			.thenReturn("3")
			.thenReturn("4")
			.thenReturn("5")
			.thenReturn("6")
			.thenReturn("7")
			.thenReturn("8")
			.thenReturn("9")
			.thenReturn("10")
			.thenReturn("11")
			.thenReturn("12")
			.thenReturn(null);
		
		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailWriting.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			notTransactionalReaderJob, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		verify(service,times(13)).reading();
		assertRead(read, exec);
		assertWrite(read-1, exec);
	}
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired 
	private QueueViewMBean productQueueView;
	
	@Test public void transactionalReader() throws Exception {
		while(jmsTemplate.receive() != null) { }
		int read = 12;
		for(int i=1;i<=read;i++) {
			jmsTemplate.convertAndSend(String.valueOf(i));
		}		
		
		Assert.assertEquals(read,productQueueView.getQueueSize());
		
		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailWriting.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new DeadlockLoserDataAccessException("",null)).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			transactionalReaderJob, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		int expectedWritten = 5;
		int stillOnQueue = (int) productQueueView.getQueueSize(); 
		assertRead(10, exec);
		Assert.assertEquals(read-expectedWritten,stillOnQueue);
		assertWrite(expectedWritten, exec);
	}
	
}
