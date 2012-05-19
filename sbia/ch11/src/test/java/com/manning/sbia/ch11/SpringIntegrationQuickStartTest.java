/**
 * 
 */
package com.manning.sbia.ch11;

import java.util.Collections;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import com.manning.sbia.ch11.integration.JobLaunchRequest;

/**
 * @author acogoluegnes
 * 
 */
public class SpringIntegrationQuickStartTest {

	@Test
	public void SpringIntegrationQuickStart() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				SpringIntegrationQuickStartTest.class.getSimpleName()+"-context.xml",
				SpringIntegrationQuickStartTest.class);
		JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(
			"echoJob",Collections.singletonMap("param1", "value1"));
		Message<JobLaunchRequest> msg = MessageBuilder
			.withPayload(jobLaunchRequest).build();
		MessageChannel jobRequestsChannel = ctx.getBean("job-requests",MessageChannel.class);
		jobRequestsChannel.send(msg);
	}

}
