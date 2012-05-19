/**
 * 
 */
package com.manning.sbia.ch09.besteffort;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import junit.framework.Assert;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch09.domain.Order;
import com.manning.sbia.ch09.domain.OrderItem;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DetectDuplicateTest {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job updateInventoryJobWithMockReader;
	
	@Autowired
	private Job updateInventoryJob;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired 
	private QueueViewMBean orderQueueView;
	
	@Autowired
	private ItemReader<Message> mockReader;
	

	@Test public void noDuplicate() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		prepareQueue();
		Assert.assertEquals(13,orderQueueView.getQueueSize());
		JobExecution exec = jobLauncher.run(updateInventoryJob, jobParameters);
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		assertInventoryOk();
		Assert.assertEquals(0,orderQueueView.getQueueSize());
	}
	
	@Test public void simulateDuplicate() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		
		ObjectMessage redeliveredNotProcessed = Mockito.mock(ObjectMessage.class);
		ObjectMessage redeliveredProcessed = Mockito.mock(ObjectMessage.class);
		
		when(redeliveredNotProcessed.getJMSRedelivered())
			.thenReturn(true);
		when(redeliveredNotProcessed.getObject())
			.thenReturn(createOrder("3", createItem("3")));
			
		when(redeliveredProcessed.getJMSRedelivered())
			.thenReturn(true);
		when(redeliveredProcessed.getObject())
			.thenReturn(createOrder("14", createItem("14")));
		
		when(mockReader.read())
			.thenReturn(redeliveredProcessed)
			.thenReturn(redeliveredNotProcessed)
			.thenReturn(null);
		
		JobExecution exec = jobLauncher.run(updateInventoryJobWithMockReader, jobParameters);
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		assertInventoryOk();
		Assert.assertEquals(50-1,getProductQuantity("14"));
	}
	
	private void prepareQueue() {
		emptyQueue();		
		fillInQueue();
	}

	private void fillInQueue() {
		jmsTemplate.convertAndSend(
			createOrder("1", createItem("1"),createItem("2",(short) 2))
		);
		jmsTemplate.convertAndSend(
			createOrder("2", createItem("1",(short) 2))
		);
		jmsTemplate.convertAndSend(
			createOrder("3", createItem("3"))
		);
		jmsTemplate.convertAndSend(
			createOrder("4", createItem("4"))
		);
		jmsTemplate.convertAndSend(
			createOrder("5", createItem("5"))
		);
		jmsTemplate.convertAndSend(
			createOrder("6", createItem("6"))
		);
		jmsTemplate.convertAndSend(
			createOrder("7", createItem("7"))
		);
		jmsTemplate.convertAndSend(
			createOrder("8", createItem("8"))
		);
		jmsTemplate.convertAndSend(
			createOrder("9", createItem("9"))
		);
		jmsTemplate.convertAndSend(
			createOrder("10", createItem("10"))
		);
		jmsTemplate.convertAndSend(
			createOrder("11", createItem("11"))
		);
		jmsTemplate.convertAndSend(
			createOrder("12", createItem("12"))
		);
		jmsTemplate.convertAndSend(
			createOrder("13", createItem("13"))
		);
	}

	private void emptyQueue() {
		while(jmsTemplate.receive() != null) { }
	}
	
	private void assertInventoryOk() {
		int initialCount = 50;
		Assert.assertEquals(initialCount-3, getProductQuantity("1"));
		Assert.assertEquals(initialCount-2, getProductQuantity("2"));
		Assert.assertEquals(initialCount-1, getProductQuantity("3"));
		Assert.assertEquals(initialCount-1, getProductQuantity("4"));
		Assert.assertEquals(initialCount-1, getProductQuantity("5"));
		Assert.assertEquals(initialCount-1, getProductQuantity("6"));
		Assert.assertEquals(initialCount-1, getProductQuantity("7"));
		Assert.assertEquals(initialCount-1, getProductQuantity("8"));
		Assert.assertEquals(initialCount-1, getProductQuantity("9"));
		Assert.assertEquals(initialCount-1, getProductQuantity("10"));
		Assert.assertEquals(initialCount-1, getProductQuantity("11"));
		Assert.assertEquals(initialCount-1, getProductQuantity("12"));
		Assert.assertEquals(initialCount-1, getProductQuantity("13"));
	}
	
	private int getProductQuantity(String productId) {
		return jdbcTemplate.queryForInt("select quantity from inventory where product_id = ?",productId);
	}
	
	private Order createOrder(String orderId,OrderItem ... items) {
		return new Order(orderId, Arrays.asList(items));
	}
	
	private OrderItem createItem(String productId,short quantity) {
		return new OrderItem(productId, quantity);
	}
	
	private OrderItem createItem(String productId) {
		return createItem(productId, (short) 1);
	}
	
}
