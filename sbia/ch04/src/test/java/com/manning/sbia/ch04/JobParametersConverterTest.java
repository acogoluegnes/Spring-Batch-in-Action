/**
 * 
 */
package com.manning.sbia.ch04;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.util.StringUtils;

/**
 * @author acogoluegnes
 *
 */
public class JobParametersConverterTest {

	@Test public void convert() throws Exception {
		// trick to make the test portable (for double) on different OS
		// the default number format used by the converter truncates digits
		double doubleParam = 23.4;
		NumberFormat format = new DecimalFormat("#"); // the one used in the converter
		doubleParam = format.parse(format.format(doubleParam)).doubleValue();
		Properties params = StringUtils.splitArrayElementsIntoProperties(new String[]{
			"someString(string)=someStringValue",
			"someDate(date)=2010/12/08",
			"someLong(long)=23",
			"someDouble(double)="+format.format(doubleParam)
		}, "=");
		JobParametersConverter converter = new DefaultJobParametersConverter();
		JobParameters parameters = converter.getJobParameters(params);
		Assert.assertEquals("someStringValue",parameters.getString("someString"));
		Assert.assertEquals(23L,parameters.getLong("someLong"));
		Assert.assertEquals(doubleParam,parameters.getDouble("someDouble"),0);
		Assert.assertEquals(
			"2010/12/08",
			new SimpleDateFormat("yyyy/MM/dd").format(parameters.getDate("someDate"))
		);
	}
	
}
