package com.manning.sbia.ch04;

import java.util.concurrent.CountDownLatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CountDownTasklet implements Tasklet {

	private CountDownLatch countDownLatch;
	
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		countDownLatch.countDown();
		return RepeatStatus.FINISHED;
	}
	
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
}