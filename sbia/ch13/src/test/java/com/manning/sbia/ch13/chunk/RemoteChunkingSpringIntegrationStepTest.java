package com.manning.sbia.ch13.chunk;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch13.DummyProductWriter;

public class RemoteChunkingSpringIntegrationStepTest {
	
	private GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job remoteChunkingImportProductsJob;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DummyProductWriter itemWriter;
	
	@Before
	public void setUp() throws Exception {
		createApplicationContext();
		injectDependenciesIntoTest();
		int count = 55;
		for (int i = 0; i < count; i++) {
			String sql = "insert into product (id,name,description,price) values(?,?,?,?)";
			jdbcTemplate.update(
				sql,
				i,"Product "+i,"",124.6
			);
		}
	}

	@After
	public void teardDown() {
		ctx.close();
	}
	
	@Test
	public void testMultithreadedStep() throws Exception {
		JobExecution remoteChunkingImportProductsJobExec = launcher.run(
				remoteChunkingImportProductsJob,
			new JobParametersBuilder()
				.toJobParameters()
		);
		assertEquals(ExitStatus.COMPLETED, remoteChunkingImportProductsJobExec.getExitStatus());
		assertEquals(jdbcTemplate.queryForInt("select count(1) from product"),itemWriter.getProducts().size());
	}
	
	private void createApplicationContext() {
		ctx.load(RemoteChunkingSpringIntegrationStepTest.class,this.getClass().getSimpleName()+"-context.xml");
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();
	}
	
	private void injectDependenciesIntoTest() {
		AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
		beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
	}
}
