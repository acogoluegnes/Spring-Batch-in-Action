/**
 * 
 */
package com.manning.sbia.ch08.retry;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.policy.SimpleRetryPolicy;
import org.springframework.batch.retry.support.RetryTemplate;

/**
 * @author acogoluegnes
 *
 */
public class DiscountsWithRetryTemplateTasklet implements Tasklet {
	
	private DiscountService discountService;
	
	private DiscountsHolder discountsHolder;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);
		retryTemplate.setRetryPolicy(retryPolicy);
		List<Discount> discounts = retryTemplate.execute(
				new RetryCallback<List<Discount>>() {
			@Override
			public List<Discount> doWithRetry(RetryContext context)
					throws Exception {
				return discountService.getDiscounts();
			}
		});
		discountsHolder.setDiscounts(discounts);
		return RepeatStatus.FINISHED;
	}
	
	public void setDiscountService(DiscountService discountService) {
		this.discountService = discountService;
	}
	
	public void setDiscountsHolder(DiscountsHolder discountsHolder) {
		this.discountsHolder = discountsHolder;
	}

}
