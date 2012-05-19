/**
 * 
 */
package com.manning.sbia.ch12;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import net.sf.json.JSONObject;

import org.h2.Driver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.client.RestTemplate;

/**
 * @author templth
 *
 */
public class SpringBatchAdminTest extends AbstractJobStructureTest {
	private org.mortbay.jetty.Server server;
	private org.h2.tools.Server h2;
	private ClassPathXmlApplicationContext context;
	
	private static final String BASE_URL = "http://localhost:8081/springbatchadmin/";
	
	@Before
	public void setUp() throws Exception {
		startDatabase();
		startWebContainer();
		Thread.sleep(1000);
		startSpring();
	}
	

	@After
	public void tearDown() throws Exception {
		stopSpring();
		stopWebContainer();
		stopDatabase();
	}

	private void startSpring() {
		context = new ClassPathXmlApplicationContext("/com/manning/sbia/ch12/SpringBatchAdminTest-context.xml");
		jobSuccess = (Job) context.getBean("importProductsJobSuccess");
		jobFailure =  (Job) context.getBean("importProductsJobFailure");
		Map<String,JobLauncher> jobLaunchers = context.getBeansOfType(JobLauncher.class);
		if (jobLaunchers.size()==1) {
			jobLauncher = (JobLauncher)jobLaunchers.values().iterator().next();
		}
	}
	
	private void stopSpring() {
		if (context!=null) {
			context.close();
		}
	}
	
	private void stopDatabase() {
		h2.stop();
	}

	private void startWebContainer() throws Exception {
		server = new org.mortbay.jetty.Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8081);
		connector.setHost("127.0.0.1");
		server.addConnector(connector);

		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/springbatchadmin");
		wac.setWar("./src/main/webapp");
		server.setHandler(wac);
		server.setStopAtShutdown(true);

		server.start();
	}
	
	private void startDatabase() throws Exception {
		File[] files = new File(".").listFiles(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				return filename.startsWith("ch12.");
			}
		});
		for (File file : files) {
			file.delete();
		}

		h2 = org.h2.tools.Server.createTcpServer(); 
		h2.start();
		
		SingleConnectionDataSource ds = new SingleConnectionDataSource(
			"jdbc:h2:tcp://localhost/ch12",
			"sa",
			"",
			true
		);
		ds.setDriverClassName(Driver.class.getName());
		ds.initConnection();
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-drop-h2.sql"));
		populator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-h2.sql"));
		populator.populate(ds.getConnection());
	}
	
	private void stopWebContainer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}
	
	@Test public void springBatchAdminWithSuccess() throws Exception {
		launchSuccessJob();

		String jobName = "importProductsJobSuccess";
		RestTemplate tpl = new RestTemplate();
		ResponseEntity<String> resp = tpl.getForEntity(BASE_URL, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		Assert.assertTrue(resp.getHeaders().getContentType().isCompatibleWith(MediaType.TEXT_HTML));
		Assert.assertTrue(resp.getBody().contains("Spring Batch Admin"));
		
		resp = tpl.getForEntity(BASE_URL+"jobs/"+jobName+".json", String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		
		JSONObject jsonObject = JSONObject.fromObject(resp.getBody());
		JSONObject job = jsonObject.getJSONObject("job");
		JSONObject jobInstances = job.getJSONObject("jobInstances");
		Assert.assertEquals(1, jobInstances.size());
		
		JSONObject jobInstance = (JSONObject) jobInstances.values().iterator().next();
		String executionsUrl = jobInstance.getString("resource");
		
		resp = tpl.getForEntity(executionsUrl, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());

		JSONObject executions = JSONObject.fromObject(resp.getBody());
		JSONObject jobExecutions = executions.getJSONObject("jobExecutions");
		Assert.assertEquals(1, jobExecutions.size());

		JSONObject execution = (JSONObject) jobExecutions.values().iterator().next();
		String executionUrl = execution.getString("resource");

		resp = tpl.getForEntity(executionUrl, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());

		execution = JSONObject.fromObject(resp.getBody());
		execution = execution.getJSONObject("jobExecution");
		Assert.assertEquals("COMPLETED", execution.getString("exitCode"));
		JSONObject stepExecutions = (JSONObject) execution.get("stepExecutions");
		Assert.assertEquals(1, stepExecutions.size());
		String stepName = (String) stepExecutions.keys().next();
		Assert.assertEquals("readWriteSuccess", stepName);
		JSONObject stepExecution = (JSONObject) stepExecutions.get(stepName);
		Assert.assertEquals("COMPLETED", stepExecution.getString("status"));
		Assert.assertEquals("COMPLETED", stepExecution.getString("exitCode"));
		Assert.assertEquals(8, stepExecution.getInt("readCount"));
		Assert.assertEquals(8, stepExecution.getInt("writeCount"));
		Assert.assertEquals(1, stepExecution.getInt("commitCount"));
		Assert.assertEquals(0, stepExecution.getInt("rollbackCount"));
	}
	
	@Test public void springBatchAdminWithFailure() throws Exception {
		launchFailureJob();

		String jobName = "importProductsJobFailure";
		RestTemplate tpl = new RestTemplate();
		ResponseEntity<String> resp = tpl.getForEntity(BASE_URL, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		Assert.assertTrue(resp.getHeaders().getContentType().isCompatibleWith(MediaType.TEXT_HTML));
		Assert.assertTrue(resp.getBody().contains("Spring Batch Admin"));
		
		resp = tpl.getForEntity(BASE_URL+"jobs/"+jobName+".json", String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		
		JSONObject jsonObject = JSONObject.fromObject(resp.getBody());
		JSONObject job = jsonObject.getJSONObject("job");
		JSONObject jobInstances = job.getJSONObject("jobInstances");
		Assert.assertEquals(1, jobInstances.size());
		
		JSONObject jobInstance = (JSONObject) jobInstances.values().iterator().next();
		String executionsUrl = jobInstance.getString("resource");
		
		resp = tpl.getForEntity(executionsUrl, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());

		JSONObject executions = JSONObject.fromObject(resp.getBody());
		JSONObject jobExecutions = executions.getJSONObject("jobExecutions");
		Assert.assertEquals(1, jobExecutions.size());

		JSONObject execution = (JSONObject) jobExecutions.values().iterator().next();
		String executionUrl = execution.getString("resource");

		resp = tpl.getForEntity(executionUrl, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());

		execution = JSONObject.fromObject(resp.getBody());
		execution = execution.getJSONObject("jobExecution");
		Assert.assertEquals("FAILED", execution.getString("exitCode"));
		JSONObject stepExecutions = execution.getJSONObject("stepExecutions");
		Assert.assertEquals(1, stepExecutions.size());
		String stepName = (String) stepExecutions.keys().next();
		Assert.assertEquals("readWriteFailure", stepName);
		JSONObject stepExecution = stepExecutions.getJSONObject(stepName);
		Assert.assertEquals("FAILED", stepExecution.getString("status"));
		Assert.assertEquals("FAILED", stepExecution.getString("exitCode"));
		Assert.assertEquals(0, stepExecution.getInt("readCount"));
		Assert.assertEquals(0, stepExecution.getInt("writeCount"));
		Assert.assertEquals(0, stepExecution.getInt("commitCount"));
		Assert.assertEquals(1, stepExecution.getInt("rollbackCount"));
	}
	
}
