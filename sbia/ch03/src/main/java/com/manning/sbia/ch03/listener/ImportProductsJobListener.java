package com.manning.sbia.ch03.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ImportProductsJobListener implements JobExecutionListener {
	public void beforeJob(JobExecution jobExecution) {
		// Called when job starts
		System.out.println("Called when job starts");
	}

	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus()==BatchStatus.COMPLETED) {
			// Called when job ends successfully
			System.out.println("Called when job ends successfully");
		} else if (jobExecution.getStatus()==BatchStatus.FAILED) {
			// Called when job ends in failure
			System.out.println("Called when job ends in failure");
		}
	}

}
