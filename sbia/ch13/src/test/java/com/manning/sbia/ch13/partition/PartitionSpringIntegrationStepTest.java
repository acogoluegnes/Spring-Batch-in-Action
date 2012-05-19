package com.manning.sbia.ch13.partition;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.manning.sbia.ch13.chunk.RemoteChunkingSpringIntegrationStepTest;

public class PartitionSpringIntegrationStepTest {
	
	private GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job partitionImportProductsJob;
	
	@Before public void setUp() {
		createApplicationContext();
		injectDependenciesIntoTest();
	}
	
	@After public void tearDown() {
		ctx.close();
	}

	@Test
	public void testMultithreadedStep() throws Exception {
		assertNotNull(partitionImportProductsJob);
		/*JobExecution partitionImportProductsJobExec = launcher.run(
				partitionImportProductsJob,
			new JobParametersBuilder()
				.toJobParameters()
		);*/
	}
	
	private void createApplicationContext() {
		ctx.load(PartitionSpringIntegrationStepTest.class,this.getClass().getSimpleName()+"-context.xml");
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();
	}
	
	private void injectDependenciesIntoTest() {
		AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
		beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
	}

}
