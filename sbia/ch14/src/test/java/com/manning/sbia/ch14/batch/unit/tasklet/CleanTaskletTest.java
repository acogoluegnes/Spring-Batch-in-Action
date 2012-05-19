/**
 *
 */
package com.manning.sbia.ch14.batch.unit.tasklet;

import org.junit.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.manning.sbia.ch14.batch.CleanTasklet;

import static org.junit.Assert.assertEquals;

import static org.springframework.batch.test.MetaDataInstanceFactory.createStepExecution;

/**
 * @author bazoud
 *
 */
public class CleanTaskletTest {
  @Test
  public void clean() throws Exception {
    StepExecution stepExecution = createStepExecution();
    StepContribution contrib = new StepContribution(stepExecution);
    ChunkContext context = new ChunkContext(new StepContext(stepExecution));
    CleanTasklet clean = new CleanTasklet();
    RepeatStatus status = clean.execute(contrib, context);
    assertEquals(RepeatStatus.FINISHED, status);
  }
}
