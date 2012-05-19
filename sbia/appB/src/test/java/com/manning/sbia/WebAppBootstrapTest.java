/**
 * 
 */
package com.manning.sbia;

import org.h2.Driver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;
import org.springframework.batch.core.BatchStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.client.RestTemplate;

/**
 * @author acogoluegnes
 *
 */
public class WebAppBootstrapTest {

	private Server server;
	
	private org.h2.tools.Server h2;
	
	private static final String BASE_URL = "http://localhost:8081/springbatchadmin/";
	
	private static final String JOB_NAME = "sbiaJob";
	
	@Before
	public void setUp() throws Exception {
		startDatabase();
		startWebContainer();
	}
	

	@Test public void webAppBootstrap() throws Exception {
		RestTemplate tpl = new RestTemplate();
		ResponseEntity<String> resp = tpl.getForEntity(BASE_URL, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		Assert.assertTrue(resp.getHeaders().getContentType().isCompatibleWith(MediaType.TEXT_HTML));
		Assert.assertTrue(resp.getBody().contains("Spring Batch Admin"));
		
		resp = tpl.getForEntity(BASE_URL+"jobs", String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		Assert.assertTrue(resp.getBody().contains(JOB_NAME));
		
		resp = tpl.getForEntity(BASE_URL+"jobs/"+JOB_NAME, String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		
		tpl.postForLocation(BASE_URL+"jobs/"+JOB_NAME,null);
		
		// need to wait a little
		Thread.sleep(5000L);
		
		resp = tpl.getForEntity(BASE_URL+"jobs/executions", String.class);
		Assert.assertEquals(HttpStatus.OK,resp.getStatusCode());
		Assert.assertTrue(resp.getBody().contains(JOB_NAME));
		Assert.assertTrue(resp.getBody().contains(BatchStatus.COMPLETED.toString()));
	}
	
	@After
	public void tearDown() throws Exception {
		stopWebContainer();
		stopDatabase();
	}

	private void stopDatabase() {
		h2.stop();
	}


	private void startWebContainer() throws Exception {
		XmlConfiguration configuration = new XmlConfiguration(
				getClass().getResourceAsStream("/com/manning/sbia/jetty.xml")
		);
		server = new Server();
		configuration.configure(server);

		server.start();
	}
	
	private void startDatabase() throws Exception {
		h2 = org.h2.tools.Server.createTcpServer(); 
		h2.start();
		
		SingleConnectionDataSource ds = new SingleConnectionDataSource(
			"jdbc:h2:tcp://localhost/sbia_appB",
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
	
	
	
}
