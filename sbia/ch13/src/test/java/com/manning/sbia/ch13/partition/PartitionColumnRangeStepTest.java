package com.manning.sbia.ch13.partition;

import javax.sql.DataSource;

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
public class PartitionColumnRangeStepTest {

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("partitionImportProductsJob")
	private Job partitionImportProductsJob;

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
		JobExecution partitionImportProductsJobExec = launcher.run(
				partitionImportProductsJob,
			new JobParametersBuilder()
				.toJobParameters()
		);
	}
}
