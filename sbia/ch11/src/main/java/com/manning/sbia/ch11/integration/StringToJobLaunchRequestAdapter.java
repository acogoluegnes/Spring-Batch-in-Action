/**
 * 
 */
package com.manning.sbia.ch11.integration;

import java.util.Properties;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.util.StringUtils;

/**
 * @author acogoluegnes
 *
 */
public class StringToJobLaunchRequestAdapter {

	private static String PATTERN = "([\\w-_]*)(\\[(.*)\\]|.*)";
	
	@ServiceActivator
	public JobLaunchRequest adapt(String request) {
		request = request.trim();
		String jobName = request.replaceAll(PATTERN, "$1");
		String paramsText = request.replaceAll(PATTERN, "$3");
		Properties params = StringUtils.splitArrayElementsIntoProperties(paramsText.split(","), "=");
		return new JobLaunchRequest(jobName, params);
	}
	
}
