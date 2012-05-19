/**
 * 
 */
package com.manning.sbia.ch04.web;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;

/**
 * @author acogoluegnes
 * 
 */
public class WebTest {
	
	private static final String BASE_URL = "http://localhost:8089/sbiach04/joblauncher";

	private Server server;

	private CountDownLatch countDownLatch;

	private Map<String, String> jobParams;

	@Before
	public void setUp() throws Exception {
		startWebContainer();
	}

	@After
	public void tearDown() throws Exception {
		stopWebContainer();
	}

	@Test
	public void webEmbeded() throws Exception {
		Assert.assertTrue(
				"Job should have been launched to reach the count down",
				countDownLatch.await(10, TimeUnit.SECONDS));

		Assert.assertTrue(jobParams.isEmpty());
		WebConversation wc = new WebConversation();
		WebRequest req = new GetMethodWebRequest(
				BASE_URL+"?job=importProductsJob&date=20101215");
		wc.getResponse(req);
		Assert.assertFalse(jobParams.isEmpty());
		Assert.assertEquals("20101215",jobParams.get("date"));
		
		req = new GetMethodWebRequest(
				BASE_URL+"?job=importProductsJob");
		wc.getResponse(req);
		Assert.assertTrue(jobParams.isEmpty());
	}

	private void startWebContainer() throws Exception {
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8089);
		connector.setHost("127.0.0.1");
		server.addConnector(connector);

		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/sbiach04");
		wac.setWar("./src/test/resources/com/manning/sbia/ch04/web/webapp/");
		server.setHandler(wac);
		server.setStopAtShutdown(true);

		server.start();
		countDownLatch = getWebAppSpringContext(wac).getBean(
				CountDownLatch.class);
		jobParams = getWebAppSpringContext(wac).getBean("params",Map.class);
	}

	private void stopWebContainer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

	private ApplicationContext getWebAppSpringContext(WebAppContext wac) {
		ApplicationContext webAppAppCtx = WebApplicationContextUtils
				.getWebApplicationContext(wac.getServletContext());
		return webAppAppCtx;
	}

}
