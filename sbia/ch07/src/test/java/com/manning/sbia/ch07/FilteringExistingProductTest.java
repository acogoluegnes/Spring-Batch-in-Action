/**
 * 
 */
package com.manning.sbia.ch07;

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
public class FilteringExistingProductTest {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before public void setUpt() {
		jdbcTemplate.update("delete from product");
		jdbcTemplate.update("insert into product (id) values(?)","214");
		jdbcTemplate.update("insert into product (id) values(?)","215");
	}

	@Test public void changingState() throws Exception {
		JobExecution exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputFile", "classpath:/products.txt")
			.toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED, exec.getExitStatus());
		StepExecution stepExecution = exec.getStepExecutions().iterator().next();
		Assert.assertEquals(2,stepExecution.getFilterCount());
		Assert.assertEquals(6,stepExecution.getWriteCount());	
	}	
	
}
