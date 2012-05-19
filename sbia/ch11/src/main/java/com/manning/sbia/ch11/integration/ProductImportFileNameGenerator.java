/**
 * 
 */
package com.manning.sbia.ch11.integration;

import org.springframework.integration.Message;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.util.Assert;

/**
 * @author acogoluegnes
 *
 */
public class ProductImportFileNameGenerator implements FileNameGenerator {

	/* (non-Javadoc)
	 * @see org.springframework.integration.file.FileNameGenerator#generateFileName(org.springframework.integration.core.Message)
	 */
	@Override
	public String generateFileName(Message<?> message) {
		Assert.notNull(message.getPayload());
		Assert.isInstanceOf(String.class, message.getPayload());
		String payload = (String) message.getPayload();
		return ProductImportUtils.extractImportId(payload)+".xml";
	}

}
