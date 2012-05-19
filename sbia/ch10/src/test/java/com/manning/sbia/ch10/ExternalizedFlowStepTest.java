/**
 * 
 */
package com.manning.sbia.ch10;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"batch-infrastructure.xml","externalized-flow-step.xml"})
public class ExternalizedFlowStepTest extends AbstractJobTest {

	@Override
	protected int getExpectedNbOfInvocationFileExists() {
		return 2;
	}
	
	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems() {
		return super.getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems()+1;
	}
	
	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkSkippedItems() {
		return super.getExpectedNbStepExecutionsDownloadedFileOkSkippedItems()+1;
	}
	
	@Override
	protected int getExpectedNbStepExecutionsNoDownloadedFile() {
		return super.getExpectedNbStepExecutionsNoDownloadedFile()+1;
	}
	
}
