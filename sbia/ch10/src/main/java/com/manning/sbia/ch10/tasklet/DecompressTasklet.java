/**
 * 
 */
package com.manning.sbia.ch10.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.manning.sbia.ch10.batch.BatchService;

/**
 * @author acogoluegnes
 *
 */
public class DecompressTasklet implements Tasklet {
	
	private BatchService batchService;
	
	private String inputFile;
	
	private String outputDirectory;

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		batchService.decompress(inputFile, outputDirectory);
		return RepeatStatus.FINISHED;
	}
	
	public void setBatchService(BatchService batchService) {
		this.batchService = batchService;
	}

	/**
	 * The input file to decompress.
	 * @param input
	 */
	public void setInputFile(String input) {
		this.inputFile = input;
	}

	/**
	 * The output directory where to extract the archive.
	 * @param output
	 */
	public void setOutputDirectory(String output) {
		this.outputDirectory = output;
	}
	
}
