package com.manning.sbia.ch03.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class ImportProductsExecutionListener {
	@BeforeStep
	public void handlingBeforeStep(StepExecution stepExecution) {
		// Called before step starts
		System.out.println("Called before step starts");
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		// Called after step ends
		System.out.println("Called after step ends");
	    return ExitStatus.COMPLETED;
	}
}
