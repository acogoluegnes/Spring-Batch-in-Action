/**
 * 
 */
package com.manning.sbia.ch08.skip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * @author acogoluegnes
 *
 */
public class Slf4jSkipListener {

	private static final Logger LOG = LoggerFactory.getLogger(Slf4jSkipListener.class);
	
	@OnSkipInRead
	public void log(Throwable t) {
		if(t instanceof FlatFileParseException) {
			FlatFileParseException ffpe = (FlatFileParseException) t;
			LOG.error(ffpe.getInput());
		}
	}
	
}
