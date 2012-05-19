/**
 * 
 */
package com.manning.sbia.ch04;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

/**
 * @author acogoluegnes
 *
 */
public class SpringSchedulingLauncher {
	
	private Job job;
	
	private JobLauncher jobLauncher;
	
	public void launch() throws Exception {
		JobParameters jobParams = createJobParameters();
		jobLauncher.run(job, jobParams);
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
