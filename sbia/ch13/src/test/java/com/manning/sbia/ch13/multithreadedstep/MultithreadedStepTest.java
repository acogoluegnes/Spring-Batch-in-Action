package com.manning.sbia.ch13.multithreadedstep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MultithreadedStepTest {

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("readWriteMultiThreadedJob")
	private Job multiThreadedJob;

	@Test
	public void testMultithreadedStep() throws Exception {
		long count = 55;
		JobExecution multiThreadedJobExec = launcher.run(
			multiThreadedJob,
			new JobParametersBuilder()
				.addLong("count",count)
				.toJobParameters()
		);
	}
}
