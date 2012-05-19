/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringSchedulingTest {
	
	@Autowired
	private CountDownLatch xmlCountDownLatch;
	
	@Autowired
	private CountDownLatch annotationCountDownLatch;
	
	@Test public void xmlSpringScheduling() throws Exception {		
		Assert.assertTrue("job should have been launched several times already",xmlCountDownLatch.await(10, TimeUnit.SECONDS));
	}
	
	@Test public void annotationSpringScheduling() throws Exception {		
		Assert.assertTrue("job should have been launched several times already",annotationCountDownLatch.await(10, TimeUnit.SECONDS));
	}
	
}
