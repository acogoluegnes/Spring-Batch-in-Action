/**
 * 
 */
package com.manning.sbia.ch07;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ItemProcessorConfigurationTest {
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	private String targetFile = "file:./target/output.txt";
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Before public void setUp() throws Exception  {
		Resource resource = resourceLoader.getResource(targetFile);
		if(resource.exists()) {
			boolean deleted = FileUtils.deleteQuietly(resource.getFile());
			Assert.assertTrue("couldn't delete target file before execution", deleted);
		}
	}

	@Test public void itemProcessorConfiguration() throws Exception {
		Assert.assertEquals(9, template.queryForInt("select count(1) from product"));
		JobExecution exec = jobLauncher.run(job, new JobParametersBuilder()
			.addString("targetFile", targetFile)
			.toJobParameters()
		);
		Assert.assertEquals(ExitStatus.COMPLETED, exec.getExitStatus());
		Resource resource = resourceLoader.getResource(targetFile);
		Assert.assertEquals(5,FileUtils.readLines(resource.getFile()).size());
	}
	
	
	
}
