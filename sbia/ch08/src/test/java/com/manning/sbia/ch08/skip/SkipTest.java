/**
 * 
 */
package com.manning.sbia.ch08.skip;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
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
public class SkipTest {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job importProductsJob;
	
	@Autowired
	private Job importProductsJobWithSkipPolicy;
	
	@Test public void jobWithNoSkip() throws Exception {
		int initialCount = countProducts();
		int initialSkippedCount = countSkippedProducts();
		JobParameters params = new JobParametersBuilder()
			.addString("inputFile", "classpath:/com/manning/sbia/ch08/skip/products_no_error.txt")
			.toJobParameters();
		JobExecution exec = jobLauncher.run(importProductsJob, params);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		Assert.assertEquals(initialCount+8,countProducts());
		Assert.assertEquals(0,getStepExec(exec).getSkipCount());
		Assert.assertEquals(0,getStepExec(exec).getRollbackCount());
		Assert.assertEquals(initialSkippedCount+getStepExec(exec).getSkipCount(),countSkippedProducts());
	}
	
	@Test public void jobWithSkip() throws Exception {
		int initialCount = countProducts();
		int initialSkippedCount = countSkippedProducts();
		JobParameters params = new JobParametersBuilder()
			.addString("inputFile", "classpath:/com/manning/sbia/ch08/skip/products_with_errors.txt")
			.toJobParameters();
		JobExecution exec = jobLauncher.run(importProductsJob, params);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		Assert.assertEquals(initialCount+7,countProducts());
		Assert.assertEquals(1,getStepExec(exec).getSkipCount());
		Assert.assertEquals(initialSkippedCount+getStepExec(exec).getSkipCount(),countSkippedProducts());
		Assert.assertEquals(0,getStepExec(exec).getRollbackCount());
	}
	
	@Test public void jobWithTooManySkip() throws Exception {
		int initialCount = countProducts();
		int initialSkippedCount = countSkippedProducts();
		JobParameters params = new JobParametersBuilder()
			.addString("inputFile", "classpath:/com/manning/sbia/ch08/skip/products_too_many_errors.txt")
			.toJobParameters();
		JobExecution exec = jobLauncher.run(importProductsJob, params);
		Assert.assertEquals(ExitStatus.FAILED,exec.getExitStatus());
		Assert.assertEquals(initialCount+9,countProducts());
		Assert.assertEquals(2,getStepExec(exec).getSkipCount());
		Assert.assertEquals(1,getStepExec(exec).getRollbackCount());
		Assert.assertEquals(initialSkippedCount+getStepExec(exec).getSkipCount(),countSkippedProducts());
	}
	
	@Test public void jobWithSkipPolicy() throws Exception {
		deleteTables();
		int initialCount = countProducts();
		JobParameters params = new JobParametersBuilder()
			.addString("inputFile", "classpath:/com/manning/sbia/ch08/skip/products_too_many_errors.txt")
			.toJobParameters();
		JobExecution exec = jobLauncher.run(importProductsJobWithSkipPolicy, params);
		Assert.assertEquals(ExitStatus.COMPLETED,exec.getExitStatus());
		Assert.assertEquals(initialCount+13,countProducts());
		Assert.assertEquals(3,getStepExec(exec).getSkipCount());
		Assert.assertEquals(0,getStepExec(exec).getRollbackCount());
	}
	
	@Autowired
	private JdbcTemplate tpl;
	
	private int countProducts() {
		return tpl.queryForInt("select count(1) from product");
	}
	
	private void deleteTables() {
		tpl.execute("delete from product");
		tpl.execute("delete from skipped_product");
	}
	
	private int countSkippedProducts() {
		return tpl.queryForInt("select count(1) from skipped_product");
	}
	
	private StepExecution getStepExec(JobExecution exec) {
		return exec.getStepExecutions().iterator().next();
	}
	
}
