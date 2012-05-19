/**
 * 
 */
package com.manning.sbia.ch09.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch09.domain.Order;

/**
 * @author acogoluegnes
 *
 */
public class ShippedOrderWriter implements ItemWriter<Order> {
	
	private JdbcTemplate jdbcTemplate;
	
	public ShippedOrderWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(final List<? extends Order> items) throws Exception {
		jdbcTemplate.batchUpdate("update orders set shipped = ?",
			new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setBoolean(1, true);
				}
				
				@Override
				public int getBatchSize() {
					return items.size();
				}
			}	
		);
	}

}
