/**
 * 
 */
package com.manning.sbia.ch11.integration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

/**
 * @author acogoluegnes
 *
 */
public class ProductImportToJobLaunchRequestHandler {

	public JobLaunchRequest adapt(File products) {
		String importId = FilenameUtils.getBaseName(products.getAbsolutePath());
		Map<String, String> jobParams = new HashMap<String, String>();
		jobParams.put("importId", importId);
		jobParams.put("inputFile", products.getAbsolutePath());
		return new JobLaunchRequest("importProducts",jobParams);
	}
	
}
