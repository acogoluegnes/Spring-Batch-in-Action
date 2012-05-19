/**
 * 
 */
package com.manning.sbia.ch10;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"batch-infrastructure.xml","transmit-data-holder-spel-job.xml"})
public class TransmitDataHolderSpelTest extends AbstractJobTest {
	
}
