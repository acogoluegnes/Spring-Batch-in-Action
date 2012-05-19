/**
 * 
 */
package com.manning.sbia.ch09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * @author acogoluegnes
 *
 */
public class DummyItemReader implements ItemReader<String> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DummyItemReader.class);
	
	private BusinessService service;
	
	public DummyItemReader(BusinessService service) {
		super();
		this.service = service;
	}



	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public String read() throws Exception, UnexpectedInputException,
			ParseException {
		String read = service.reading();
		LOG.debug("read "+read);
		return read;
	}

}
