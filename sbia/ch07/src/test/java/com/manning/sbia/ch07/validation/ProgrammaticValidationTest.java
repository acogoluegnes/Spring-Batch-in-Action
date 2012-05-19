/**
 * 
 */
package com.manning.sbia.ch07.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProgrammaticValidationTest {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before public void setUpt() {
		jdbcTemplate.update("delete from product");
	}

	@Test public void programmaticValidationSkip() throws Exception {
		JobExecution exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputFile", "classpath:/products-with-negative-price.txt")
			.addString("filter", "false")
			.toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED, exec.getExitStatus());
		StepExecution stepExecution = exec.getStepExecutions().iterator().next();
		Assert.assertEquals(0,stepExecution.getFilterCount());
		Assert.assertEquals(3,stepExecution.getProcessSkipCount());
		Assert.assertEquals(5,stepExecution.getWriteCount());	
	}	
	
	@Test public void programmaticValidationFilter() throws Exception {
		JobExecution exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputFile", "classpath:/products-with-negative-price.txt")
			.addString("filter", "true")
			.toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED, exec.getExitStatus());
		StepExecution stepExecution = exec.getStepExecutions().iterator().next();
		Assert.assertEquals(3,stepExecution.getFilterCount());
		Assert.assertEquals(0,stepExecution.getProcessSkipCount());
		Assert.assertEquals(5,stepExecution.getWriteCount());
	}	
	
}
