/**
 * 
 */
package com.manning.sbia.ch09.besteffort;

import java.util.Collections;

import junit.framework.Assert;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch09.domain.Order;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IdempotencyTest {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired 
	private QueueViewMBean shippedOrderQueueView;
	
	@Test public void idempotency() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		prepareQueue();
		Assert.assertEquals(14,shippedOrderQueueView.getQueueSize());
		assertOrderState(false);
		JobExecution exec = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		assertOrderState(true);
		Assert.assertEquals(0,shippedOrderQueueView.getQueueSize());
		
		jobParameters = new JobParametersBuilder()
			.addLong("date", System.currentTimeMillis())
			.toJobParameters();
		prepareQueue();
		Assert.assertEquals(14,shippedOrderQueueView.getQueueSize());
		exec = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(BatchStatus.COMPLETED,exec.getStatus());
		assertOrderState(true);
		Assert.assertEquals(0,shippedOrderQueueView.getQueueSize());
	}
	
	private void prepareQueue() {
		emptyQueue();		
		fillInQueue();
	}

	private void fillInQueue() {
		for(int i=1;i<=14;i++) {
			jmsTemplate.convertAndSend(
				new Order(String.valueOf(i), Collections.EMPTY_LIST)
			);
		}
	}

	private void emptyQueue() {
		while(jmsTemplate.receive() != null) { }
	}
	
	private void assertOrderState(boolean shipped) {
		int countShipped = jdbcTemplate.queryForInt("select count(1) from orders where shipped = ?",
				shipped
		);
		int total = jdbcTemplate.queryForInt("select count(1) from orders");
		Assert.assertTrue(total == countShipped);
	}
	
	private int getProductQuantity(String productId) {
		return jdbcTemplate.queryForInt("select quantity from inventory where product_id = ?",productId);
	}
	
}
