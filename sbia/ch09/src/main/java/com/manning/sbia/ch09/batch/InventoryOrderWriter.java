/**
 * 
 */
package com.manning.sbia.ch09.batch;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch09.domain.Order;
import com.manning.sbia.ch09.domain.OrderItem;

/**
 * @author acogoluegnes
 *
 */
public class InventoryOrderWriter implements ItemWriter<Order> {
	
	private JdbcTemplate jdbcTemplate;
	
	public InventoryOrderWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends Order> orders) throws Exception {
		for(Order order: orders) {
			updateInventory(order);
			track(order);
		}
	}

	private void updateInventory(Order order) {
		for(OrderItem item : order.getItems()) {
			jdbcTemplate.update("update inventory set quantity = quantity - ? where product_id = ?",
				item.getQuantity(),item.getProductId()	
			);
		}
	}
	
	private void track(Order order) {
		jdbcTemplate.update("insert into inventory_order (order_id,processing_date) values (?,?)",
			order.getOrderId(),new Date()	
		);
	}

}
