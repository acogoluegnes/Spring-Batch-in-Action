/**
 * 
 */
package com.manning.sbia.ch10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"batch-infrastructure.xml","externalized-job.xml"})
public class ExternalizedJobTest extends AbstractJobTest {
	
	@Autowired
	private JobOperator jobOperator;

	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems() {
		int stepsInSubJob = 3;
		int stepJob = 1;
		return super.getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems()-stepsInSubJob+stepJob;
	}
	
	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkSkippedItems() {
		int stepsInSubJob = 3;
		int stepJob = 1;
		return super.getExpectedNbStepExecutionsDownloadedFileOkSkippedItems()-stepsInSubJob+stepJob;
	}
	
	@Override
	protected int getExpectedNbOfInvocationFileExists() {
		return 2;
	}
	
	@Override
	protected void extraAssertionsDowlnloadedFileOkNoSkippedItems() throws Exception {
		assertInstancesForSubJob();
	}
	
	@Override
	protected void extraAssertionsDowlnloadedFileOkSkippedItems() throws Exception {
		assertInstancesForSubJob();
	}
	
	@Override
	protected void extraAssertionsNoDownloadedFile() throws Exception {
		assertInstancesForSubJob();
	}
	
	public void assertInstancesForSubJob() throws Exception {
		Set<String> names = jobOperator.getJobNames();
		assertEquals(2,names.size(), names.size());
		for(String name : names) {
			List<Long> instances = jobOperator.getJobInstances(name, 0, Integer.MAX_VALUE);
			assertTrue(instances.size() > 0);
			assertTrue(instances.size() <= 3);
		}
	}
	
}
