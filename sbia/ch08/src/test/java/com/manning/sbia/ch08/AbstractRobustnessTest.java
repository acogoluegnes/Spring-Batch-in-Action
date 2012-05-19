/**
 * 
 */
package com.manning.sbia.ch08;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractRobustnessTest {

	@Before public void setUp() {
		reset(service);
		reset(skipListener);
	}
	
	@Autowired
	protected JobLauncher jobLauncher;
	
	@Autowired
	protected Job job;
	
	@Autowired
	protected BusinessService service;
	
	@Autowired
	protected SkipListener<?, ?> skipListener;
	
	protected void configureServiceForRead(BusinessService service,int count) {
		List<String> args = new ArrayList<String>();
		for(int i=2;i<=count;i++) {
			args.add(String.valueOf(i));
		}
		args.add(null);
		when(service.reading()).thenReturn("1",args.toArray(new String[0]));
	}
	
	protected void assertRead(int read, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(read,stepExec.getReadCount());
	}
	
	protected void assertWrite(int write, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(write,stepExec.getWriteCount());
	}
	
	protected void assertProcessSkip(int processSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(processSkip,stepExec.getProcessSkipCount());
	}
	
	protected void assertReadSkip(int readSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(readSkip,stepExec.getReadSkipCount());
	}
	
	protected void assertWriteSkip(int writeSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(writeSkip,stepExec.getWriteSkipCount());
	}
	
	protected void assertCommit(int commit, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(commit,stepExec.getCommitCount());
	}
	
	protected void assertRollback(int rollback, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		Assert.assertEquals(rollback,stepExec.getRollbackCount());
	}

	protected StepExecution getStepExecution(JobExecution exec) {
		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		return stepExec;
	}
	
}
