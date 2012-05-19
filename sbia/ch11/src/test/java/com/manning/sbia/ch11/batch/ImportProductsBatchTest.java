/**
 * 
 */
package com.manning.sbia.ch11.batch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
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
public class ImportProductsBatchTest {

	@Autowired
	private Job job;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before public void setUp() {
		jdbcTemplate.update("delete from product");
	}
	
	@Test public void importProducts() throws Exception {
		assertPreConditions();
		String importId = "partner1-1";
		JobExecution exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("importId", importId)
			.addString("inputFile", productsFilePath(importId)).toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		Assert.assertEquals(1,countProducts());
		
		importId = "partner1-2";
		exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("importId", importId)
			.addString("inputFile", productsFilePath(importId)).toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		Assert.assertEquals(5,countProducts());
		
		importId = "partner1-3";
		exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("importId", importId)
			.addString("inputFile", productsFilePath(importId)).toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		Assert.assertEquals(8,countProducts());		
	}
	
	
	
	private void assertPreConditions() {
		Assert.assertEquals(0, countProducts());		
	}
	
	private int countProducts() {
		return jdbcTemplate.queryForInt("select count(1) from product");
	}

	private String productsFilePath(String importId) {
		return "./src/test/resources/com/manning/sbia/ch11/product-import-samples/products-"+importId+".xml";
	}
	
}
