/**
 * 
 */
package com.manning.sbia.ch10;

import static org.junit.Assert.assertNull;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"batch-infrastructure.xml","transmit-data-job-context-late-binding-job.xml"})
public class TransmitDataJobContextLateBindingTest extends AbstractJobTest {
	
	@Override
	protected void assertMetadataDownloadFileOkNoSkippedItems() {
		assertNull(importMetadataHolder.get());
	}
	
	@Override
	protected void assertMetadataDownloadFileOkSkippedItems() {
		assertNull(importMetadataHolder.get());
	}
	
	@Override
	protected void assertMetadataNoDownloadFile() {
		assertNull(importMetadataHolder.get());
	}
	
}
