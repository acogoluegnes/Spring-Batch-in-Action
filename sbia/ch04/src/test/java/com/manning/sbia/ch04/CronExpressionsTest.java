/**
 * 
 */
package com.manning.sbia.ch04;

import org.junit.Test;
import org.quartz.CronExpression;

/**
 * @author acogoluegnes
 *
 */
public class CronExpressionsTest {

	@Test public void cronExpressionSection() throws Exception {
		// in figure
		new CronExpression("0 15 10 * * ? 2011");
		
		// in special characters table
		new CronExpression("* * * * * ?");
		new CronExpression("0 15 8 10 * ?");
		new CronExpression("0 15 8 ? * MON");
		new CronExpression("0 15 8 L * ?");
		new CronExpression("0 15 8 ? * 6L");
		new CronExpression("0 15 8 15W * ?");
		new CronExpression("0 15 8 LW * ?");
		new CronExpression("0 15 8 ? * 2#1");
		new CronExpression("0 15 8 ? * 6#1");
		
		new CronExpression("0 0 18 ? * 4 2010-2011");
		new CronExpression("0 0 23 LW * ?");
		
		// examples in table
		new CronExpression("0 0 4 * * ?");
		new CronExpression("0 0 4 * * ? 2011");
		new CronExpression("0 0/5 8-18 ? * MON-FRI");
		new CronExpression("0 0 23 L * ?");
		new CronExpression("0 0 23 LW * ?");
		new CronExpression("0 0 23 15 * ?");
		new CronExpression("0 0 23 ? * 6#3");

	}
	
}
