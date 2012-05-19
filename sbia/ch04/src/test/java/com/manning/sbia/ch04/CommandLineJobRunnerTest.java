/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.batch.core.launch.support.SystemExiter;

/**
 * @author acogoluegnes
 *
 */
public class CommandLineJobRunnerTest {

	@Test public void run() throws Exception {
		final Queue<Integer> exitCode = new ArrayBlockingQueue<Integer>(1);
		CommandLineJobRunner.presetSystemExiter(new SystemExiter() {			
			@Override
			public void exit(int status) {
				exitCode.add(status);
			}
		});

		CommandLineJobRunner.main(new String[] {
			"/com/manning/sbia/ch04/import-products-job-exit-code.xml",
			"importProductsJob"
			}
		);
		Assert.assertEquals(0,exitCode.poll().intValue());
		
		CommandLineJobRunner.main(new String[] {
			"/com/manning/sbia/ch04/import-products-job-exit-code.xml",
			"importProductsJob",
			"exit.status=COMPLETED"
			}
		);
		Assert.assertEquals(0,exitCode.poll().intValue());
		
		CommandLineJobRunner.main(new String[] {
			"/com/manning/sbia/ch04/import-products-job-exit-code.xml",
			"importProductsJob",
			"exit.status=FAILED"
			}
		);
		Assert.assertEquals(1,exitCode.poll().intValue());
		
		CommandLineJobRunner.main(new String[] {
			"/com/manning/sbia/ch04/import-products-job-exit-code.xml",
			"importProductsJob",
			"exit.status=COMPLETED WITH SKIPS"
			}
		);
		Assert.assertEquals(3,exitCode.poll().intValue());
		
		CommandLineJobRunner.main(new String[] {
			"/com/manning/sbia/ch04/import-products-job-exit-code.xml",
			"importProductsJob",
			"exit.status=ANYTHING"
			}
		);
		Assert.assertEquals(2,exitCode.poll().intValue());
	}
	
}
