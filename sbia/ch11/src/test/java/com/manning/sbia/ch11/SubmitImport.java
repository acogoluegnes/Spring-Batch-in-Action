/**
 * 
 */
package com.manning.sbia.ch11;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author acogoluegnes
 * 
 */
public class SubmitImport {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String importId = "partner1-1";
		restTemplate.put(
			"http://localhost:8080/enterpriseintegration/product-imports/{importId}",
			loadProductFiles(importId), importId);
	}

	private static String loadProductFiles(String importId) throws Exception {
		return FileUtils.readFileToString(new File(new File("./data-samples/"),
				"products-" + importId + ".xml"), "UTF-8");
	}

}
