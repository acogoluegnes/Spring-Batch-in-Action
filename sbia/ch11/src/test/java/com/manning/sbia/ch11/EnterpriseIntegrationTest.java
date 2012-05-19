/**
 * 
 */
package com.manning.sbia.ch11;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.batch.core.BatchStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.manning.sbia.ch11.integration.ProductImport;

/**
 * @author acogoluegnes
 * 
 */
public class EnterpriseIntegrationTest {

	private static final String BASE_URI = "http://localhost:8080/enterpriseintegration/";

	private JdbcTemplate jdbcTemplate;
	
	private PollableChannel receiverChannel;
	
	private RestTemplate restTemplate;

	private Server server;

	@Before
	public void setUp() throws Exception {
		startWebContainer();
		jdbcTemplate.update("delete from product");
	}

	@After
	public void tearDown() throws Exception {
		stopWebContainer();
	}

	@Test
	public void enterpriseIntegration() throws Exception {
		assertPreConditions();
		String importId = "partner1-1";
		restTemplate.put(BASE_URI + "product-imports/{importId}",
				loadProductFiles(importId), importId);
		extractMessage(Object.class);
		checkProductImportTableCount(1);		
		checkProductTableCount(1);
		ProductImport productImport = restTemplate.getForObject(BASE_URI + "product-imports/{importId}", ProductImport.class,importId);
		Assert.assertEquals(importId, productImport.getImportId());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), productImport.getState());
				
		importId = "partner1-2";
		restTemplate.put(BASE_URI + "product-imports/{importId}",
				loadProductFiles(importId), importId);
		extractMessage(Object.class);
		checkProductImportTableCount(2);
		checkProductTableCount(5);
		productImport = restTemplate.getForObject(BASE_URI + "product-imports/{importId}", ProductImport.class,importId);
		Assert.assertEquals(importId, productImport.getImportId());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), productImport.getState());
		
		importId = "partner1-3";
		restTemplate.put(BASE_URI + "product-imports/{importId}",
				loadProductFiles(importId), importId);
		extractMessage(Object.class);
		checkProductImportTableCount(3);
		checkProductTableCount(8);
		productImport = restTemplate.getForObject(BASE_URI + "product-imports/{importId}", ProductImport.class,importId);
		Assert.assertEquals(importId, productImport.getImportId());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), productImport.getState());
		
		// try to re-submit
		try {
			restTemplate.put(BASE_URI + "product-imports/{importId}",
					loadProductFiles(importId), importId);
		} catch (ResourceAccessException e) {
			Assert.assertTrue(e.getMessage().contains(
				String.valueOf(HttpStatus.CONFLICT.value())
			));
		}
		
		// try to access to non-existing import
		try {
			restTemplate.put(BASE_URI + "product-imports/{importId}",
					loadProductFiles(importId), "does-not-exist");
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.NOT_FOUND,e.getStatusCode());
		}
		
		// check pending status (import exists but no corresponding job instance)
		// creates fake product_import
		importId = "fake-import";
		jdbcTemplate.update("insert into product_import (import_id) values (?)","fake-import");
		productImport = restTemplate.getForObject(BASE_URI + "product-imports/{importId}", ProductImport.class,importId);
		Assert.assertEquals(importId, productImport.getImportId());
		Assert.assertEquals("PENDING", productImport.getState());
		
		
	}	

	private void checkProductImportTableCount(int expected) {
		Assert.assertEquals(expected, jdbcTemplate.queryForInt("select count(1) from product_import"));
	}
	
	private void checkProductTableCount(int expected) {
		Assert.assertEquals(expected, jdbcTemplate.queryForInt("select count(1) from product"));
	}

	private void assertPreConditions() {
		checkProductImportTableCount(0);		
		checkProductTableCount(0);
	}

	private String loadProductFiles(String importId) throws Exception {
		return FileUtils
				.readFileToString(
						new File(
								new File(
										"./src/test/resources/com/manning/sbia/ch11/product-import-samples"),
								"products-" + importId + ".xml"), "UTF-8");
	}

	private DataSource getWebAppDataSource(WebAppContext wac) {
		ApplicationContext webAppAppCtx = getWebAppSpringContext(wac);
		Assert.assertNotNull(webAppAppCtx);
		return webAppAppCtx.getBean(DataSource.class);
	}

	private ApplicationContext getWebAppSpringContext(WebAppContext wac) {
		ApplicationContext webAppAppCtx = WebApplicationContextUtils
				.getWebApplicationContext(wac.getServletContext());
		return webAppAppCtx;
	}

	private void setUpSpringBeans(WebAppContext wac) {
		setUpJdbcTemplate(wac);
		setUpReceiverChannel(wac);
		setUpRestTemplate(wac);
	}
	
	private void setUpRestTemplate(WebAppContext wac) {
		this.restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new StringHttpMessageConverter());
		MarshallingHttpMessageConverter marshallingConverter = new MarshallingHttpMessageConverter(
				getWebAppSpringContext(wac).getBean(Marshaller.class)
		);
		converters.add(marshallingConverter);
		restTemplate.setMessageConverters(converters);
	}
	
	private void setUpJdbcTemplate(WebAppContext wac) {
		this.jdbcTemplate = new JdbcTemplate(getWebAppDataSource(wac));
	}
	
	private void setUpReceiverChannel(WebAppContext wac) {
		this.receiverChannel = getWebAppSpringContext(wac)
			.getBean("job-executions", PollableChannel.class);
	}

	private void startWebContainer() throws Exception {
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(8080);
		connector.setHost("127.0.0.1");
		server.addConnector(connector);

		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/enterpriseintegration");
		wac.setWar("./src/main/webapp");
		server.setHandler(wac);
		server.setStopAtShutdown(true);

		server.start();
		setUpSpringBeans(wac);
	}

	private void stopWebContainer() throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}
	
	private <T> Message<T> extractMessage(Class<T> payloadClass) {
		Message<?> message = receiverChannel.receive(1000L);
		Assert.assertNotNull(message);
		Assert.assertTrue(payloadClass.isAssignableFrom(message.getPayload().getClass()));
		return (Message<T>) message;
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		System.setProperty("product.import.pickup.dir",
				LaunchEnterpriseIntegrationServer.PICKUP_DIR);
		File pickUpDir = new File(LaunchEnterpriseIntegrationServer.PICKUP_DIR);
		if (pickUpDir.exists()) {
			FileUtils.cleanDirectory(pickUpDir);
		}
	}

}
