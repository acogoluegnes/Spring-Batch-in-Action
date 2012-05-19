/**
 *
 */
package com.manning.sbia.ch14.batch.unit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.core.io.ResourceLoader;

import com.manning.sbia.ch14.batch.ImportValidator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static com.manning.sbia.ch14.batch.ImportValidator.PARAM_REPORT_RESOURCE;

/**
 * Unit with mock.
 *
 * @author bazoud
 *
 */
public class ImportValidatorTest {
  String PRODUCTS_PATH = "classpath:com/manning/sbia/ch15/input/products.txt";
  String STATISTIC_PATH = "file:./target/statistic.txt";
  private ResourceLoader resourceLoader;
  private ImportValidator validator;

  @Before
  public void setUp() {
    resourceLoader = mock(ResourceLoader.class, Mockito.RETURNS_DEEP_STUBS);
    when(resourceLoader.getResource(PRODUCTS_PATH).exists()).thenReturn(true);
    validator = new ImportValidator();
    validator.setResourceLoader(resourceLoader);
  }

  @Test
  public void testJobParameters() throws JobParametersInvalidException {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString(PARAM_INPUT_RESOURCE, PRODUCTS_PATH) //
        .addString(PARAM_REPORT_RESOURCE, STATISTIC_PATH) //
        .toJobParameters();
    JobParameters spy = spy(jobParameters);
    validator.validate(spy);
    verify(spy, times(2)).getParameters();
    verify(spy, times(1)).getString(PARAM_INPUT_RESOURCE);
    verifyNoMoreInteractions(spy);
  }

  @Test(expected = JobParametersInvalidException.class)
  public void testEmptyJobParameters() throws JobParametersInvalidException {
    JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
    validator.validate(jobParameters);
  }

  @Test(expected = JobParametersInvalidException.class)
  public void testMissingJobParameters() throws JobParametersInvalidException {
    JobParameters jobParameters = new JobParametersBuilder() //
        .addString(PARAM_INPUT_RESOURCE, PRODUCTS_PATH) //
        .toJobParameters();
    validator.validate(jobParameters);
  }
}
