/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author acogoluegnes
 *
 */
public class QuartzSchedulingTest {
	
	@Test public void springScheduling() throws Exception {	
		// doesn't seem to work with test context framework
		// quartz complains about not finding a database table,
		// perhaps the quartz thread is still working whereas the in-memory datasource is closed? 
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/manning/sbia/ch04/QuartzSchedulingTest-context.xml");
		CountDownLatch countDownLatch = ctx.getBean(CountDownLatch.class);		
		Assert.assertTrue("job should have been launched several times already",countDownLatch.await(10, TimeUnit.SECONDS));
		ctx.close();
	}
	
}
