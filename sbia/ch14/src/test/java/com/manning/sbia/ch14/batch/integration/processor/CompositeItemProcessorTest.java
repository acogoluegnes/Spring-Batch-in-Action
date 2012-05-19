/**
 *
 */
package com.manning.sbia.ch14.batch.integration.processor;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.manning.sbia.ch14.domain.Product;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.springframework.batch.test.MetaDataInstanceFactory.createStepExecution;

/**
 * Unit with Spring Batch.
 *
 * @author bazoud
 *
 */
@ContextConfiguration("/com/manning/sbia/ch14/spring/test-job-context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    StepScopeTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class CompositeItemProcessorTest {
  @Autowired
  private ItemProcessor<Product, Product> processor;

  public StepExecution getStepExecution() {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addDouble("maxPrice", 200.00d) //
        .toJobParameters();
    StepExecution execution = createStepExecution(jobParameters);
    return execution;
  }

  @Test
  @DirtiesContext
  public void testProcessor() throws Exception {
    Product p1 = new Product();
    p1.setPrice(new BigDecimal("100.0"));
    Product p2 = processor.process(p1);
    assertNotNull(p2);
  }

  @Test
  @DirtiesContext
  public void testZeorPriceFailure() throws Exception {
    Product p1 = new Product();
    p1.setPrice(new BigDecimal("0.0"));
    Product p2 = processor.process(p1);
    assertNull(p2);
  }

  @Test
  @DirtiesContext
  public void testNegativePriceFailure() throws Exception {
    Product p1 = new Product();
    p1.setPrice(new BigDecimal("-800.0"));
    Product p2 = processor.process(p1);
    assertNull(p2);
  }

  @Test
  @DirtiesContext
  public void testEmptyProductFailure() throws Exception {
    Product p1 = new Product();
    Product p2 = processor.process(p1);
    assertNull(p2);
  }
}
