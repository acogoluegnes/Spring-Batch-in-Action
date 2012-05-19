/**
 * 
 */
package com.manning.sbia.ch08.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.listener.RetryListenerSupport;

/**
 * @author acogoluegnes
 *
 */
public class Slf4jRetryListener extends RetryListenerSupport {
	
	private static final Logger LOG = LoggerFactory.getLogger(Slf4jRetryListener.class);

	@Override
	public <T> void onError(RetryContext context, RetryCallback<T> callback,
			Throwable throwable) {
		LOG.error("retried operation",throwable);
	}
	
}
