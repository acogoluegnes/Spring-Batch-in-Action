package com.manning.sbia.ch12.batch;

import org.aspectj.lang.JoinPoint;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class StepExecutionApplicationEventAdvice implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void before(JoinPoint jp, StepExecution stepExecution) {
		String msg = "Before: " + jp.toShortString() + " with: " + stepExecution;
		publish(jp.getTarget(), msg);
	}

	public void after(JoinPoint jp, StepExecution stepExecution) {
		String msg = "After: " + jp.toShortString() + " with: " + stepExecution;
		publish(jp.getTarget(), msg);
	}

	public void onError(JoinPoint jp, StepExecution stepExecution, Throwable t) {
		String msg = "Error in: " + jp.toShortString() + " with: " + stepExecution + " (" + t.getClass() + ":" + t.getMessage() + ")";
		publish(jp.getTarget(), msg);
	}

	/*
	 * Publish a {@link SimpleMessageApplicationEvent} with the given
	 * parameters.
	 */
	private void publish(Object source, String message) {
		applicationEventPublisher.publishEvent(new SimpleMessageApplicationEvent(source, message));
	}

}