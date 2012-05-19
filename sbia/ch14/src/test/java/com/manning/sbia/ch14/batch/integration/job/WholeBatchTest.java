/**
 *
 */
package com.manning.sbia.ch14.batch.integration.job;

import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_REPORT_RESOURCE;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration with mock.
 *
 * @author bazoud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
public class WholeBatchTest {
  String PRODUCTS_PATH = "classpath:com/manning/sbia/ch14/input/products.txt";
  String STATISTIC_PATH = "file:./target/statistic.txt";
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private SimpleJdbcTemplate jdbcTemplate;

  @Test
  @DirtiesContext
  public void integration() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString(PARAM_INPUT_RESOURCE, PRODUCTS_PATH) //
        .addString(PARAM_REPORT_RESOURCE, STATISTIC_PATH) //
        .toJobParameters();

    JobExecution exec = jobLauncherTestUtils.launchJob(jobParameters);
    Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());
    StepExecution setpExec = exec.getStepExecutions().iterator().next();
    assertEquals(2, setpExec.getFilterCount());
    assertEquals(6, setpExec.getWriteCount());
    assertEquals(6, jdbcTemplate.queryForInt("SELECT COUNT(*) from PRODUCT"));
  }
}
