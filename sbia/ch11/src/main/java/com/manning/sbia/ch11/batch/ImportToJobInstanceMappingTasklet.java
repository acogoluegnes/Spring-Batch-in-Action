/**
 * 
 */
package com.manning.sbia.ch11.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.manning.sbia.ch11.repository.ProductImportRepository;

/**
 * @author acogoluegnes
 *
 */
public class ImportToJobInstanceMappingTasklet implements Tasklet,InitializingBean {
	
	private String productImportId;
	
	private ProductImportRepository productImportRepository;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		Long jobInstanceId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getId();
		productImportRepository.mapImportToJobInstance(productImportId, jobInstanceId);
		return RepeatStatus.FINISHED;
	}
	
	public void setProductImportId(String productImportId) {
		this.productImportId = productImportId;
	}
	
	public void setProductImportRepository(
			ProductImportRepository productImportRepository) {
		this.productImportRepository = productImportRepository;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(productImportId);		
	}

}
