/**
 *
 */
package com.manning.sbia.ch14.batch.integration.reader;

import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static org.junit.Assert.assertEquals;
import static org.springframework.batch.test.MetaDataInstanceFactory.createStepExecution;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch14.domain.Product;

/**
 * Integration with Spring Batch Test.
 * @author bazoud
 *
 */
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ReaderWithStepScopeTestUtilsTest {
  String PRODUCTS_PATH = "classpath:com/manning/sbia/ch14/input/products.txt";
  @Autowired
  private ItemReader<Product> reader;

  public StepExecution getStepExecution() {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString(PARAM_INPUT_RESOURCE, PRODUCTS_PATH) //
        .toJobParameters();
    StepExecution execution = createStepExecution(jobParameters);
    return execution;
  }

  @Test
  @DirtiesContext
  public void testReader() throws Exception {
    int count = StepScopeTestUtils.doInStepScope(getStepExecution(),
        new Callable<Integer>() {
          @Override
          public Integer call() throws Exception {
            int count = 0;
            try {
              ((ItemStream) reader).open(new ExecutionContext());
              while (reader.read() != null) {
                count++;
              }
              return count;
            } finally {
              ((ItemStream) reader).close();
            }
          }
        });
    assertEquals(8, count);
  }
}
