/**
 * 
 */
package com.manning.sbia.ch08.restart;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class RestartTest {

	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private ItemWriter<File> writer;

	private JobParameters jobParameters = new JobParametersBuilder().addLong(
			"date", System.currentTimeMillis()).toJobParameters();

	@Test
	public void restart() throws Exception {
		doNothing().doThrow(new RuntimeException()).when(writer)
				.write(anyList());

		JobExecution exec = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(ExitStatus.FAILED, exec.getExitStatus());

		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		verify(writer, times(2)).write(captor.capture());
		List<List> files = captor.getAllValues();
		List<?> written = files.get(0);
		Assert.assertEquals(3, written.size());
		List<?> rolledback = files.get(1);
		Assert.assertEquals(3, rolledback.size());
		Assert.assertEquals("[01, 02, 03]", extractFilenames(written)
				.toString());
		Assert.assertEquals("[04, 05, 06]", extractFilenames(rolledback)
				.toString());

		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		System.out.println(stepExec.getExecutionContext());

		reset(writer);
		
		doNothing().doThrow(new RuntimeException()).when(writer)
				.write(anyList());

		exec = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(ExitStatus.FAILED, exec.getExitStatus());

		captor = ArgumentCaptor.forClass(List.class);
		verify(writer, times(2)).write(captor.capture());
		files = captor.getAllValues();
		written = files.get(0);
		Assert.assertEquals(3, written.size());
		rolledback = files.get(1);
		Assert.assertEquals(3, rolledback.size());
		Assert.assertEquals("[04, 05, 06]", extractFilenames(written)
				.toString());
		Assert.assertEquals("[07, 08, 09]", extractFilenames(rolledback)
				.toString());
		
		reset(writer);
		doNothing().doNothing().when(writer).write(anyList());
		
		exec = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, exec.getExitStatus());

		captor = ArgumentCaptor.forClass(List.class);
		verify(writer, times(2)).write(captor.capture());
		files = captor.getAllValues();
		written = files.get(0);
		Assert.assertEquals(3, written.size());
		Assert.assertEquals("[07, 08, 09]", extractFilenames(written)
				.toString());
		written = files.get(1);
		Assert.assertEquals(1, written.size());		
		Assert.assertEquals("[10]", extractFilenames(written)
				.toString());
	}

	private List<String> extractFilenames(List files) {
		List<String> r = new ArrayList<String>();
		for (Object o : files) {
			File file = (File) o;
			r.add(FilenameUtils.getBaseName(file.getName()));
		}
		return r;
	}

}
