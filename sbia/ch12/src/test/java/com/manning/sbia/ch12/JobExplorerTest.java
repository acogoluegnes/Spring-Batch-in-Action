/**
 * 
 */
package com.manning.sbia.ch12;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author templth
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JobExplorerTest extends AbstractJobStructureTest {
	@Autowired
	private JobExplorer jobExplorer;

	@Test public void jobExplorerWithSuccess() throws Exception {
		launchSuccessJob();

		List<JobInstance> jobInstances = jobExplorer.getJobInstances("importProductsJobSuccess", 0, 30);
		Assert.assertEquals(1, jobInstances.size());

		JobInstance jobInstance = jobInstances.get(0);
		Assert.assertEquals("importProductsJobSuccess", jobInstance.getJobName());
		
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
		Assert.assertEquals(1, jobExecutions.size());
		
		JobExecution jobExecution = jobExecutions.get(0);
		Assert.assertEquals("exitCode=COMPLETED;exitDescription=",
							jobExecution.getExitStatus().toString());

		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
		Assert.assertEquals(1, stepExecutions.size());
		StepExecution stepExecution = stepExecutions.iterator().next();

		Assert.assertEquals("readWriteSuccess", stepExecution.getStepName());
		Assert.assertEquals("StepExecution: id=1, version=3, name=readWriteSuccess, status=COMPLETED," +
							" exitStatus=COMPLETED, readCount=8, filterCount=0, writeCount=8 readSkipCount=0," +
							" writeSkipCount=0, processSkipCount=0, commitCount=1, rollbackCount=0",
							stepExecution.getSummary());
		Assert.assertEquals(8, stepExecution.getReadCount());
		Assert.assertEquals(8, stepExecution.getWriteCount());
		Assert.assertEquals(0, stepExecution.getFilterCount());
		Assert.assertEquals(0, stepExecution.getReadSkipCount());
		Assert.assertEquals(0, stepExecution.getWriteSkipCount());
		Assert.assertEquals(0, stepExecution.getProcessSkipCount());
		Assert.assertEquals(1, stepExecution.getCommitCount());
		Assert.assertEquals(0, stepExecution.getRollbackCount());
	}
	
	@Test public void jobExplorerWithFailure() throws Exception {
		launchFailureJob();

		List<JobInstance> jobInstances = jobExplorer.getJobInstances("importProductsJobFailure", 0, 30);
		Assert.assertEquals(1, jobInstances.size());

		JobInstance jobInstance = jobInstances.get(0);
		Assert.assertEquals("importProductsJobFailure", jobInstance.getJobName());
		
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
		Assert.assertEquals(1, jobExecutions.size());
		
		JobExecution jobExecution = jobExecutions.get(0);
		Assert.assertEquals("exitCode=FAILED;exitDescription=",
							jobExecution.getExitStatus().toString());

		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
		Assert.assertEquals(1, stepExecutions.size());
		StepExecution stepExecution = stepExecutions.iterator().next();

		Assert.assertEquals("readWriteFailure", stepExecution.getStepName());
		Assert.assertEquals("FAILED", stepExecution.getStatus().toString());
		Assert.assertTrue(stepExecution.getExitStatus().toString().contains("exitCode=FAILED;exitDescription=org.springframework.batch.item.file.FlatFileParseException"));
		Assert.assertTrue(stepExecution.getExitStatus().toString().contains("input=[PR....210,BlackBerry 8100 Pearl,,124.60dd]"));
		Assert.assertEquals(0, stepExecution.getReadCount());
		Assert.assertEquals(0, stepExecution.getWriteCount());
		Assert.assertEquals(0, stepExecution.getFilterCount());
		Assert.assertEquals(0, stepExecution.getReadSkipCount());
		Assert.assertEquals(0, stepExecution.getWriteSkipCount());
		Assert.assertEquals(0, stepExecution.getProcessSkipCount());
		Assert.assertEquals(0, stepExecution.getCommitCount());
		Assert.assertEquals(1, stepExecution.getRollbackCount());
	}
	
	public JobExplorer getJobExplorer() {
		return jobExplorer;
	}

	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

}
