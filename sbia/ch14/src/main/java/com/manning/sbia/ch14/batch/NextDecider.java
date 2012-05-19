/**
 *
 */
package com.manning.sbia.ch14.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * @author bazoud
 *
 */
public class NextDecider implements JobExecutionDecider {

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution,
      StepExecution stepExecution) {
    if (stepExecution.getWriteCount() > 0) {
      return new FlowExecutionStatus("NEXT");
    }
    return FlowExecutionStatus.COMPLETED;
  }

}
