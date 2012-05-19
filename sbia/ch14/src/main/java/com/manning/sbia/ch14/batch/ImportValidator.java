/**
 *
 */
package com.manning.sbia.ch14.batch;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * @author bazoud
 *
 */
public class ImportValidator implements JobParametersValidator,
    ResourceLoaderAware {
  public static final String PARAM_INPUT_RESOURCE = "inputResource";
  public static final String PARAM_REPORT_RESOURCE = "reportResource";
  private ResourceLoader resourceLoader;

  @Override
  public void validate(JobParameters parameters)
      throws JobParametersInvalidException {
    Collection<String> missing = new ArrayList<String>();
    checkParameter(PARAM_INPUT_RESOURCE, parameters, missing);
    checkParameter(PARAM_REPORT_RESOURCE, parameters, missing);
    if (!missing.isEmpty()) {
      throw new JobParametersInvalidException("Missing parameter: " + missing);
    }
    if (!resourceLoader.getResource(parameters.getString(PARAM_INPUT_RESOURCE))
        .exists()) {
      throw new JobParametersInvalidException("The input file: "
          + parameters.getString(PARAM_INPUT_RESOURCE) + " does not exist");
    }
  }

  private void checkParameter(String key, JobParameters parameters,
      Collection<String> missing) {
    if (!parameters.getParameters().containsKey(key)) {
      missing.add(key);
    }
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
}
