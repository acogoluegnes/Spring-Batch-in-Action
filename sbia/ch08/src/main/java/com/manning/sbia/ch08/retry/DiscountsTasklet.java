/**
 * 
 */
package com.manning.sbia.ch08.retry;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author acogoluegnes
 *
 */
public class DiscountsTasklet implements Tasklet {
	
	private DiscountService discountService;
	
	private DiscountsHolder discountsHolder;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		List<Discount> discounts = discountService.getDiscounts();
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
