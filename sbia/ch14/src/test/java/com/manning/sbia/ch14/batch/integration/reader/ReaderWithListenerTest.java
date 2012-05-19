/**
 *
 */
package com.manning.sbia.ch14.batch.integration.reader;

import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.batch.test.MetaDataInstanceFactory.createStepExecution;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.manning.sbia.ch14.domain.Product;

/**
 * Integration with Spring Batch Test.
 * @author bazoud
 *
 */
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
@TestExecutionListeners({//
DependencyInjectionTestExecutionListener.class,//
    StepScopeTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ReaderWithListenerTest {
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

  @Before
  public void setUp() {
    ((ItemStream) reader).open(new ExecutionContext());
  }

  @After
  public void tearDown() {
    ((ItemStream) reader).close();
  }

  @Test
  @DirtiesContext
  public void testReader() throws Exception {
    Product p = reader.read();
    assertNotNull(p);
    assertEquals("211", p.getId());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNotNull(reader.read());
    assertNull(reader.read());
  }
}
