/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

/**
 * @author acogoluegnes
 *
 */
public class QuartzLauncher implements org.quartz.Job { 

	private Job job;
	
	private JobLauncher jobLauncher;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobParameters jobParams = createJobParameters();
			jobLauncher.run(job, jobParams);
		} catch(Exception e) {
			throw new JobExecutionException("error while executing Spring Batch job", e);
		}
	}
	

	private JobParameters createJobParameters() {
		JobParameters jobParams = new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters();
		return jobParams;
	}
	
	public void setJob(Job job) {
		this.job = job;
	}
	
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	
}
