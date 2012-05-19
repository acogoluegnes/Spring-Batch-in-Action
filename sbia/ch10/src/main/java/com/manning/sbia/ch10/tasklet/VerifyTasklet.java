/**
 * 
 */
package com.manning.sbia.ch10.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.manning.sbia.ch10.batch.BatchService;
import com.manning.sbia.ch10.batch.ImportMetadataHolder;

/**
 * @author acogoluegnes
 *
 */
public class VerifyTasklet implements Tasklet {
	
	private String outputDirectory;
	
	private BatchService batchService;
	
	private ImportMetadataHolder importMetadataHolder;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		batchService.verify(outputDirectory);
		importMetadataHolder.set(batchService.extractMetadata(outputDirectory));
		return RepeatStatus.FINISHED;
	}
	
	public void setBatchService(BatchService batchService) {
		this.batchService = batchService;
	}
	
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setImportMetadataHolder(
			ImportMetadataHolder importMetadataHolder) {
		this.importMetadataHolder = importMetadataHolder;
	}

}
