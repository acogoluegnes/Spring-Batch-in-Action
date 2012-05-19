/**
 * 
 */
package com.manning.sbia.ch05.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;

import com.manning.sbia.ch05.AbstractJobStructureTest;
import com.manning.sbia.ch05.Product;

/**
 * @author templth
 *
 */
@ContextConfiguration
public class JobStructureJmsTest extends AbstractJobStructureTest {
	@Autowired
	private JmsTemplate jmsTemplate;

	private void sendProduct(final Product product) {
		jmsTemplate.send(new MessageCreator() {
		    public Message createMessage(Session session)
		                                    throws JMSException {
		        ObjectMessage message = session.createObjectMessage();
		        message.setObject(product);
		        return message;
		    }
		});
	}
	
	@Test public void existingServiceJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
		
		Thread.sleep(2000);

		sendProduct(createProduct("PR....210", "BlackBerry 8100 Pearl", "", 124.60f));
		sendProduct(createProduct("PR....211", "Sony Ericsson W810i", "", 139.45f));
		sendProduct(createProduct("PR....212", "Samsung MM-A900M Ace", "", 97.80f));
		sendProduct(createProduct("PR....213", "Toshiba M285-E 14", "", 166.20f));
		sendProduct(createProduct("PR....214", "Nokia 2610 Phone", "", 145.50f));
		sendProduct(createProduct("PR....215", "CN Clogs Beach/Garden Clog", "", 190.70f));
		sendProduct(createProduct("PR....216", "AT&T 8525 PDAl", "", 289.20f));
		sendProduct(createProduct("PR....217", "Canon Digital Rebel XT 8MP Digital SLR Camera", "", 13.70f));
	}
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
