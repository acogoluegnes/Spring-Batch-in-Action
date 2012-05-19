package com.manning.sbia.ch13.multithreadedstep;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProcessIndicatorTest {

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("readWriteMultiThreadedJob")
	private Job multiThreadedJob;

	@Autowired
	private DataSource dataSource;
	
	@Before
	public void initializeDatabase() throws Exception {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		int count = 55;
		for (int i = 0; i < count; i++) {
			String sql = "insert into product (id,name,description,price) values('"+i+"','Product "+i+"','',124.60)";
			template.update(sql);
		}
	}
	
	@Test
	public void testMultithreadedStep() throws Exception {
		JobExecution multiThreadedJobExec = launcher.run(
			multiThreadedJob,
			new JobParametersBuilder().toJobParameters()
		);

		JdbcTemplate template = new JdbcTemplate(dataSource);
		int count = template.queryForInt("select count(id) from product where processed = false");
		Assert.assertEquals(0, count);
	}
}
