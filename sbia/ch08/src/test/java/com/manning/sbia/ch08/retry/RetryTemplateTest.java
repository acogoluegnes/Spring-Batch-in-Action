/**
 * 
 */
package com.manning.sbia.ch08.retry;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch08.retry.Discount;
import com.manning.sbia.ch08.retry.DiscountService;
import com.manning.sbia.ch08.retry.DiscountsHolder;
import com.manning.sbia.ch08.retry.DiscountsTasklet;
import com.manning.sbia.ch08.retry.DiscountsWithRetryTemplateTasklet;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class RetryTemplateTest {

	@Test public void discountTasketWithRetryTemplateOk() throws Exception {
		DiscountsWithRetryTemplateTasklet tasklet = new DiscountsWithRetryTemplateTasklet();
		DiscountService service = mock(DiscountService.class);
		DiscountsHolder holder = new DiscountsHolder();
		tasklet.setDiscountService(service);
		tasklet.setDiscountsHolder(holder);
		
		List<Discount> discounts = new ArrayList<Discount>();
		discounts.add(new Discount());
		
		when(service.getDiscounts())
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenReturn(discounts);
		
		RepeatStatus status = tasklet.execute(null, null);
		Assert.assertEquals(RepeatStatus.FINISHED, status);
		Assert.assertSame(discounts, holder.getDiscounts());
			
	}
	
	@Test public void discountTasketWithRetryTemplateRetryExhausted() throws Exception {
		DiscountsWithRetryTemplateTasklet tasklet = new DiscountsWithRetryTemplateTasklet();
		DiscountService service = mock(DiscountService.class);
		DiscountsHolder holder = new DiscountsHolder();
		tasklet.setDiscountService(service);
		tasklet.setDiscountsHolder(holder);
		
		List<Discount> discounts = new ArrayList<Discount>();
		discounts.add(new Discount());
		
		when(service.getDiscounts())
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenReturn(discounts);
		
		try {
			tasklet.execute(null, null);
			Assert.fail();
		} catch (TransientDataAccessResourceException e) {
			// OK
		}
	}
	
	@Autowired
	private DiscountsTasklet discountsTasklet;
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	private DiscountsHolder discountsHolder;
	
	@Test public void transparentRetry() throws Exception {
		List<Discount> discounts = new ArrayList<Discount>();
		discounts.add(new Discount());
		
		when(discountService.getDiscounts())
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenReturn(discounts)
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenThrow(new TransientDataAccessResourceException(""))
			.thenReturn(discounts);
		
		RepeatStatus status = discountsTasklet.execute(null, null);
		Assert.assertEquals(RepeatStatus.FINISHED, status);
		Assert.assertSame(discounts, discountsHolder.getDiscounts());
		
		try {
			discountsTasklet.execute(null, null);
			Assert.fail();
		} catch (TransientDataAccessResourceException e) {
			// OK
		}
	}
	
}
