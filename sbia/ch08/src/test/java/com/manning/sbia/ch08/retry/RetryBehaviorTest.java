/**
 * 
 */
package com.manning.sbia.ch08.retry;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.RetryListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;

import com.manning.sbia.ch08.AbstractRobustnessTest;

/**
 * @author acogoluegnes
 *
 */
@ContextConfiguration
public class RetryBehaviorTest extends AbstractRobustnessTest {
	
	@Autowired
	private Job retryPolicyJob;
	
	@Autowired
	private RetryListener mockRetryListener;
	
	@Before public void init() {
		reset(mockRetryListener);
		when(mockRetryListener.open(any(RetryContext.class), any(RetryCallback.class)))
			.thenReturn(true);
			
	}
	
	@Test public void sunnyDay() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		assertRead(read, exec);
		assertWrite(read, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(0, exec);
	}
	
	@Test public void exceptionInReading() throws Exception {
		when(service.reading())
			.thenReturn("1")
			.thenReturn("2")
			.thenReturn("3")
			.thenReturn("4")
			.thenReturn("5")
			.thenReturn("6")
			.thenThrow(new OptimisticLockingFailureException("", null))
			.thenReturn("8")
			.thenReturn("9")
			.thenReturn("10")
			.thenReturn("11")
			.thenReturn("12")
			.thenReturn(null);
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		// no retry on read!
		Assert.assertEquals(ExitStatus.FAILED,exec.getExitStatus());
		verify(service,times(7)).reading();
		verify(service,times(5)).processing(anyString());
		verify(service,times(5)).writing(anyString());
		assertRead(6, exec);
		assertWrite(5, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(1, exec);
		assertRollback(1, exec);
	}
	
	@Test public void exceptionInWritingSkippable() throws Exception {
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
		doThrow(new DeadlockLoserDataAccessException("",null)).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		verify(service,times(5+5+2+1)).reading();
		verify(service,times(5+5+(1+1)+5+2)).processing(anyString());
		verify(service,times(5+2+2+2+5+2)).writing(anyString());
		assertRead(read, exec);
		assertWrite(read-1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(1+4+1, exec);
		assertRollback(3+1, exec);
	}
	
	@Test public void exceptionInWritingNotExhausted() throws Exception {
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
		
		doThrow(new DeadlockLoserDataAccessException("",null))
			.doThrow(new DeadlockLoserDataAccessException("",null))
			.doNothing()
			.when(service).writing(toFailWriting);
		
			
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		verify(service,times(5+5+2+1)).reading();
		verify(service,times(5+5+1+1+2)).processing(anyString());
		verify(service,times(5+2+2+5+2)).writing(anyString());
		verify(mockRetryListener,times(2)).onError(any(RetryContext.class), any(RetryCallback.class), any(Throwable.class));
		assertRead(read, exec);
		assertWrite(read, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(2, exec);
	}
	
	@Test public void exceptionInWritingNotSkippable() throws Exception {
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
		doThrow(new OptimisticLockingFailureException("",null)).when(service).writing(toFailWriting);
			
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.FAILED,exec.getExitStatus());
		verify(service,times(5+5)).reading();
		verify(service,times(5+5+1+1+1)).processing(anyString());
		verify(service,times(5+2+2+2)).writing(anyString());
		assertRead(10, exec);
		assertWrite(5, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(1, exec);
		assertRollback(3+1, exec); // why additional processing attempt & rollback?
	}
	
	@Test public void exceptionInProcessingSkippable() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailProcessing = "7";
		doNothing().when(service).processing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailProcessing.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new DeadlockLoserDataAccessException("",null)).when(service).processing(toFailProcessing);
			
		doNothing().when(service).writing(anyString());
		
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		verify(service,times(5+5+2+1)).reading();
		verify(service,times(5+2+2+2+4+2)).processing(anyString());
		verify(service,times(5+4+2)).writing(anyString());
		assertRead(read, exec);
		assertWrite(read-1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(1, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(3, exec); // rollback on each retry
	}
	
	@Test public void exceptionInProcessingNotSkippable() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailProcessing = "7";
		doNothing().when(service).processing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailProcessing.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new OptimisticLockingFailureException("",null)).when(service).processing(toFailProcessing);
			
		doNothing().when(service).writing(anyString());
		
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.FAILED,exec.getExitStatus());
		verify(service,times(5+5)).reading();
		verify(service,times(5+2+2+2+1)).processing(anyString()); // one more attempt on 6th, why?
		verify(service,times(5)).writing(anyString());
		assertRead(10, exec);
		assertWrite(5, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(1, exec); // first chunk
		assertRollback(4, exec); // 1 for each retry + the attempt on 6th (why?)
	}
	
	@Test public void exceptionInProcessingNotExhausted() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailProcessing = "7";
		doNothing().when(service).processing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {				
				return !toFailProcessing.equals(input);
			}
			
			@Override
			public void describeTo(Description desc) { }
			
		}));
		doThrow(new OptimisticLockingFailureException("",null))
			.doThrow(new OptimisticLockingFailureException("",null))
			.doNothing()
			.when(service).processing(toFailProcessing);
			
		doNothing().when(service).writing(anyString());
		
		JobExecution exec = jobLauncher.run(
			job, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		verify(service,times(5+5+2+1)).reading();
		verify(service,times(5+2+2+5+2)).processing(anyString()); // two retry
		verify(service,times(12)).writing(anyString());
		assertRead(read, exec);
		assertWrite(read, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(2, exec); // one for each retry
	}
	
	@Test public void retryPolicy() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		
		final String toFailProcessingConcurrency = "7";
		final String toFailProcessingDeadlock = "11";
		final int maxAttemptsConcurrency = 2;
		final int maxAttemptsDeadlock = 4;
		doAnswer(new Answer<Void>() {
			
			private int countConcurrency = 0;
			private int countDeadlock = 0;
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String item = (String) invocation.getArguments()[0];
				if(toFailProcessingConcurrency.equals(item) &&
					countConcurrency < maxAttemptsConcurrency 
				) {
					countConcurrency++;
					throw new ConcurrencyFailureException("");
				} else if(toFailProcessingDeadlock.equals(item) &&
					countDeadlock < maxAttemptsDeadlock	
				) {
					countDeadlock++;
					throw new DeadlockLoserDataAccessException("", null);
				}
				return null;
			}
		}).when(service).writing(anyString());
		
		JobExecution exec = jobLauncher.run(
			retryPolicyJob, 
			new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		assertRead(read, exec);
		assertWrite(read, exec);
		
		
		
	}
	
}
