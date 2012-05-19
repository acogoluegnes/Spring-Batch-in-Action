/**
 * 
 */
package com.manning.sbia.ch12;

import java.util.List;
import java.util.Map;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author templth
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JobJmxTest extends AbstractJobStructureTest {

	@Test public void jmxWithSuccess() throws Exception {
		launchSuccessJob();

		//see http://download.oracle.com/javase/tutorial/jmx/remote/custom.html
		
		JMXServiceURL url =
            new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/myconnector");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("spring:service=batch,bean=jobOperator");

        JobOperator jobOperator =
            	JMX.newMBeanProxy(mbsc, mbeanName, JobOperator.class, true);
        List<Long> jobInstances = jobOperator.getJobInstances("importProductsJobSuccess", 0, 30);
		Assert.assertEquals(1, jobInstances.size());

		Long jobInstanceId = jobInstances.get(0);
		Assert.assertNotNull(jobInstanceId);
		
		List<Long> jobExecutions = jobOperator.getExecutions(jobInstanceId);
		Assert.assertEquals(1, jobExecutions.size());
		
		Long jobExecutionId = jobExecutions.get(0);
		String jobExecutionSummary = jobOperator.getSummary(jobExecutionId);
		Assert.assertTrue(jobExecutionSummary.contains("Job=[importProductsJobSuccess]"));
		Assert.assertTrue(jobExecutionSummary.contains("exitCode=COMPLETED;exitDescription="));

		Map<Long,String> stepExecutionSummaries = jobOperator.getStepExecutionSummaries(jobExecutionId);
		Assert.assertEquals(1, stepExecutionSummaries.size());
		String stepExecutionSummary = stepExecutionSummaries.values().iterator().next();

		Assert.assertTrue(stepExecutionSummary.contains("status=COMPLETED"));
		Assert.assertTrue(stepExecutionSummary.contains("exitStatus=COMPLETED"));
		Assert.assertTrue(stepExecutionSummary.contains("exitDescription="));
		Assert.assertTrue(stepExecutionSummary.contains("name=readWriteSuccess"));
		Assert.assertTrue(stepExecutionSummary.contains("readCount=8"));
		Assert.assertTrue(stepExecutionSummary.contains("writeCount=8"));
		Assert.assertTrue(stepExecutionSummary.contains("filterCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("readSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("writeSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("processSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("commitCount=1"));
		Assert.assertTrue(stepExecutionSummary.contains("rollbackCount=0"));
	}
	
	@Test public void jmxWithFailure() throws Exception {
		launchFailureJob();

		//see http://download.oracle.com/javase/tutorial/jmx/remote/custom.html
		
		JMXServiceURL url =
            new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/myconnector");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName("spring:service=batch,bean=jobOperator");

        JobOperator jobOperator =
            	JMX.newMBeanProxy(mbsc, mbeanName, JobOperator.class, true);
        List<Long> jobInstances = jobOperator.getJobInstances("importProductsJobFailure", 0, 30);
		Assert.assertEquals(1, jobInstances.size());

		Long jobInstanceId = jobInstances.get(0);
		Assert.assertNotNull(jobInstanceId);
		
		List<Long> jobExecutions = jobOperator.getExecutions(jobInstanceId);
		Assert.assertEquals(1, jobExecutions.size());
		
		Long jobExecutionId = jobExecutions.get(0);
		String jobExecutionSummary = jobOperator.getSummary(jobExecutionId);
		Assert.assertTrue(jobExecutionSummary.contains("Job=[importProductsJobFailure]"));
		Assert.assertTrue(jobExecutionSummary.contains("exitCode=FAILED;exitDescription="));

		Map<Long,String> stepExecutionSummaries = jobOperator.getStepExecutionSummaries(jobExecutionId);
		Assert.assertEquals(1, stepExecutionSummaries.size());
		String stepExecutionSummary = stepExecutionSummaries.values().iterator().next();

		Assert.assertTrue(stepExecutionSummary.contains("status=FAILED, exitStatus=FAILED"));
		Assert.assertTrue(stepExecutionSummary.contains("exitDescription=org.springframework.batch.item.file.FlatFileParseException"));
		Assert.assertTrue(stepExecutionSummary.contains("input=[PR....210,BlackBerry 8100 Pearl,,124.60dd]"));
		Assert.assertTrue(stepExecutionSummary.contains("name=readWriteFailure"));
		Assert.assertTrue(stepExecutionSummary.contains("readCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("writeCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("filterCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("readSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("writeSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("processSkipCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("commitCount=0"));
		Assert.assertTrue(stepExecutionSummary.contains("rollbackCount=1"));
	}
	
}
