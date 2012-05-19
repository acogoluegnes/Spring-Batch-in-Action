/**
 * 
 */
package com.manning.sbia.ch03;

import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author templth
 *
 */
@ContextConfiguration()
public class JobStructureTest extends AbstractJobStructureTest {

	@Test public void delimitedJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
	}
	
	
}
