/**
 * 
 */
package com.manning.sbia.ch11.integration;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch11.integration.JobLaunchRequest;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JobLaunchingMessageHandlerTest {
	
	@Autowired
	@Qualifier("job-requests")
	private MessageChannel jobRequestsChannel;
	
	@Autowired
	@Qualifier("job-requests-as-string")
	private MessageChannel jobRequestsAsStringChannel;	
	
	@Autowired
	private SimpleTasklet tasklet;
	
	@Autowired
	@Qualifier("job-executions")
	private PollableChannel jobExecutionsChannel;
	
	@Before public void setUp() {
		tasklet.clear();
	}
	
	@Test public void launch() {
		Assert.assertNull(jobExecutionsChannel.receive(1));
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		jobRequestsChannel.send(
			MessageBuilder.withPayload(
				new JobLaunchRequest("simpleJob",Collections.singletonMap("date", currentDate.toString())))
				.build()
		);		
		Assert.assertEquals(currentDate.toString(),tasklet.getParameters().get("date"));
		Assert.assertNotNull(jobExecutionsChannel.receive(1));
	}
	
	@Test public void launchFromString() {
		Assert.assertNull(jobExecutionsChannel.receive(1));
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		jobRequestsAsStringChannel.send(
			MessageBuilder.withPayload("simpleJob[date="+currentDate.toString()+"]")
				.build()
		);		
		Assert.assertEquals(currentDate.toString(),tasklet.getParameters().get("date"));	
		Assert.assertNotNull(jobExecutionsChannel.receive(1));
	}
	
	public static class SimpleTasklet implements Tasklet {
		
		private Map<String, Object> parameters;
		
		@Override
		public RepeatStatus execute(StepContribution contribution,
				ChunkContext chunkContext) throws Exception {
			parameters = new HashMap<String, Object>(chunkContext.getStepContext().getJobParameters());
			return RepeatStatus.FINISHED;
		}
		
		public Map<String, Object> getParameters() {
			return parameters;
		}
		
		public void clear() {
			if(parameters != null) {
				parameters.clear();
			}
		}
	}
	
}
