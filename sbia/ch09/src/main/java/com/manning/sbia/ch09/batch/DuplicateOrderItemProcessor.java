/**
 * 
 */
package com.manning.sbia.ch09.batch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.support.converter.MessageConversionException;

import com.manning.sbia.ch09.domain.Order;

/**
 * @author acogoluegnes
 *
 */
public class DuplicateOrderItemProcessor implements
		ItemProcessor<Message, Order> {
	
	private JdbcTemplate jdbcTemplate;
	
	public DuplicateOrderItemProcessor(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Order process(Message message) throws Exception {
		Order order = extractOrder(message);
		if(message.getJMSRedelivered()) {
			if(orderAlreadyProcessed(order)) {
				order = null;
				
			}
		}
		return order;
	}

	private Order extractOrder(Message message) {
		if(message instanceof ObjectMessage) {
			try {
				return (Order) ((ObjectMessage) message).getObject();
			} catch (JMSException e) {
				throw new MessageConversionException("couldn't extract order", e);
			}
		}
		return null;
	}
	
	private boolean orderAlreadyProcessed(Order order) {
		return jdbcTemplate.queryForInt(
			"select count(1) from inventory_order where order_id = ?",
			order.getOrderId()) > 0;
	}

}
