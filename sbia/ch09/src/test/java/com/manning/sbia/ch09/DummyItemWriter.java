/**
 * 
 */
package com.manning.sbia.ch09;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

/**
 * @author acogoluegnes
 * 
 */
public class DummyItemWriter implements ItemWriter<String> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DummyItemWriter.class);

	private BusinessService service;

	public DummyItemWriter(BusinessService service) {
		super();
		this.service = service;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items) {
			LOG.debug("writing "+item);
			service.writing(item);
			LOG.debug("item written "+item);
		}
	}

}
