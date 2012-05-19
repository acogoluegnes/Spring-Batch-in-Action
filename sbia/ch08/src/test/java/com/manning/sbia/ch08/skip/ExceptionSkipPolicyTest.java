/**
 * 
 */
package com.manning.sbia.ch08.skip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.file.FlatFileParseException;

import com.manning.sbia.ch08.skip.ExceptionSkipPolicy;

/**
 * @author acogoluegnes
 *
 */
public class ExceptionSkipPolicyTest {
	
	private ExceptionSkipPolicy skipPolicy = new ExceptionSkipPolicy(
			ParseException.class
	);
	
	private int whateverSkipCount = -1;

	@Test public void shouldSkip() {
		Assert.assertTrue(skipPolicy.shouldSkip(
				new FlatFileParseException("", ""), 
				whateverSkipCount)
		);
		Assert.assertTrue(skipPolicy.shouldSkip(
				new ParseException(""), 
				whateverSkipCount)
		);
		Assert.assertFalse(skipPolicy.shouldSkip(
				new ItemReaderException("") {}, 
				whateverSkipCount)
		);
	}
	
}
