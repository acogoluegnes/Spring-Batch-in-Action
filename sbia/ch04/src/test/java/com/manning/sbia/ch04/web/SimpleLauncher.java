/**
 * 
 */
package com.manning.sbia.ch04.web;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * @author acogoluegnes
 *
 */
public class SimpleLauncher {

	private JobLauncher jobLauncher;
	
	private Job job;
	
	public void launch() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		jobLauncher.run(
			job, 
			new JobParametersBuilder().addDate("date", new Date()).toJobParameters()
		);
	}
	
	public void setJob(Job job) {
		this.job = job;
	}
	
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}
	
}
