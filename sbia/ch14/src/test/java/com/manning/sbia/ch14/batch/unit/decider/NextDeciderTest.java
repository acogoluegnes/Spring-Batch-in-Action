/**
 *
 */
package com.manning.sbia.ch14.batch.unit.decider;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import com.manning.sbia.ch14.batch.NextDecider;

import static org.junit.Assert.assertEquals;

/**
 * Unit with mock Spring Batch.
 *
 * @author bazoud
 *
 */
public class NextDeciderTest {
  StepExecution stepExecution = null;
  JobExecution jobExecution = null;
  NextDecider decider = null;
  
  @Before
  public void setUp() {
    stepExecution = MetaDataInstanceFactory.createStepExecution();
    jobExecution = MetaDataInstanceFactory.createJobExecution();
    decider = new NextDecider();
  }

  @Test
  public void testNextStatus() {
    stepExecution.setWriteCount(5);
    FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
    assertEquals(status.getName(), "NEXT");
  }

  @Test
  public void testCompletedStatus() {
    stepExecution.setWriteCount(0);
    FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
    assertEquals(status, FlowExecutionStatus.COMPLETED);
  }
}
