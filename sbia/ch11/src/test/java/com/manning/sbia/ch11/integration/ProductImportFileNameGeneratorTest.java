/**
 * 
 */
package com.manning.sbia.ch11.integration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.integration.support.MessageBuilder;

import com.manning.sbia.ch11.integration.ProductImportFileNameGenerator;

/**
 * @author acogoluegnes
 *
 */
public class ProductImportFileNameGeneratorTest {
	
	ProductImportFileNameGenerator generator = new ProductImportFileNameGenerator();

	@Test public void generateFileName() {
		String filename = generator.generateFileName(MessageBuilder.withPayload(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<products import-id=\"partner1-1\">"+
			"<product>"+
			"<id>1</id>"+
			"<title>some phone</title>"+
			"</product>"+
			"</products>"
		).build());
		Assert.assertEquals("partner1-1.xml",filename);
	}
	
}
