package com.manning.sbia.ch12.batch;

import java.util.Map;

public interface JobLoader {
	void loadResource(String path);
	Map<String, String> getConfigurations();
	Object getJobConfiguration(String path);
	Object getProperty(String path);
	void setProperty(String path, String value);
}
