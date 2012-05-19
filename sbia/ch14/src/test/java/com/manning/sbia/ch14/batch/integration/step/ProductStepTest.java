/**
 *
 */
package com.manning.sbia.ch14.batch.integration.step;

import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static org.junit.Assert.assertEquals;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration with Spring Batch Test.
 *
 * @author bazoud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
public class ProductStepTest {
  String PRODUCTS_PATH = "classpath:com/manning/sbia/ch14/input/products.txt";
  String EXCLUDES_REF_PATH = "com/manning/sbia/ch14/output/excludes.txt";
  String EXCLUDES_PATH = "./target/excludes.txt";
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private SimpleJdbcTemplate jdbcTemplate;

  @Test
  @DirtiesContext
  public void testIntegration() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString(PARAM_INPUT_RESOURCE, PRODUCTS_PATH) //
        .toJobParameters();

    JobExecution exec = jobLauncherTestUtils.launchStep("productsStep",
        jobParameters);
    assertEquals(BatchStatus.COMPLETED, exec.getStatus());
    StepExecution setpExec = exec.getStepExecutions().iterator().next();
    assertEquals(2, setpExec.getFilterCount());
    assertEquals(6, setpExec.getWriteCount());
    assertEquals(6, jdbcTemplate.queryForInt("SELECT COUNT(*) from PRODUCT"));
    assertFileEquals(//
        new ClassPathResource(EXCLUDES_REF_PATH), //
        new FileSystemResource(EXCLUDES_PATH));
  }
}
