/**
 * 
 */
package com.manning.sbia.ch11.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xml.sax.SAXException;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;

/**
 * @author acogoluegnes
 * 
 */
public class HttpLaunchingJobTest {
	
	private static final String JOB_NAME = "simpleJob";
	private static final long RECEIVING_TIMEOUT = 1000;
	private static final int PORT = 8085;

	@Test
	public void httpLaunch() throws Exception {
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(PORT);
		connector.setHost("127.0.0.1");
		server.addConnector(connector);

		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/sbiahttplaunch");
		wac.setDescriptor("./src/test/resources/com/manning/sbia/ch11/integration/web.xml");
		wac.setWar("./src/test/resources/com/manning/sbia/ch11/integration/");
		server.setHandler(wac);
		server.setStopAtShutdown(true);

		try {
			server.start();
			submitJob(JOB_NAME);
			
			ApplicationContext webAppAppCtx = WebApplicationContextUtils.getWebApplicationContext(wac.getServletContext());
			Assert.assertNotNull(webAppAppCtx);			
			JobExecution jobExecution = receiveAndConvertMessage(webAppAppCtx);
			
			Assert.assertEquals(JOB_NAME,jobExecution.getJobInstance().getJobName());
			Assert.assertTrue(jobExecution.getJobInstance().getJobParameters().isEmpty());

			submitJob(JOB_NAME+"[date=2010-07-23]");
			
			jobExecution = receiveAndConvertMessage(webAppAppCtx);
			Assert.assertEquals(JOB_NAME,jobExecution.getJobInstance().getJobName());
			Assert.assertEquals("2010-07-23",jobExecution.getJobInstance().getJobParameters().getString("date"));

		} finally {
			server.stop();
		}

	}

	private JobExecution receiveAndConvertMessage(ApplicationContext webAppAppCtx) {
		PollableChannel jobExecutionsChannel = webAppAppCtx.getBean("job-executions", PollableChannel.class);	
		
		Message<?> message = jobExecutionsChannel.receive(RECEIVING_TIMEOUT);
		Assert.assertNotNull("No message on the job execution queue!",message);
		Assert.assertEquals(JobExecution.class,message.getPayload().getClass());
		JobExecution jobExecution = (JobExecution) message.getPayload();
		return jobExecution;
	}

	private void submitJob(String jobString) throws MalformedURLException, IOException,
			SAXException {
		WebConversation wc = new WebConversation();
		WebRequest req = new PostMethodWebRequest(
			"http://localhost:"+PORT+"/sbiahttplaunch/job-requests",
			new ByteArrayInputStream(jobString.getBytes()),
			"text/plain"
		);		
		wc.getResponse(req);
	}

}
