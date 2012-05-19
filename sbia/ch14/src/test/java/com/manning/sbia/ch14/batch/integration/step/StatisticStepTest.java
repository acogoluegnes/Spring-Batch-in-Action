/**
 *
 */
package com.manning.sbia.ch14.batch.integration.step;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch14.domain.Product;

import static com.manning.sbia.ch14.batch.ProductItemWriter.INSERT_SQL;

import static org.junit.Assert.assertEquals;

import static org.springframework.batch.test.AssertFile.assertFileEquals;

/**
 * Integration with Spring Batch Test.
 *
 * @author bazoud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
public class StatisticStepTest {
  String STATISTIC_REF_PATH = "com/manning/sbia/ch14/output/statistic-summary.txt";
  String STATISTIC_PATH = "./target/statistic-summary.txt";
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private SimpleJdbcTemplate jdbcTemplate;
  ItemSqlParameterSourceProvider<Product> ispsp;

  @Before
  public void setup() {
      ispsp = new BeanPropertyItemSqlParameterSourceProvider<Product>();

      Product product = new Product();
      product.setId("1");
      product.setDescription("");
      product.setPrice(new BigDecimal(10.0f));

      SqlParameterSource args = ispsp.createSqlParameterSource(product);
      jdbcTemplate.update(INSERT_SQL, args);

      product = new Product();
      product.setId("2");
      product.setDescription("");
      product.setPrice(new BigDecimal(30.0f));
      args = ispsp.createSqlParameterSource(product);
      jdbcTemplate.update(INSERT_SQL, args);
  }
  
  @Test
  @DirtiesContext
  public void integration() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString("reportResource", "file:" + STATISTIC_PATH) //
        .toJobParameters();

    JobExecution exec = jobLauncherTestUtils.launchStep("statisticStep",
        jobParameters);
    assertEquals(BatchStatus.COMPLETED, exec.getStatus());
    StepExecution setpExec = exec.getStepExecutions().iterator().next();
    assertEquals(1, setpExec.getWriteCount());

    assertFileEquals( //
        new ClassPathResource(STATISTIC_REF_PATH), //
        new FileSystemResource(STATISTIC_PATH));
  }
}
