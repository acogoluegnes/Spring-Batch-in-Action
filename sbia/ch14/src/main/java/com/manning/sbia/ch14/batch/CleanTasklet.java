/**
 *
 */
package com.manning.sbia.ch14.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author bazoud
 *
 */
public class CleanTasklet implements Tasklet {
  @Override
  public RepeatStatus execute(StepContribution contrib, ChunkContext context)
      throws Exception {
    return RepeatStatus.FINISHED;
  }
}
