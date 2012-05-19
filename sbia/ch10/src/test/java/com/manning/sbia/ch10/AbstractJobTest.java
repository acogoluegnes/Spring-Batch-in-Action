/**
 * 
 */
package com.manning.sbia.ch10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch10.batch.BatchService;
import com.manning.sbia.ch10.batch.ImportMetadata;
import com.manning.sbia.ch10.batch.ImportMetadataHolder;

/**
 * @author acogoluegnes
 * @todo check number of job executions
 */
public abstract class AbstractJobTest {

	@Autowired
	protected BatchService batchService;
	
	@Autowired
	protected JobLauncher jobLauncher;
	
	@Autowired
	protected Job importProductsJob;
	
	@Autowired
	protected Tasklet readWriteProductsTasklet;
	
	@Autowired
	protected Tasklet indexTasklet;
	
	@Autowired
	protected ImportMetadataHolder importMetadataHolder;
	
	protected ImportMetadata metadata;
	
	protected String archiveFile = "input.zip";
	protected String workingDirectory = "/tmp";
	
	protected int getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems() {
		return 7;
	}
	
	protected int getExpectedNbStepExecutionsDownloadedFileOkSkippedItems() {
		return 8;
	}
	
	protected int getExpectedNbStepExecutionsNoDownloadedFile() {
		return 1;
	}
	
	protected int getExpectedNbOfInvocationFileExists() {
		return 1;
	}
	
	@Before public void setUp() {
		reset(readWriteProductsTasklet,indexTasklet,batchService);
		importMetadataHolder.set(null);
		metadata = new ImportMetadata();
	}

	@Test public void importProductsDownloadedFileOkNoSkippedItems() throws Exception {
		metadata.setImportId("1");		
		assertPreConditionsDownloadedFileOkNoSkippedItems();
		recordBehaviorDownloadedFileOkNoSkippedItems();
		
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("archiveFile", archiveFile)
			.addString("workingDirectory",workingDirectory)
			.addLong("date",System.currentTimeMillis()).toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(importProductsJob, jobParameters);
		assertPostConditionsDownloadFileOkNoSkippedItems(jobExecution);
	}
	
	protected void extraAssertionsDowlnloadedFileOkNoSkippedItems() throws Exception { }
	
	private void assertPreConditionsDownloadedFileOkNoSkippedItems() {
		assertNull(importMetadataHolder.get());
	}

	private void recordBehaviorDownloadedFileOkNoSkippedItems()
			throws Exception {
		when(batchService.extractMetadata(workingDirectory))
			.thenReturn(metadata);		
		when(batchService.exists(archiveFile))
			.thenReturn(true);
		
		when(readWriteProductsTasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
			.thenReturn(RepeatStatus.FINISHED);
	}
	
	private void assertPostConditionsDownloadFileOkNoSkippedItems(
			JobExecution jobExecution) throws Exception {
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems(),jobExecution.getStepExecutions().size());
		
		assertMetadataDownloadFileOkNoSkippedItems();
		
		verify(batchService,times(1)).download(archiveFile);
		verify(batchService,times(getExpectedNbOfInvocationFileExists())).exists(archiveFile);
		verify(batchService,times(1)).decompress(archiveFile, workingDirectory);
		verify(batchService,times(1)).verify(workingDirectory);
		verify(batchService,times(1)).extractMetadata(workingDirectory);
		verify(readWriteProductsTasklet,times(1)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(0)).generateReport(any(JobExecution.class));
		verify(indexTasklet,times(1)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(1)).track(metadata.getImportId());
		verify(batchService,times(1)).clean(workingDirectory);
		extraAssertionsDowlnloadedFileOkNoSkippedItems();
	}

	protected void assertMetadataDownloadFileOkNoSkippedItems() {
		assertSame(metadata, importMetadataHolder.get());
	}
	
	@Test public void importProductsDownloadedFileOkSkippedItems() throws Exception {
		metadata.setImportId("1");		
		assertPreConditionsDownloadedFileOkSkippedItems();
		recordBehaviorDownloadedFileOkSkippedItems();
		
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("archiveFile", archiveFile)
			.addString("workingDirectory",workingDirectory)
			.addLong("date",System.currentTimeMillis()).toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(importProductsJob, jobParameters);
		assertPostConditionsDownloadFileOkSkippedItems(jobExecution);
	}
	
	protected void extraAssertionsDowlnloadedFileOkSkippedItems() throws Exception { }
	
	private void assertPreConditionsDownloadedFileOkSkippedItems() {
		assertNull(importMetadataHolder.get());
	}

	private void recordBehaviorDownloadedFileOkSkippedItems()
			throws Exception {
		when(batchService.extractMetadata(workingDirectory))
			.thenReturn(metadata);		
		when(batchService.exists(archiveFile))
			.thenReturn(true);
		
		when(readWriteProductsTasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
			.thenAnswer(new Answer<RepeatStatus>() {
				@Override
				public RepeatStatus answer(InvocationOnMock invocation)
						throws Throwable {
					StepContribution contribution = (StepContribution) invocation.getArguments()[0];
					contribution.incrementReadSkipCount();
					return RepeatStatus.FINISHED;
				}
			});
	}
	
	@Autowired
	private DataSource dataSource;
	
	private void assertPostConditionsDownloadFileOkSkippedItems(
			JobExecution jobExecution) throws Exception {
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(getExpectedNbStepExecutionsDownloadedFileOkSkippedItems(),jobExecution.getStepExecutions().size());
		JdbcTemplate tpl = new JdbcTemplate(dataSource);
		assertMetadataDownloadFileOkSkippedItems();
		
		verify(batchService,times(1)).download(archiveFile);
		verify(batchService,times(getExpectedNbOfInvocationFileExists())).exists(archiveFile);
		verify(batchService,times(1)).decompress(archiveFile, workingDirectory);
		verify(batchService,times(1)).verify(workingDirectory);
		verify(batchService,times(1)).extractMetadata(workingDirectory);
		verify(readWriteProductsTasklet,times(1)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(1)).generateReport(any(JobExecution.class));
		verify(indexTasklet,times(1)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(1)).track(metadata.getImportId());
		verify(batchService,times(1)).clean(workingDirectory);
		extraAssertionsDowlnloadedFileOkSkippedItems();
	}
	
	protected void assertMetadataDownloadFileOkSkippedItems() {
		assertSame(metadata, importMetadataHolder.get());
	}
	
	@Test public void importProductsNoDownloadedFile() throws Exception {
		metadata.setImportId("1");		
		assertPreConditionsNoDownloadedFile();
		recordBehaviorNoDownloadedFile();
		
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("archiveFile", archiveFile)
			.addString("workingDirectory",workingDirectory)
			.addLong("date",System.currentTimeMillis()).toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(importProductsJob, jobParameters);
		assertPostConditionsNoDownloadedFile(jobExecution);
	}
	
	protected void extraAssertionsNoDownloadedFile() throws Exception { }
	
	private void assertPreConditionsNoDownloadedFile() {
		assertNull(importMetadataHolder.get());
	}
	
	private void recordBehaviorNoDownloadedFile() throws Exception {
		when(batchService.extractMetadata(workingDirectory))
			.thenReturn(metadata);		
		when(batchService.exists(archiveFile))
			.thenReturn(false);
	}
	
	private void assertPostConditionsNoDownloadedFile(JobExecution jobExecution) throws Exception{
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(getExpectedNbStepExecutionsNoDownloadedFile(),jobExecution.getStepExecutions().size());
		
		assertMetadataNoDownloadFile();
		
		
		verify(batchService,times(1)).download(archiveFile);
		verify(batchService,times(getExpectedNbOfInvocationFileExists())).exists(archiveFile);
		verify(batchService,times(0)).decompress(archiveFile, workingDirectory);
		verify(batchService,times(0)).verify(workingDirectory);
		verify(batchService,times(0)).extractMetadata(workingDirectory);
		verify(readWriteProductsTasklet,times(0)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(0)).generateReport(any(JobExecution.class));
		verify(indexTasklet,times(0)).execute(any(StepContribution.class), any(ChunkContext.class));
		verify(batchService,times(0)).track(metadata.getImportId());
		verify(batchService,times(0)).clean(workingDirectory);
		
		extraAssertionsNoDownloadedFile();
	}

	protected void assertMetadataNoDownloadFile() {
		assertNull(importMetadataHolder.get());
	}
	
}
