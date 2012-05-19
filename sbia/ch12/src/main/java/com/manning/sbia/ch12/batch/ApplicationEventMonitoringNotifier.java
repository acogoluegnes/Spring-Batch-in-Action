package com.manning.sbia.ch12.batch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class ApplicationEventMonitoringNotifier implements
		ApplicationEventPublisherAware, BatchMonitoringNotifier {
	private ApplicationEventPublisher applicationEventPublisher;

	private String formatExceptionMessage(Throwable exception) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));
		return baos.toString();
	}

	private String createMessageContent(JobExecution jobExecution) {
		List<Throwable> exceptions = jobExecution.getFailureExceptions();
		StringBuffer content = new StringBuffer();
		content.append("Job execution #");
		content.append(jobExecution.getId());
		content.append(" of job instance #");
		content.append(jobExecution.getJobInstance().getId());
		content.append(" failed with following exceptions:");
		for (Throwable exception : exceptions) {
			content.append("");
			content.append(formatExceptionMessage(exception));
		}
		return content.toString();
	}

	public void notify(JobExecution jobExecution) {
		String content = createMessageContent(jobExecution);
		applicationEventPublisher
				.publishEvent(new SimpleMessageApplicationEvent(this, content));
	}

	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
