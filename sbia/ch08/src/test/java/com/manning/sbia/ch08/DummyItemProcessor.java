/**
 * 
 */
package com.manning.sbia.ch08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author acogoluegnes
 *
 */
public class DummyItemProcessor implements ItemProcessor<String, String> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DummyItemProcessor.class);
	
	private BusinessService service;
	
	public DummyItemProcessor(BusinessService service) {
		super();
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public String process(String item) throws Exception {
		LOG.debug("processing "+item);
		service.processing(item);
		LOG.debug("after processing "+item);
		return item;
	}

}
